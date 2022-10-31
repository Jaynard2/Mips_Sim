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
    }
}
