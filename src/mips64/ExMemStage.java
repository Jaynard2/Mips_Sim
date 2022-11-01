package mips64;

public class ExMemStage {
    enum FORWADED {NONE, REG_A, REG_B, ALL};
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
            aluIntData = 0;

            if(!shouldWriteback)
                return;

            int regA = prevStage.regAData;
            int regB = prevStage.regBData;
            if(isForwarded == FORWADED.REG_A || isForwarded == FORWADED.ALL)
                regA = storeIntData;
            if(isForwarded == FORWADED.REG_B || isForwarded == FORWADED.ALL)
                regB = storeIntData;

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
                destReg = -1;
                if(regA == regB)
                {
                    simulator.pc.pc += imm;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BNE:
                destReg = -1;
                if(regA != regB)
                {
                    simulator.pc.pc += imm;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BLTZ:
                destReg = -1;
                if(regA < regB)
                {
                    simulator.pc.pc += imm;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BLEZ:
                destReg = -1;
                if(regA <= regB)
                {
                    simulator.pc.pc += imm;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BGTZ:
                destReg = -1;
                if(regA > regB)
                {
                    simulator.pc.pc += imm;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BGEZ:
                destReg = -1;
                if(regA >= regB)
                {
                    simulator.pc.pc += imm;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_JR:
            case Instruction.INST_JALR:
            case Instruction.INST_JAL:
                aluIntData = imm;
                break;
            default:
                break;
            }
        }
        
    }
}
