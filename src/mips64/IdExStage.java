package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    int destReg;
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
        destReg = -1;

        if (inst instanceof ITypeInst){
            regAData = getIntRegister(((ITypeInst)inst).getRS());
            destReg = ((ITypeInst)inst).getRT();
            immediate = ((ITypeInst)inst).getImmed();
        }
        else if (inst instanceof RTypeInst){
            regAData = getIntRegister(((RTypeInst)inst).getRS());
            regBData = getIntRegister(((RTypeInst)inst).getRD());
            destReg = ((RTypeInst)inst).getRT();
            //immediate = 0;
        }
        else if (inst instanceof JTypeInst){
            //regAData = 0;
            //regBData = 0;
            immediate = ((JTypeInst)inst).getOffset();
        }
    }
}
