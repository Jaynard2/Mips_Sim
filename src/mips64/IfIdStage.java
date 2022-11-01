package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode;
  Instruction inst;
  boolean shouldWriteback;
  boolean nextWriteBack;


  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;
    inst = Instruction.getInstructionFromName("NOP");
    opcode = inst.getOpcode();
  }

  public void update() 
  {
    if(!simulator.stall)
    {
      shouldWriteback = nextWriteBack;
      instPC = simulator.getPCStage().getPC();
      inst = simulator.getMemory().getInstAtAddr(instPC);
      opcode = inst.getOpcode();
      nextWriteBack = true;
    }
  }
}
