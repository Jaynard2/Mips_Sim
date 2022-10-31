package mips64;

import mips64.Registers.FORWARD_STAGES;

public class MemWbStage {

    PipelineSimulator simulator;
    boolean halted;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int loadIntData;
    int destReg;

    final int haltCode = Instruction.getOpcodeFromName("HALT");
    final int loadCode = Instruction.getOpcodeFromName("LW");
    final int storeCode = Instruction.getOpcodeFromName("SW");

    public MemWbStage(PipelineSimulator sim) {
        simulator = sim;
        opcode = Instruction.getOpcodeFromName("NOP");
        halted = false;
    }

    public boolean isHalted() {
        return halted;
    }

    public void update() {
        if(opcode == haltCode)
        {
            halted = true;
            return;
        }

        // if(!shouldWriteback)
        // {
        //     return;
        // }

        Registers regs = simulator.regs;
        regs.setConcerned(FORWARD_STAGES.NONE);
        regs.setMemwbCur(destReg);
        if(opcode == loadCode)
        {
            loadIntData =  simulator.getMemory().getIntDataAtAddr(aluIntData);
            regs.writeReg(destReg, loadIntData);
        }
        else if(opcode == storeCode)
        {
            MemoryModel mem = simulator.getMemory();
            mem.setIntDataAtAddr(aluIntData, regs.readReg(destReg));
            regs.setMemwbCur(-1);
        }
        else if(destReg != -1)
        {
            regs.writeReg(destReg, aluIntData);
        }

        ExMemStage prevStage = simulator.getExMemStage();
        shouldWriteback = prevStage.shouldWriteback;
        instPC = prevStage.instPC;
        opcode = prevStage.opcode;
        aluIntData = prevStage.aluIntData;
        destReg = prevStage.destReg;

        int regA = prevStage.regA;
        int regB = prevStage.regB;
        if(regA == destReg || regB == destReg)
        {
            prevStage.storeIntData = aluIntData;
        }

        IdExStage idexStage = simulator.getIdExStage();
        regA = idexStage.regA;
        regB = idexStage.regB;
        if(regA == destReg || regB == destReg)
        {
            idexStage.storeIntData = aluIntData;
        }
    }
}
