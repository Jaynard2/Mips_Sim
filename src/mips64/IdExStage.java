package mips64;

import mips64.Registers.FORWARD_STAGES;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int regA;
    int regB;
    int immediate;
    int destReg;
    int shftAmount;
    Instruction inst;

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
        opcode = Instruction.getOpcodeFromName("NOP");
        inst = Instruction.getInstructionFromName("NOP");
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
            regA = -1;
            regB = -1;

            Registers regs = simulator.regs;
            regs.setConcerned(FORWARD_STAGES.ALL);

            if (inst instanceof ITypeInst){
                regA = ((ITypeInst)inst).getRS();
                regAData = regs.readReg(regA);
                destReg = ((ITypeInst)inst).getRT();
                immediate = ((ITypeInst)inst).getImmed();
            }
            else if (inst instanceof RTypeInst){
                regA = ((RTypeInst)inst).getRS();
                regB = ((RTypeInst)inst).getRT();
                regAData = regs.readReg(regA);
                regBData = regs.readReg(regB);
                destReg = ((RTypeInst)inst).getRD();
                shftAmount = ((RTypeInst)inst).getShamt();
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
