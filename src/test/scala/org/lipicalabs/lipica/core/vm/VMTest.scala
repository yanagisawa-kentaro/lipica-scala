package org.lipicalabs.lipica.core.vm

import org.junit.runner.RunWith
import org.lipicalabs.lipica.core.kernel
import org.lipicalabs.lipica.core.utils.ImmutableBytes
import org.lipicalabs.lipica.core.vm.OpCode.Address
import org.lipicalabs.lipica.core.vm.program.Program
import org.lipicalabs.lipica.core.vm.program.Program.BadJumpDestinationException
import org.lipicalabs.lipica.core.vm.program.context.ProgramContextMockImpl
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.specification.BeforeExample

/**
 * Created by IntelliJ IDEA.
 * 2015/09/08 13:01
 * YANAGISAWA, Kentaro
 */

@RunWith(classOf[JUnitRunner])
class VMTest extends Specification with BeforeExample {
	sequential

	private var context = new ProgramContextMockImpl(null)

	override def before: scala.Any = {
		this.context = new ProgramContextMockImpl(null)
	}

	"test tiers" should {
		"be right" in {
			Tier.all.foreach {
				each => {
					each.level mustEqual each.asInt
					each.toString.nonEmpty mustEqual true
				}
			}
			ok
		}
	}

	"test push1" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60A0"), context, null)
			val expected = "00000000000000000000000000000000000000000000000000000000000000A0"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push2" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61A0B0"), context, null)
			val expected = "000000000000000000000000000000000000000000000000000000000000A0B0"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push3" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("62A0B0C0"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000A0B0C0"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push4" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("63A0B0C0D0"), context, null)
			val expected = "00000000000000000000000000000000000000000000000000000000A0B0C0D0"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push5" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("64A0B0C0D0E0"), context, null)
			val expected = "000000000000000000000000000000000000000000000000000000A0B0C0D0E0"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push6" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("65A0B0C0D0E0F0"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000A0B0C0D0E0F0"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push7" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("66A0B0C0D0E0F0A1"), context, null)
			val expected = "00000000000000000000000000000000000000000000000000A0B0C0D0E0F0A1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push8" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("67A0B0C0D0E0F0A1B1"), context, null)
			val expected = "000000000000000000000000000000000000000000000000A0B0C0D0E0F0A1B1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push9" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("68A0B0C0D0E0F0A1B1C1"), context, null)
			val expected = "0000000000000000000000000000000000000000000000A0B0C0D0E0F0A1B1C1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push10" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("69A0B0C0D0E0F0A1B1C1D1"), context, null)
			val expected = "00000000000000000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push11" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6AA0B0C0D0E0F0A1B1C1D1E1"), context, null)
			val expected = "000000000000000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push12" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6BA0B0C0D0E0F0A1B1C1D1E1F1"), context, null)
			val expected = "0000000000000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push13" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6CA0B0C0D0E0F0A1B1C1D1E1F1A2"), context, null)
			val expected = "00000000000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push14" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6DA0B0C0D0E0F0A1B1C1D1E1F1A2B2"), context, null)
			val expected = "000000000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push15" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6EA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2"), context, null)
			val expected = "0000000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push16" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6FA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2"), context, null)
			val expected = "00000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push17" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("70A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2"), context, null)
			val expected = "000000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push18" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("71A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2"), context, null)
			val expected = "0000000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push19" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("72A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3"), context, null)
			val expected = "00000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push20" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("73A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3"), context, null)
			val expected = "000000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push21" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("74A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3"), context, null)
			val expected = "0000000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push22" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("75A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3"), context, null)
			val expected = "00000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push23" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("76A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3"), context, null)
			val expected = "000000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push24" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("77A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3"), context, null)
			val expected = "0000000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push25" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("78A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4"), context, null)
			val expected = "00000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push26" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("79A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4"), context, null)
			val expected = "000000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push27" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("7AA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4"), context, null)
			val expected = "0000000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push28" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("7BA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4"), context, null)
			val expected = "00000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push29" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("7CA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4"), context, null)
			val expected = "000000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push30" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("7DA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4F4"), context, null)
			val expected = "0000A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4F4"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push31" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("7EA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4F4A1"), context, null)
			val expected = "00A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4F4A1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push32" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("7FA0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4F4A1B1"), context, null)
			val expected = "A0B0C0D0E0F0A1B1C1D1E1F1A2B2C2D2E2F2A3B3C3D3E3F3A4B4C4D4E4F4A1B1"
			vm.step(program)
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test push not enough data (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61AA"), context, null)
			val expected = "000000000000000000000000000000000000000000000000000000000000AA00"
			vm.step(program)
			program.isStopped mustEqual true
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}
	"test push not enough data (2)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("7fAABB"), context, null)
			val expected = "AABB000000000000000000000000000000000000000000000000000000000000"
			vm.step(program)
			program.isStopped mustEqual true
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test and (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("600A600A16"), context, null)
			val expected = "000000000000000000000000000000000000000000000000000000000000000A"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test or (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60F0600F17"), context, null)
			val expected = "00000000000000000000000000000000000000000000000000000000000000FF"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test xor (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60FF60FF18"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000000"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test byte (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("65AABBCCDDEEFF601E1A"), context, null)
			val expected = "00000000000000000000000000000000000000000000000000000000000000EE"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test is zero (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("600015"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 2).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test eq (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("602A602A14"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test gt (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6001600211"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test sgt (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6001600213"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test lt (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6001600210"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000000"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test slt (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6001600212"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000000"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test bnot (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("600119"), context, null)
			val expected = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE"
			(0 until 2).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test pop (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61000060016200000250"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 4).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test dupN (1)" should {
		"be right" in {
			(1 to 16).foreach {
				testDupN
			}
			ok
		}
	}

	private def testDupN(n: Int): Unit = {
		val vm = new VM
		val operation = (OpCode.Dup1.opcode + n - 1).toByte
		val programCode = new StringBuilder
		(0 until n).foreach {
			i => {
				programCode.append("60" + (12 + i))
			}
		}
		val code = ImmutableBytes.parseHexString(programCode.toString()) :+ operation
		val program = new Program(code, context)
		val expected = "0000000000000000000000000000000000000000000000000000000000000012"
		val expectedLen = n + 1
		(0 until expectedLen).foreach {
			_ => vm.step(program)
		}

		program.stack.size mustEqual expectedLen
		program.stack.pop.data.toHexString.toUpperCase mustEqual expected
		(0 until (expectedLen - 2)).foreach {
			_ => program.stack.pop.data.toHexString.toUpperCase mustNotEqual expected
		}
		program.stack.pop.data.toHexString.toUpperCase mustEqual expected
	}

	"test swapN (1)" should {
		"be right" in {
			(1 to 16).foreach {
				testSwapN
			}
			ok
		}
	}

	private def testSwapN(n: Int): Unit = {
		val vm = new VM
		val operation = (OpCode.Swap1.opcode + n - 1).toByte

		val programCode = new StringBuilder
		val top = VMWord(0x10 + n).toString
		(n to 0 by -1).foreach {
			i => {
				programCode.append("60" + ImmutableBytes.fromOneByte((0x10 + i).toByte).toHexString)
			}
		}
		programCode.append(ImmutableBytes.fromOneByte(operation).toHexString)

		val code = ImmutableBytes.parseHexString(programCode.toString()) :+ operation

		val program = new Program(code, context)
		(0 until n + 2).foreach {
			_ => vm.step(program)
		}

		program.stack.size mustEqual n + 1
		program.stackPop.data.toHexString mustEqual top
	}

	"test log0" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61123460005260206000A0"), context)
			(0 until 6).foreach {
				_ => vm.step(program)
			}
			val logInfo = program.result.logsAsSeq.head
			logInfo.address.toHexString mustEqual "cd2a3d9f938e13cd947ec05abc7fe734df8dd826"
			logInfo.topics.isEmpty mustEqual true
			logInfo.data.toHexString mustEqual "0000000000000000000000000000000000000000000000000000000000001234"
		}
	}

	"test log1" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61123460005261999960206000A1"), context)
			(0 until 7).foreach {
				_ => vm.step(program)
			}
			val logInfo = program.result.logsAsSeq.head
			logInfo.address.toHexString mustEqual "cd2a3d9f938e13cd947ec05abc7fe734df8dd826"
			logInfo.topics.size mustEqual 1
			logInfo.data.toHexString mustEqual "0000000000000000000000000000000000000000000000000000000000001234"
		}
	}


	"test log2" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61123460005261999961666660206000A2"), context)
			(0 until 8).foreach {
				_ => vm.step(program)
			}
			val logInfo = program.result.logsAsSeq.head
			logInfo.address.toHexString mustEqual "cd2a3d9f938e13cd947ec05abc7fe734df8dd826"
			logInfo.topics.size mustEqual 2
			logInfo.data.toHexString mustEqual "0000000000000000000000000000000000000000000000000000000000001234"
		}
	}

	"test log3" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61123460005261999961666661333360206000A3"), context)
			(0 until 9).foreach {
				_ => vm.step(program)
			}
			val logInfo = program.result.logsAsSeq.head
			logInfo.address.toHexString mustEqual "cd2a3d9f938e13cd947ec05abc7fe734df8dd826"
			logInfo.topics.size mustEqual 3
			logInfo.data.toHexString mustEqual "0000000000000000000000000000000000000000000000000000000000001234"
		}
	}

	"test log4" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61123460005261999961666661333361555560206000A4"), context)
			(0 until 10).foreach {
				_ => vm.step(program)
			}
			val logInfo = program.result.logsAsSeq.head
			logInfo.address.toHexString mustEqual "cd2a3d9f938e13cd947ec05abc7fe734df8dd826"
			logInfo.topics.size mustEqual 4
			logInfo.data.toHexString mustEqual "0000000000000000000000000000000000000000000000000000000000001234"
		}
	}

	"test mstore (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("611234600052"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000001234"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.getMemoryContent.toHexString.toUpperCase mustEqual expected
		}
	}

	"test mload (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("600051"), context)
			val m_expected = "0000000000000000000000000000000000000000000000000000000000000000"
			val s_expected = "0000000000000000000000000000000000000000000000000000000000000000"

			(0 until 2).foreach {
				_ => vm.step(program)
			}
			program.getMemoryContent.toHexString.toUpperCase mustEqual m_expected
			program.stack.peek.data.toHexString.toUpperCase mustEqual s_expected
		}
	}

	"test mstore8 (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6011600053"), context)
			val expected = "1100000000000000000000000000000000000000000000000000000000000000"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.getMemoryContent.toHexString.toUpperCase mustEqual expected
		}
	}

	"test sstore (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("602260AA55"), context)
			val expectedKey = "00000000000000000000000000000000000000000000000000000000000000AA"
			val expectedValue = "0000000000000000000000000000000000000000000000000000000000000022"

			(0 until 3).foreach {
				_ => vm.step(program)
			}
			val value = program.storage.getStorageValue(kernel.Address(context.ownerAddress.getDataWithoutLeadingZeros), VMWord(expectedKey)).get
			value.data.toHexString mustEqual expectedValue
		}
	}

	"test sload (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60AA54"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000000000"

			(0 until 2).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test sload (2)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("602260AA5560AA54"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000000022"

			(0 until 5).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test pc (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("58"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000000000"
			(0 until 1).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test jump (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60AA60BB600E5660CC60DD60EE5B60FF"), context)
			val expected = "00000000000000000000000000000000000000000000000000000000000000FF"

			try {
				(0 until 5).foreach {
					_ => vm.step(program)
				}
				program.stack.peek.data.toHexString.toUpperCase mustEqual expected
				ko
			} catch {
				case e: BadJumpDestinationException => ok
			}
		}
	}

	"test jumpi (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60016005575B60CC"), context)
			val expected = "00000000000000000000000000000000000000000000000000000000000000CC"
			(0 until 5).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test jumpdest (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("602360085660015b600255"), context)
			val s_expected_key = "0000000000000000000000000000000000000000000000000000000000000002"
			val s_expected_val = "0000000000000000000000000000000000000000000000000000000000000023"
			try {
				(0 until 5).foreach {
					_ => vm.step(program)
				}

				program.isStopped mustEqual true
				val key = VMWord(ImmutableBytes.parseHexString(s_expected_key))
				val value = program.storage.getStorageValue(kernel.Address(program.getOwnerAddress.data), key).get
				value.data.toHexString mustEqual s_expected_val
				ko
			} catch {
				case e: BadJumpDestinationException => ok
			}
		}
	}

	"test add (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6002600201"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000000004"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test add mod (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60026002600308"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 4).foreach {
				_ => vm.step(program)
			}
			program.isStopped mustEqual true
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test add mod (2)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6110006002611002086000"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000004"
			(0 until 4).foreach {
				_ => vm.step(program)
			}
			program.isStopped mustEqual false
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test mul (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6003600202"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000006"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test mul mod (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60036002600409"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000002"
			(0 until 4).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test div (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6002600404"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000002"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test sdiv (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6103E87FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC1805"), context, null)
			val expected = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test sub (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6004600603"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000002"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test msize (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("59"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000000"
			(0 until 1).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
		}
	}

	"test stop" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60206030601060306011602300"), context, null)
			val expectedSteps = 7
			var count = 0
			while (!program.isStopped) {
				vm.step(program)
				count += 1
			}
			count mustEqual expectedSteps
		}
	}

	"test exp (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("600360020a"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000000008"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stack.peek.data.toHexString.toUpperCase mustEqual expected
			program.result.manaUsed mustEqual 26
		}
	}

	"test return (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("61123460005260206000F3"), context, null)
			val expected = "0000000000000000000000000000000000000000000000000000000000001234"
			(0 until 6).foreach {
				_ => vm.step(program)
			}
			program.isStopped mustEqual true
			program.result.hReturn.toHexString mustEqual expected
		}
	}

	"test codecopy (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60036007600039123456"), context, null)
			val expected = "1234560000000000000000000000000000000000000000000000000000000000"
			(0 until 4).foreach {
				_ => vm.step(program)
			}
			val mana = program.result.manaUsed
			program.getMemoryContent.toHexString mustEqual expected
			mana mustEqual 18
		}
	}

	"test codesize (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("385E60076000396000605f556014600054601e60205463abcddcba6040545b51602001600a5254516040016014525451606001601e5254516080016028525460a052546016604860003960166000f26000603f556103e75660005460005360200235"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000000062"
			(0 until 1).foreach {
				_ => vm.step(program)
			}
			program.stackPop.toHexString.toUpperCase mustEqual expected
		}
	}

	"test extcodecopy (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("60036007600073471FD3AD3E9EEADEEC4608B92D16CE6B500704CC3C123456"), context)
			val expected = "6000600000000000000000000000000000000000000000000000000000000000"
			(0 until 5).foreach {
				_ => vm.step(program)
			}
			program.getMemoryContent.toHexString mustEqual expected
		}
	}

	"test mod (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6003600406"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stackPop.data.toHexString mustEqual expected
		}
	}

	"test smod (1)" should {
		"be right" in {
			val vm = new VM
			val program = new Program(ImmutableBytes.parseHexString("6003600407"), context)
			val expected = "0000000000000000000000000000000000000000000000000000000000000001"
			(0 until 3).foreach {
				_ => vm.step(program)
			}
			program.stackPop.data.toHexString mustEqual expected
		}
	}

}
