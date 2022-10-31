package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
    }

    int getIntRegister(int regNum) {
        // todo - add supporting code        
        return 0;
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
