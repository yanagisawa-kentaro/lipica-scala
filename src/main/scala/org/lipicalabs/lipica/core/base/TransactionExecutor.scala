package org.lipicalabs.lipica.core.base

import org.lipicalabs.lipica.core.config.SystemProperties
import org.lipicalabs.lipica.core.db.{BlockStore, Repository}
import org.lipicalabs.lipica.core.listener.LipicaListener
import org.lipicalabs.lipica.core.utils.{UtilConsts, ImmutableBytes}
import org.lipicalabs.lipica.core.vm.PrecompiledContracts.PrecompiledContract
import org.lipicalabs.lipica.core.vm._
import org.lipicalabs.lipica.core.vm.program.{ProgramResult, Program}
import org.lipicalabs.lipica.core.vm.program.invoke.ProgramInvokeFactory
import org.slf4j.LoggerFactory

/**
 * Created by IntelliJ IDEA.
 * 2015/11/22 13:32
 * YANAGISAWA, Kentaro
 */
class TransactionExecutor(
	private val tx: TransactionLike, private val coinbase: ImmutableBytes, private val track: Repository,
	private val blockStore: BlockStore, private val programInvokeFactory: ProgramInvokeFactory,
	private val currentBlock: Block, private val listener: LipicaListener, private val manaUsedInTheBlock: Long
) {
	import TransactionExecutor._

	private val cacheTrack: Repository = this.track.startTracking
	private var vm: VM = null
	private var program: Program = null
	private var precompiledContract: Option[PrecompiledContract] = None
	private var result: ProgramResult = new ProgramResult
	private var _logs: Seq[LogInfo] = Seq.empty

	private var readyToExecute: Boolean = false
	private var basicTxCost: Long = 0L
	private var endMana: Long = 0L

	private var _localCall: Boolean = false
	def localCall: Boolean = this._localCall
	def localCall_=(v: Boolean): Unit = this._localCall = v

	def init(): Unit = {
		if (this.localCall) {
			this.readyToExecute = true
			return
		}
		val txManaLimit = this.tx.manaLimit.toPositiveBigInt.longValue()
		//新たなトランザクションを処理すると、このブロックのマナ上限を超えるか？
		val exceedingLimit = this.currentBlock.manaLimit < (this.manaUsedInTheBlock + txManaLimit)
		if (exceedingLimit) {
			logger.info("<TxExecutor> Mana limit reached: Limit of block: %s, Already used: %s, Required: %s".format(
				this.currentBlock.manaLimit, this.manaUsedInTheBlock, txManaLimit
			))
			return
		}
		this.basicTxCost = this.tx.transactionCost
		if (txManaLimit < this.basicTxCost) {
			logger.info("<TxExecutor> Not enough mana for tx execution: %s < %s".format(txManaLimit, this.basicTxCost))
			return
		}
		val reqNonce = this.track.getNonce(this.tx.sendAddress)
		val txNonce = this.tx.nonce.toPositiveBigInt
		if (reqNonce != txNonce) {
			logger.info("<TxExecutor> Invalid nonce: Required: %s != Tx: %s".format(reqNonce, txNonce))
			return
		}
		val txManaCost = this.tx.manaPrice.toPositiveBigInt * BigInt(txManaLimit)
		val totalCost = this.tx.value.toPositiveBigInt + txManaCost
		val senderBalance = this.track.getBalance(this.tx.sendAddress).getOrElse(UtilConsts.Zero)
		if (senderBalance < totalCost) {
			logger.info("<TxExecutor> Not enough coin: Required: %s, Sender balance: %s".format(totalCost, senderBalance))
			return
		}
		this.readyToExecute = true
	}

	def execute(): Unit = {
		if (!this.readyToExecute) {
			return
		}
		if (!localCall) {
			this.track.increaseNonce(this.tx.sendAddress)
			val txManaPrice = this.tx.manaPrice.toPositiveBigInt
			val txManaLimit = this.tx.manaLimit.toPositiveBigInt
			val txManaCost = txManaPrice * txManaLimit
			this.track.addBalance(tx.sendAddress, -txManaCost)
			logger.info("<TxExecutor> Paying: TxManaCost: %s, ManaPrice: %s, ManaLimit: %s".format(txManaCost, txManaPrice, txManaLimit))
		}
		if (this.tx.isContractCreation) {
			create()
		} else {
			call()
		}
	}

	private def create(): Unit = {
		val newContractAddress = this.tx.getContractAddress
		if (this.tx.data.isEmpty) {
			this.endMana = this.tx.manaLimit.toPositiveBigInt.longValue() - this.basicTxCost
			this.cacheTrack.createAccount(this.tx.getContractAddress)
		} else {
			val invoke = this.programInvokeFactory.createProgramInvoke(tx, currentBlock, cacheTrack, blockStore)
			this.vm = new VM
			this.program = new Program(this.tx.data, invoke, this.tx)
		}
		val endowment = this.tx.value.toPositiveBigInt
		Transfer.transfer(this.cacheTrack, this.tx.sendAddress, newContractAddress, endowment)
	}

	private def call(): Unit = {
		if (!readyToExecute) {
			return
		}
		val targetAddress = this.tx.receiveAddress
		this.precompiledContract = PrecompiledContracts.getContractForAddress(DataWord(targetAddress))
		this.precompiledContract match {
			case Some(contract) =>
				val requiredMana = contract.manaForData(this.tx.data)
				val txManaLimit = this.tx.manaLimit.toPositiveBigInt.longValue()
				if (!localCall && txManaLimit < requiredMana) {
					//コストオーバー。
					return
				} else {
					this.endMana = txManaLimit - requiredMana - this.basicTxCost
					contract.execute(this.tx.data)
				}
			case None =>
				this.track.getCode(targetAddress) match {
					case Some(code) =>
						val invoke = this.programInvokeFactory.createProgramInvoke(this.tx, this.currentBlock, this.cacheTrack, this.blockStore)
						this.vm = new VM
						this.program = new Program(code, invoke, this.tx)
					case None =>
						this.endMana = this.tx.manaLimit.toPositiveBigInt.longValue() - this.basicTxCost
				}
		}

		val endowment = this.tx.value.toPositiveBigInt
		Transfer.transfer(this.cacheTrack, this.tx.sendAddress, targetAddress, endowment)
	}

	def go(): Unit = {
		if (!readyToExecute) {
			return
		}
		if (this.vm eq null) {
			return
		}
		try {
			this.program.spendMana(this.tx.transactionCost, "Tx Cost")
			this.vm.play(this.program)
			this.result = this.program.result
			this.endMana = (this.tx.manaLimit.toPositiveBigInt - BigInt(this.result.manaUsed)).longValue()

			if (this.tx.isContractCreation) {
				val returnDataManaValue = result.hReturn.length * ManaCost.CreateData
				if (returnDataManaValue <= this.endMana) {
					this.endMana -= returnDataManaValue
					this.cacheTrack.saveCode(this.tx.getContractAddress, result.hReturn)
				} else {
					//足りない。
					result.hReturn = ImmutableBytes.empty
				}
			}
			if (this.result.exception ne null) {
				this.result.clearDeletedAccounts()
				result.clearLogInfoList()
				result.resetFutureRefund()
				throw result.exception
			}
		} catch {
			case e: Throwable =>
				this.cacheTrack.rollback()
				this.endMana = 0
		}
	}

	def finalization(): Unit = {
		if (!this.readyToExecute) {
			return
		}
		this.cacheTrack.commit()
		val summaryBuilder = TransactionExecutionSummary.builder(this.tx).manaLeftOver(this.endMana)
		if (this.result ne null) {
			this.result.addFutureRefund(this.result.deletedAccounts.size * ManaCost.SuicideRefund)
			val manaRefund = this.result.futureRefund min (this.result.manaUsed / 2)
			val address = if (this.tx.isContractCreation) this.tx.getContractAddress else this.tx.receiveAddress
			this.endMana += manaRefund

			summaryBuilder.manaUsed(BigInt(this.result.manaUsed)).manaRefund(BigInt(manaRefund)).
				deletedAccounts(this.result.deletedAccounts).
				internalTransactions(this.result.internalTransactions).
				storageDiff(this.track.getContractDetails(address).get.storageContent)
			if (this.result.exception ne null) {
				summaryBuilder.markAsFailed
			}
		}
		val summary = summaryBuilder.build

		//払い戻す。
		this.track.addBalance(this.tx.sendAddress, summary.calculateLeftOver + summary.calculateRefund)
		logger.info("<TxExecutor> Paying totla refund to sender: %s, refund val: %s".format(this.tx.sendAddress, summary.calculateRefund))
		//採掘報酬。
		this.track.addBalance(this.coinbase, summary.calculateFee)
		logger.info("<TxExecutor> Paying fee to miner: %s, fee: %s".format(this.coinbase, summary.calculateFee))

		Option(this.result).foreach {
			r => {
				this._logs = r.logInfoList
				r.deletedAccounts.foreach(address => this.track.delete(address.last20Bytes))
			}
		}
		this.listener.onTransactionExecuted(summary)
		if (SystemProperties.CONFIG.vmTrace) {
			val trace = this.program.trace.result(this.result.hReturn).error(this.result.exception).toString
			saveProgramTrace(this.tx.hash.toHexString, trace)
			this.listener.onVMTraceCreated(this.tx.hash.toHexString, trace)
		}
	}

	private def saveProgramTrace(txHash: String, content: String): Unit = {
		//TODO 未実装。
	}

	def logs: Seq[LogInfo] = this._logs
	def resultOption: Option[ProgramResult] = Option(this.result)
	def manaUsed: Long = this.tx.manaLimit.toPositiveBigInt.longValue() - this.endMana

}

object TransactionExecutor {
	private val logger = LoggerFactory.getLogger("execute")
}