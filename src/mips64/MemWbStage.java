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
    final int jalCode = Instruction.getOpcodeFromName("JAL");

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
        if (opcode == loadCode)
        {
            loadIntData =  simulator.getMemory().getIntDataAtAddr(aluIntData);
            // Make reading from mem take 1 clk
            if(!simulator.stall)
            {
                regs.writeReg(destReg, loadIntData);
                loadIntData = -1;
            }
        }
        else
        {
            loadIntData = -1;
            if(opcode == storeCode)
            {
                MemoryModel mem = simulator.getMemory();
                mem.setIntDataAtAddr(aluIntData, regs.readReg(destReg));
                regs.setMemwbCur(-1);
            }
            else if(opcode == jalCode)
            {
                regs.writeReg(destReg, aluIntData);
            }
            else if(destReg != -1)
            {
                regs.writeReg(destReg, aluIntData);
            }
        }

        ExMemStage prevStage = simulator.getExMemStage();
        shouldWriteback = prevStage.shouldWriteback;
        instPC = prevStage.instPC;
        opcode = prevStage.opcode;
        aluIntData = prevStage.aluIntData;
        destReg = prevStage.destReg;
        // Need to look 2 behind bc of stall
        int regA = simulator.idEx.regA;
        int regB = simulator.idEx.regB;
        if(opcode == loadCode && !simulator.stall)
        {
            simulator.stall = true;
            if(regA == destReg)
            {
                prevStage.storeIntData = loadIntData;
                prevStage.isForwarded = FORWADED.REG_A;
            }
            else if(regB == destReg)
            {
                prevStage.storeIntData = loadIntData;
                prevStage.isForwarded = FORWADED.REG_B;
            }
        }
        else
        {
            simulator.stall = false;
        }
    }
}
