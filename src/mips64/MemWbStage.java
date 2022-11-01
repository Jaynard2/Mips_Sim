package mips64;

import mips64.ExMemStage.FORWADED;
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

        Registers regs = simulator.regs;
        regs.setConcerned(FORWARD_STAGES.NONE);
        ExMemStage prevStage = simulator.getExMemStage();
        int regA = simulator.idEx.regA;
        int regB = simulator.idEx.regB;

        if(shouldWriteback)
        {
            if (opcode == loadCode)
            {
                if(simulator.stall)
                {
                    simulator.regs.writeReg(destReg, loadIntData);
                    if(regA == destReg)
                    {
                        prevStage.isForwarded = FORWADED.REG_A;
                        prevStage.storeIntData = loadIntData;
                    }
                    if(regB == destReg)
                    {
                        prevStage.isForwarded = prevStage.isForwarded == FORWADED.NONE ? FORWADED.REG_B : FORWADED.ALL;
                        prevStage.storeIntData = loadIntData;
                    }
                }
            }
            else
            {
                if(opcode == storeCode)
                {
                    MemoryModel mem = simulator.getMemory();
                    mem.setIntDataAtAddr(aluIntData, regs.readReg(destReg));
                    regs.setMemwbCur(-1);
                }
                else if(destReg != -1)
                {
                    regs.writeReg(destReg, aluIntData);
                }
            }
        }
        loadIntData = -1;

        shouldWriteback = prevStage.shouldWriteback;
        instPC = prevStage.instPC;
        opcode = prevStage.opcode;
        aluIntData = prevStage.aluIntData;
        destReg = prevStage.destReg;
        regs.setMemwbCur(destReg);

        if(opcode == loadCode && !simulator.stall)
        {
            simulator.stall = true;
            loadIntData = simulator.memory.getIntDataAtAddr(aluIntData);
        }
        else
        {
            simulator.stall = false;
        }
    }
}
