package mips64;

import mips64.Registers.FORWARD_STAGES;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regA;
    int regB;
    int regAData;
    int regBData;
    int immediate;
    int destReg;
    int storeIntData;
    //int inst;
    Instruction inst;
    boolean forRegA;

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
        opcode = Instruction.getOpcodeFromName("NOP");
        inst = Instruction.getInstructionFromName("NOP");
        storeIntData = 0;
    }

    Integer getIntRegister(int regNum) {
        Registers regs = simulator.regs;
        regs.setConcerned(FORWARD_STAGES.NONE);
        return regs.readReg(regNum);
    }

    public void update() {
        if(!simulator.stall)
        {
            instPC = simulator.getIfIdStage().instPC;
            opcode = simulator.getIfIdStage().opcode;
            inst = simulator.getIfIdStage().inst;
            destReg = -1;

            Registers regs = simulator.regs;
            regs.setConcerned(FORWARD_STAGES.ALL);

            if (inst instanceof ITypeInst){
                regAData = regs.readReg(((ITypeInst)inst).getRS());
                destReg = ((ITypeInst)inst).getRT();
                immediate = ((ITypeInst)inst).getImmed();
            }
            else if (inst instanceof RTypeInst){
                regAData = regs.readReg(((RTypeInst)inst).getRS());
                regBData = regs.readReg(((RTypeInst)inst).getRT());
                destReg = ((RTypeInst)inst).getRD();
                //immediate = 0;
            }
            else if (inst instanceof JTypeInst){
                //regAData = 0;
                //regBData = 0;
                immediate = ((JTypeInst)inst).getOffset();
            }
        }
    }
}
