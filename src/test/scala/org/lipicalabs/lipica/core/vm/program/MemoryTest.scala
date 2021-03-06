package org.lipicalabs.lipica.core.vm.program

import org.junit.runner.RunWith
import org.lipicalabs.lipica.core.utils.ImmutableBytes
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
 * Created by IntelliJ IDEA.
 * 2015/09/08 13:01
 * YANAGISAWA, Kentaro
 */

@RunWith(classOf[JUnitRunner])
class MemoryTest extends Specification {
	sequential


	"test extend" should {
		"be right" in {
			checkMemoryExtend(0)
			checkMemoryExtend(1)
			checkMemoryExtend(Memory.WORD_SIZE - 1)
			checkMemoryExtend(Memory.WORD_SIZE)
			checkMemoryExtend(Memory.WORD_SIZE + 1)
			checkMemoryExtend(Memory.WORD_SIZE * 2)
			checkMemoryExtend(Memory.CHUNK_SIZE - 1)
			checkMemoryExtend(Memory.CHUNK_SIZE)
			checkMemoryExtend(Memory.CHUNK_SIZE + 1)
			checkMemoryExtend(2000)
			ok
		}
	}

	"save to memory (1)" should {
		"be right" in {
			val memory = new Memory
			val data = ImmutableBytes(Array[Byte](1, 1, 1, 1))
			memory.write(0, data, data.length, limited = false)

			memory.chunksAsSeq.size mustEqual 1

			val chunk = memory.chunksAsSeq.head
			chunk(0) mustEqual 1
			chunk(1) mustEqual 1
			chunk(2) mustEqual 1
			chunk(3) mustEqual 1
			chunk(4) mustEqual 0

			memory.size mustEqual Memory.WORD_SIZE
			memory.toString.nonEmpty mustEqual true
		}
	}

	"read from memory (1)" should {
		"be right" in {
			val memory = new Memory
			memory.read(0, 0) mustEqual ImmutableBytes.empty
		}
	}

	"write and read" should {
		"be right" in {
			val memory = new Memory

			(0 until 10000).foreach {i => {
				val data = ImmutableBytes(Array[Byte]((i % 256).toByte))
				if ((i % 3) == 0) {
					memory.write(i, data, data.length, limited = false)
				} else if ((i % 3) == 1) {
					memory.write(i, data, data.length + 1, limited = false)
				} else {
					memory.extendAndWrite(i, i + data.length, data)
				}

			}}
			(0 until 10000).foreach {i => {
				val data = memory.read(i, 1)
				val oneByte = memory.readByte(i)
				data.length mustEqual 1
				data(0) mustEqual oneByte
				(data(0) & 0xFF) mustEqual (i % 256)
			}}
			ok
		}
	}


	private def checkMemoryExtend(dataSize: Int) {
		val memory = new Memory
		memory.extend(0, dataSize)
		calcSize(dataSize, Memory.CHUNK_SIZE) mustEqual memory.internalSize
		calcSize(dataSize, Memory.WORD_SIZE) mustEqual memory.size
	}

	private def calcSize(dataSize: Int, chunkSize: Int): Int = {
		java.lang.Math.ceil(dataSize.toDouble / chunkSize).toInt * chunkSize
	}

}
