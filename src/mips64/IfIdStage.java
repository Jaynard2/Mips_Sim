package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode;
  Instruction inst;


  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;
    inst = Instruction.getInstructionFromName("NOP");
    opcode = inst.getOpcode();
  }

  public void update() 
  {
    if(!simulator.stall)
    {
      instPC = simulator.getPCStage().getPC();
      inst = simulator.getMemory().getInstAtAddr(instPC);
      opcode = inst.getOpcode();
    }
  }
}
