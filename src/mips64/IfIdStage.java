package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode;


  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;

  }

  public void update() 
  {
    instPC = simulator.getPCStage().getPC();
    Instruction inst = simulator.getMemory().getInstAtAddr(instPC);
    opcode = inst.getOpcode();
  }
}
