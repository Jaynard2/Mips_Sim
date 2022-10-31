package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    int inst;

    Instruction decodedInstruction = null;

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

        decodedInstruction = Instruction.getInstructionFromOper(opcode);

        if (decodedInstruction instanceof ITypeInst){
            regAData = getIntRegister(((ITypeInst)decodedInstruction).getRT());
            regBData = getIntRegister(((ITypeInst)decodedInstruction).getRS());
            immediate = ((ITypeInst)decodedInstruction).getImmed();
        }
        else if (decodedInstruction instanceof RTypeInst){
            regAData = getIntRegister(((RTypeInst)decodedInstruction).getRT());
            regBData = getIntRegister(((RTypeInst)decodedInstruction).getRS());
        }
        else if (decodedInstruction instanceof JTypeInst){
            immediate = ((JTypeInst)decodedInstruction).getOffset();
        }
    }
}
