package mips64;

public class ProgramCounter {

  PipelineSimulator simulator;
  int nextPC;
  int pc;

  public ProgramCounter(PipelineSimulator sim) {
    pc = 0;
    simulator = sim;
  }

  public int getPC () {
    return pc;
  }

  public void setPC (int newPC) {
    pc = newPC;
  }

  public void incrPC () {
    pc += 4;
  }

  public void update() 
  {
    if(!simulator.stall)
    {
      pc = nextPC;
      nextPC += 4;
    }
  }
}
