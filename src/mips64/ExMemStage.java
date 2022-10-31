package mips64;

public class ExMemStage {
    enum FORWADED {NONE, REG_A, REG_B};
    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int storeIntData;
    int destReg;
    FORWADED isForwarded;
    int regA;
    int regB;
    Instruction decodedInstruction;

    public ExMemStage(PipelineSimulator sim) {
        simulator = sim;
        opcode = Instruction.getOpcodeFromName("NOP");
        storeIntData = 0;
        isForwarded = FORWADED.NONE;
    }

    public void update() {
        if(!simulator.stall)
        {
            IdExStage prevStage  = simulator.idEx;
            instPC = prevStage.instPC;
            opcode = prevStage.opcode;
            shouldWriteback = prevStage.shouldWriteback;
            destReg = prevStage.destReg;
            simulator.regs.setExmemCur(destReg);
            regA = prevStage.regA;
            regB = prevStage.regB;

            int regA = isForwarded == FORWADED.REG_A ? storeIntData : prevStage.regAData;
            int regB = isForwarded == FORWADED.REG_B ? storeIntData : prevStage.regBData;
            int imm = prevStage.immediate;

            if(isForwarded != FORWADED.NONE)
            {
                storeIntData = 0;
                isForwarded = FORWADED.NONE;
            }

            switch(opcode)
            {
            case Instruction.INST_ADD:
                aluIntData = regA + regB;
                break;
            case Instruction.INST_LW:
            case Instruction.INST_SW:
                simulator.regs.setExmemCur(-1);
            case Instruction.INST_ADDI:
                aluIntData = regA + imm;
                break;
            case Instruction.INST_SUB:
                aluIntData = regA - regB;
                break;
            case Instruction.INST_DIV:
                aluIntData = regA / regB;
                break;
            case Instruction.INST_MUL:
                aluIntData = regA * regB;
                break;
            case Instruction.INST_AND:
                aluIntData = regA & regB;
                break;
            case Instruction.INST_ANDI:
                aluIntData = regA & imm;
                break;
            case Instruction.INST_OR:
                aluIntData = regA | regB;
                break;
            case Instruction.INST_ORI:
                aluIntData = regA | imm;
                break;
            case Instruction.INST_XOR:
                aluIntData = regA ^ regB;
                break;
            case Instruction.INST_XORI:
                aluIntData = regA ^ imm;
                break;
            case Instruction.INST_SLL:
            {
                int shiftAmount = prevStage.shftAmount;
                aluIntData = regA << shiftAmount;
                break;
            }
            case Instruction.INST_SRL:
            {
                int shiftAmount = prevStage.shftAmount;
                aluIntData = regA >> shiftAmount;
                break;
            }
            case Instruction.INST_SRA:
            {
                int shiftAmount = prevStage.shftAmount;
                aluIntData = regA >>> shiftAmount;
                break;
            }
            case Instruction.INST_BEQ:
            case Instruction.INST_BNE:
            case Instruction.INST_BLTZ:
            case Instruction.INST_BLEZ:
            case Instruction.INST_BGTZ:
            case Instruction.INST_BGEZ:
            case Instruction.INST_J:
            case Instruction.INST_JR:
            case Instruction.INST_JAL:
            case Instruction.INST_JALR:
            }
        }
        
    }
}
