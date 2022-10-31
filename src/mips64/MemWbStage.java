package mips64;

public class MemWbStage {

    PipelineSimulator simulator;
    boolean halted;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int loadIntData;
    int destReg;
    int regs[];

    final int haltCode = Instruction.getOpcodeFromName("HALT");
    final int loadCode = Instruction.getOpcodeFromName("LW");
    final int storeCode = Instruction.getOpcodeFromName("SW");

    public MemWbStage(PipelineSimulator sim) {
        simulator = sim;
        opcode = Instruction.getOpcodeFromName("NOP");
        regs = simulator.getIdExStage().registers;
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

        if(opcode == loadCode)
        {
            loadIntData =  simulator.getMemory().getIntDataAtAddr(aluIntData);
            regs[destReg] = loadIntData;
        }
        else if(opcode == storeCode)
        {
            MemoryModel mem = simulator.getMemory();
            mem.setIntDataAtAddr(aluIntData, regs[destReg]);
        }
        else if(destReg != -1)
        {
            regs[destReg] = aluIntData;
        }

        ExMemStage prevStage = simulator.getExMemStage();
        shouldWriteback = prevStage.shouldWriteback;
        instPC = prevStage.instPC;
        opcode = prevStage.opcode;
        aluIntData = prevStage.aluIntData;
        destReg = prevStage.destReg;
        loadIntData = -1;

    }
}
