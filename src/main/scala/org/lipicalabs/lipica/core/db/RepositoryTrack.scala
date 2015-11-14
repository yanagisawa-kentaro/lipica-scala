package org.lipicalabs.lipica.core.db

import org.lipicalabs.lipica.core.base.{Block, AccountState, Repository}
import org.lipicalabs.lipica.core.crypto.digest.DigestUtils
import org.lipicalabs.lipica.core.utils.{UtilConsts, ImmutableBytes}
import org.lipicalabs.lipica.core.vm.DataWord
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
 * Created by IntelliJ IDEA.
 * 2015/11/09 20:37
 * YANAGISAWA, Kentaro
 */
class RepositoryTrack(private val repository: Repository) extends Repository {

	import RepositoryTrack._

	private val cacheAccounts = new mutable.HashMap[ImmutableBytes, AccountState]
	private val cacheDetails = new mutable.HashMap[ImmutableBytes, ContractDetails]

	override def createAccount(address: ImmutableBytes) = {
		if (logger.isTraceEnabled) {
			logger.trace("<RepositoryTrack> Creating account: [%s]".format(address))
		}
		val accountState = new AccountState
		this.cacheAccounts.put(address, accountState)

		val contractDetails = new ContractDetailsImpl
		contractDetails.isDirty = true
		this.cacheDetails.put(address, contractDetails)

		accountState
	}

	override def getAccountState(address: ImmutableBytes) = {
		this.cacheAccounts.get(address) match {
			case Some(account) => Some(account)
			case _ =>
				this.repository.loadAccount(address, this.cacheAccounts, this.cacheDetails)
				this.cacheAccounts.get(address)
		}
	}

	override def existsAccount(address: ImmutableBytes) = {
		this.cacheAccounts.get(address) match {
			case Some(account) => !account.isDeleted
			case _ => this.repository.existsAccount(address)
		}
	}

	override def getContractDetails(address: ImmutableBytes) = {
		this.cacheDetails.get(address) match {
			case Some(details) => Some(details)
			case _ =>
				this.repository.loadAccount(address, this.cacheAccounts, this.cacheDetails)
				this.cacheDetails.get(address)
		}
	}

	override def loadAccount(address: ImmutableBytes, cacheAccounts: mutable.Map[ImmutableBytes, AccountState], cacheDetails: mutable.Map[ImmutableBytes, ContractDetails]) = {
		val (account, details) =
			this.cacheAccounts.get(address) match {
				case Some(accountState) =>
					val contractDetails = this.cacheDetails.get(address).get
					(accountState, contractDetails)
				case _ =>
					this.repository.loadAccount(address, this.cacheAccounts, this.cacheDetails)
					(this.cacheAccounts.get(address).get, this.cacheDetails.get(address).get)
			}
		this.cacheAccounts.put(address, account.createClone)
		this.cacheDetails.put(address, new ContractDetailsCacheImpl(details))
	}

	override def delete(address: ImmutableBytes) = {
		if (logger.isTraceEnabled) {
			logger.trace("<RepositoryTrack> Delete account: [%s]".format(address.toHexString))
		}
		getAccountState(address).foreach(_.isDeleted = true)
		getContractDetails(address).foreach(_.isDeleted = true)
	}

	override def increaseNonce(address: ImmutableBytes) = {
		val accountState = getAccountState(address).getOrElse(createAccount(address))
		getContractDetails(address).foreach(_.isDirty = true)

		val prevNonce = accountState.nonce
		accountState.incrementNonce()
		if (logger.isTraceEnabled) {
			logger.trace("<RepositoryTrack> Increased nonce: [%s] from [%s] to [%s]".format(address, prevNonce, accountState.nonce))
		}
		accountState.nonce
	}

	def setNonce(address: ImmutableBytes, v: BigInt): BigInt = {
		val accountState = getAccountState(address).getOrElse(createAccount(address))
		getContractDetails(address).foreach(_.isDirty = true)

		val prevNonce = accountState.nonce
		accountState.nonce = v
		if (logger.isTraceEnabled) {
			logger.trace("<RepositoryTrack> Set nonce: [%s] from [%s] to [%s]".format(address, prevNonce, accountState.nonce))
		}
		accountState.nonce
	}

	override def getNonce(address: ImmutableBytes) = getAccountState(address).map(_.nonce).getOrElse(UtilConsts.Zero)

	override def getBalance(address: ImmutableBytes) = getAccountState(address).map(_.balance)

