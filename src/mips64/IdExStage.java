package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    //int inst;
    Instruction inst;

    int registers[];

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
        registers = new int[32];
        registers[0] = 0;
        opcode = Instruction.getOpcodeFromName("NOP");
        inst = Instruction.getInstructionFromName("NOP");
    }

    Integer getIntRegister(int regNum) {
        return registers[regNum];
    }

    public void update() {
        instPC = simulator.getIfIdStage().instPC;
        opcode = simulator.getIfIdStage().opcode;
        inst = simulator.getIfIdStage().inst;

        Instruction decodedInstruction = Instruction.getInstructionFromName(Instruction.getNameFromOpcode(opcode));

        if (Instruction.getNameFromOpcode(opcode) == ""){
        }
        else if (decodedInstruction instanceof ITypeInst){
            regAData = getIntRegister(((ITypeInst)inst).getRT());
            regBData = getIntRegister(((ITypeInst)inst).getRS());
            immediate = ((ITypeInst)inst).getImmed();
        }
        else if (decodedInstruction instanceof RTypeInst){
            regAData = getIntRegister(((RTypeInst)inst).getRT());
            regBData = getIntRegister(((RTypeInst)inst).getRS());
            //immediate = 0;
        }
        else if (decodedInstruction instanceof JTypeInst){
            //regAData = 0;
            //regBData = 0;
            immediate = ((JTypeInst)inst).getOffset();
        }
    }
}
