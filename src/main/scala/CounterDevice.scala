import chisel3._

//- start counter_device
class CounterDevice extends Module {
  val io = IO(new Bundle() {
    val addr = Input(UInt(2.W))
    val wr = Input(Bool())
    val rd = Input(Bool())
    val din = Input(UInt(32.W))
    val dout = Output(UInt(32.W))
    val ack = Output(Bool())
  })

  val ackReg = RegInit(false.B)
  val addrReg = RegInit(0.U(2.W))
  val cntRegs = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))

  ackReg := io.rd || io.wr
  when(io.rd) {
    addrReg := io.addr
  }
  io.dout := cntRegs(addrReg)

  for (i <- 0 until 3) {
    cntRegs(i) := cntRegs(i) + 1.U
  }
  when (io.wr) {
    cntRegs(io.addr) := io.din
  }

  io.ack := ackReg
}
//- end


object CounterDevice extends App {
  emitVerilog(new CounterDevice())
}