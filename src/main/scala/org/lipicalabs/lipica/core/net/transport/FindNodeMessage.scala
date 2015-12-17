package org.lipicalabs.lipica.core.net.transport

import org.lipicalabs.lipica.core.crypto.ECKey
import org.lipicalabs.lipica.core.utils.{ByteUtils, RBACCodec, ImmutableBytes}

/**
 * Created by IntelliJ IDEA.
 * 2015/12/15 19:19
 * YANAGISAWA, Kentaro
 */
class FindNodeMessage extends TransportMessage {

	private var _target: ImmutableBytes = null
	def target: ImmutableBytes = this._target

	private var _expires: Long = 0L
	def expires: Long = this._expires

	override def parse(data: Array[Byte]): Unit = {
		val items = RBACCodec.Decoder.decode(data).right.get.items
		this._target = items.head.bytes
		this._expires = items(1).asPositiveLong
	}

	override def toString: String = {
		"[FindNodeMessage] target=%s, expires in %,d seconds. %s".format(
			this.target, this.expires - (System.currentTimeMillis / 1000L), super.toString
		)
	}
}

object FindNodeMessage {

	def create(target: ImmutableBytes, privateKey: ECKey): FindNodeMessage = {
		val expiration = 60 + System.currentTimeMillis / 1000L

		val encodedTarget = RBACCodec.Encoder.encode(target)
		val encodedExpiration = RBACCodec.Encoder.encode(ByteUtils.toByteArrayWithNoLeadingZeros(expiration))

		val messageType = Array[Byte](3)
		val data = RBACCodec.Encoder.encodeSeqOfByteArrays(Seq(encodedTarget, encodedExpiration))

		val result: FindNodeMessage = TransportMessage.encode(messageType, data, privateKey)
		result._target = target
		result._expires = expiration
		result
	}

}