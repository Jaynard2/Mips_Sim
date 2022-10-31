package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;

    int registers[];

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
        registers = new int[32];
        registers[0] = 0;
    }

    Integer getIntRegister(int regNum) {
        return registers[regNum];
    }

    public void update() {
        instPC = simulator.getIfIdStage().instPC;
        opcode = simulator.getIfIdStage().opcode;

        Instruction decodedInst = Instruction.getInstructionFromOper(opcode);

        if (decodedInst instanceof ITypeInst){
            regAData = getIntRegister(((ITypeInst)decodedInst).getRT());
            regBData = getIntRegister(((ITypeInst)decodedInst).getRS());
            immediate = ((ITypeInst)decodedInst).getImmed();
        }
        else if (decodedInst instanceof RTypeInst){
            regAData = getIntRegister(((RTypeInst)decodedInst).getRT());
            regBData = getIntRegister(((RTypeInst)decodedInst).getRS());
        }
        else if (decodedInst instanceof JTypeInst){
            immediate = ((JTypeInst)decodedInst).getOffset();
        }
    }
}
