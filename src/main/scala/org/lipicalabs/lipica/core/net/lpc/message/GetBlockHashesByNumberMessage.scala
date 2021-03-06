package org.lipicalabs.lipica.core.net.lpc.message

import org.lipicalabs.lipica.core.net.lpc.LpcMessageCode
import org.lipicalabs.lipica.core.net.message.Message
import org.lipicalabs.lipica.core.bytes_codec.RBACCodec
import org.lipicalabs.lipica.core.utils.ImmutableBytes

/**
 * 指定されたブロック番号に始まる、指定した個数のブロックハッシュ値の提供を
 * 相手ノードに要求するためのメッセージです。
 * 返信として BlockHashes メッセージを期待します。
 *
 * Created by IntelliJ IDEA.
 * 2015/12/09 20:46
 * YANAGISAWA, Kentaro
 */
case class GetBlockHashesByNumberMessage(blockNumber: Long, maxBlocks: Int) extends LpcMessage {

	override def toEncodedBytes = {
		val encodedBlockNumber = RBACCodec.Encoder.encode(BigInt(this.blockNumber))
		val encodedMaxBlocks = RBACCodec.Encoder.encode(this.maxBlocks)
		RBACCodec.Encoder.encodeSeqOfByteArrays(Seq(encodedBlockNumber, encodedMaxBlocks))
	}

	override def code = LpcMessageCode.GetBlockHashesByNumber.asByte

	override def answerMessage: Option[Class[_ <: Message]] = Option(classOf[BlockHashesMessage])

}

object GetBlockHashesByNumberMessage {

	def decode(encodedBytes: ImmutableBytes): GetBlockHashesByNumberMessage = {
		val items = RBACCodec.Decoder.decode(encodedBytes).right.get.items
		val blockNumber = items.head.asPositiveLong
		val maxBlocks = items(1).asInt
		new GetBlockHashesByNumberMessage(blockNumber, maxBlocks)
	}
}
