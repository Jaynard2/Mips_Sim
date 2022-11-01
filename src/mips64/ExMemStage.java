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
            //Set for next clock cycle
            prevStage.shouldWriteback = true;
            destReg = prevStage.destReg;
            simulator.regs.setExmemCur(destReg);
            regA = prevStage.regA;
            regB = prevStage.regB;
            aluIntData = 0;

            if(!shouldWriteback)
                return;

            int regAData = prevStage.regAData;
            int regBData = prevStage.regBData;
            if(isForwarded == FORWADED.REG_A || isForwarded == FORWADED.ALL)
                regAData = storeIntData;
            if(isForwarded == FORWADED.REG_B || isForwarded == FORWADED.ALL)
                regBData = storeIntData;

            int imm = prevStage.immediate;

            if(isForwarded != FORWADED.NONE)
            {
                storeIntData = 0;
                isForwarded = FORWADED.NONE;
            }

            switch(opcode)
            {
            case Instruction.INST_ADD:
                aluIntData = regAData + regBData;
                break;
            case Instruction.INST_LW:
            case Instruction.INST_SW:
                simulator.regs.setExmemCur(-1);
            case Instruction.INST_ADDI:
                aluIntData = regAData + imm;
                break;
            case Instruction.INST_SUB:
                aluIntData = regAData - regBData;
                break;
            case Instruction.INST_DIV:
                aluIntData = regAData / regBData;
                break;
            case Instruction.INST_MUL:
                aluIntData = regAData * regBData;
                break;
            case Instruction.INST_AND:
                aluIntData = regAData & regBData;
                break;
            case Instruction.INST_ANDI:
                aluIntData = regAData & imm;
                break;
            case Instruction.INST_OR:
                aluIntData = regAData | regBData;
                break;
            case Instruction.INST_ORI:
                aluIntData = regAData | imm;
                break;
            case Instruction.INST_XOR:
                aluIntData = regAData ^ regBData;
                break;
            case Instruction.INST_XORI:
                aluIntData = regAData ^ imm;
                break;
            case Instruction.INST_SLL:
            {
                int shiftAmount = prevStage.shftAmount;
                aluIntData = regAData << shiftAmount;
                break;
            }
            case Instruction.INST_SRL:
            {
                int shiftAmount = prevStage.shftAmount;
                aluIntData = regAData >> shiftAmount;
                break;
            }
            case Instruction.INST_SRA:
            {
                int shiftAmount = prevStage.shftAmount;
                aluIntData = regAData >>> shiftAmount;
                break;
            }
            case Instruction.INST_BEQ:
                destReg = -1;
                simulator.regs.setExmemCur(-1);
                if(regAData == regBData)
                {
                    simulator.pc.pc += imm - 4;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BNE:
                destReg = -1;
                simulator.regs.setExmemCur(-1);
                if(regAData != regBData)
                {
                    simulator.pc.pc += imm - 4;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BLTZ:
                destReg = -1;
                simulator.regs.setExmemCur(-1);
                if(regAData < 0)
                {
                    simulator.pc.pc += imm - 4;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BLEZ:
                destReg = -1;
                simulator.regs.setExmemCur(-1);
                if(regAData <= 0)
                {
                    simulator.pc.pc += imm - 4;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BGTZ:
                destReg = -1;
                simulator.regs.setExmemCur(-1);
                if(regAData > 0)
                {
                    simulator.pc.pc += imm - 4;
                    prevStage.shouldWriteback = false;
                    break;
                }
                shouldWriteback = false;
                break;
            case Instruction.INST_BGEZ:
                destReg = -1;
                simulator.regs.setExmemCur(-1);
                if(regAData >= 0)
                {
                    simulator.pc.pc += imm - 4;
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
        else
            simulator.regs.setExmemCur(-1);
        
    }
}
