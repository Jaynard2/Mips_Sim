package mips64;

public class ExMemStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int storeIntData;
    int destReg;    
    int regA;
    int regB;
    boolean forRegA;

    Instruction decodedInstruction;

    public ExMemStage(PipelineSimulator sim) {
        simulator = sim;
        opcode = Instruction.getOpcodeFromName("NOP");
        storeIntData = 0;
    }

    public void update() {
        if(!simulator.stall)
        {
            IdExStage prevStage  = simulator.idEx;
            instPC = prevStage.instPC;
            opcode = prevStage.opcode;
            shouldWriteback = prevStage.shouldWriteback;
            destReg = prevStage.destReg;
            regA = prevStage.regA;
            regB = prevStage.regB;
            storeIntData = prevStage.storeIntData;
            simulator.regs.setExmemCur(destReg);

            int regA = prevStage.regAData;
            int regB = prevStage.regBData;
            int imm = prevStage.immediate;

            switch(opcode)
            {
            case Instruction.INST_ADD:
                aluIntData = regA + regB;
                break;
            case Instruction.INST_LW:
                simulator.stall = true;
            case Instruction.INST_SW:
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
                String name = Instruction.getNameFromOpcode(opcode);
                RTypeInst inst = (RTypeInst)Instruction.getInstructionFromName(name);
                int shiftAmount = inst.getShamt();
                aluIntData = regA << shiftAmount;
                break;
            }
            case Instruction.INST_SRL:
            {
                String name = Instruction.getNameFromOpcode(opcode);
                RTypeInst inst = (RTypeInst)Instruction.getInstructionFromName(name);
                int shiftAmount = inst.getShamt();
                aluIntData = regA >> shiftAmount;
                break;
            }
            case Instruction.INST_SRA:
            {
                String name = Instruction.getNameFromOpcode(opcode);
                RTypeInst inst = (RTypeInst)Instruction.getInstructionFromName(name);
                int shiftAmount = inst.getShamt();
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
        else
        {
            simulator.stall = false;
        }
        
    }
}