	override def addBalance(address: ImmutableBytes, value: BigInt) = {
		val account = getAccountState(address).getOrElse(createAccount(address))
		getContractDetails(address).foreach(_.isDirty = true)
		val newBalance = account.addToBalance(value)
		if (logger.isTraceEnabled) {
			logger.trace("<RepositoryTrack> Added to balance: [%s] NewBalance: [%s], Delta: [%s]".format(address, newBalance, value))
		}
		newBalance
	}

	override def saveCode(address: ImmutableBytes, code: ImmutableBytes) = {
		if (logger.isTraceEnabled) {
			logger.trace("<RepositoryTrack> Saving code. Address: [%s], Code: [%s]".format(address.toHexString, code.toHexString))
		}
		getContractDetails(address).foreach {
			each => {
				each.setCode(code)
				each.isDirty = true
			}
		}
		getAccountState(address).foreach(_.codeHash = code.sha3)
	}

	override def getCode(address: ImmutableBytes): Option[ImmutableBytes] = {
		if (!existsAccount(address)) {
			None
		} else if (getAccountState(address).get.codeHash == DigestUtils.EmptyDataHash) {
			Some(ImmutableBytes.empty)
		} else {
			Option(getContractDetails(address).get.getCode)
		}
	}

	override def addStorageRow(address: ImmutableBytes, key: DataWord, value: DataWord) = {
		if (logger.isTraceEnabled) {
			logger.trace("<RepositoryTrack> Add storage row. Address: [%s], Key: [%s], Value: [%s]".format(address.toHexString, key.toHexString, value.toHexString))
		}
		getContractDetails(address).foreach(_.put(key, value))
	}

	override def getStorageValue(address: ImmutableBytes, key: DataWord) = getContractDetails(address).map(_.get(key))

	override def getAccountKeys = throw new UnsupportedOperationException

	override def dumpState(block: Block, gasUsed: Long, txNumber: Int, txHash: ImmutableBytes) = throw new UnsupportedOperationException

	override def startTracking = new RepositoryTrack(this)

	override def flush() = throw new UnsupportedOperationException

	override def flushNoReconnect() = throw new UnsupportedOperationException

	override def commit() = {
		for (details <- this.cacheDetails.values) {
			details.asInstanceOf[ContractDetailsCacheImpl].commit()
		}
		this.repository.updateBatch(this.cacheAccounts, this.cacheDetails)
		this.cacheAccounts.clear()
		this.cacheDetails.clear()
		if (logger.isDebugEnabled) {
			logger.debug("<RepositoryTrack> Committed changes.")
		}
	}

	override def syncToRoot(root: ImmutableBytes) = throw new UnsupportedOperationException

	override def rollback() = {
		this.cacheAccounts.clear()
		this.cacheDetails.clear()

		if (logger.isDebugEnabled) {
			logger.debug("<RepositoryTrack> Rolled back changes.")
		}
	}

	override def updateBatch(accountStates: mutable.Map[ImmutableBytes, AccountState], contractDetails: mutable.Map[ImmutableBytes, ContractDetails]) = {
		for (each <- accountStates) {
			this.cacheAccounts.put(each._1, each._2)
		}
		for (each <- contractDetails) {
			val hash = each._1
			val contractDetailsCache = each._2.asInstanceOf[ContractDetailsCacheImpl]
			if (Option(contractDetailsCache.originalContract).exists(original => original.isInstanceOf[ContractDetailsImpl])) {
				this.cacheDetails.put(hash, contractDetailsCache.originalContract)
			} else {
				this.cacheDetails.put(hash, contractDetailsCache)
			}
		}
	}


	override def getStorage(address: ImmutableBytes, keys: Iterable[DataWord]): Map[DataWord, DataWord] = {
		getContractDetails(address).map(_.getStorage(keys)).getOrElse(Map.empty)
	}

	override def getRoot = throw new UnsupportedOperationException

	override def getSnapshotTo(root: ImmutableBytes) = throw new UnsupportedOperationException

	override def close() = throw new UnsupportedOperationException

	override def isClosed = throw new UnsupportedOperationException

	override def reset() = throw new UnsupportedOperationException

	def getOriginalRepository: Repository = {
		this.repository match {
			case r: RepositoryTrack => r.getOriginalRepository
			case r: Repository => r
		}
	}

}

object RepositoryTrack {
	private val logger = LoggerFactory.getLogger("repository")
}