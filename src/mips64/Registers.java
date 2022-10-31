package mips64;

public class Registers 
{
    public enum FORWARD_STAGES { NONE, MEMWB, ALL };
    private int registers[];
    private int exmemCurReg;
    private int memwbCurReg;
    private PipelineSimulator sim;
    private FORWARD_STAGES concernedStage;

    public Registers()
    {
        registers = new int[32];
    }

    public void setExmemCur(int reg)
    {
        exmemCurReg = reg;
    }

    public void setMemwbCur(int reg)
    {
        memwbCurReg = reg;
    }

    public void setConcerned(FORWARD_STAGES stage)
    {
        concernedStage = stage;
    }

    public int readReg(int regNum)
    {
        if(regNum == exmemCurReg && concernedStage == FORWARD_STAGES.ALL)
            regNum = sim.getExMemStage().destReg;
        if(regNum == memwbCurReg && 
            (concernedStage == FORWARD_STAGES.ALL || concernedStage == FORWARD_STAGES.MEMWB))
            regNum = sim.getMemWbStage().destReg;

        return registers[regNum];
    }

    public void writeReg(int reg, int value)
    {
        registers[reg] = value;
    }
}
