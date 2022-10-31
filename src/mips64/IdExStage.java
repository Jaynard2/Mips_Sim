package mips64;

import java.io.Console;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    int inst;

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

        Instruction decodedInstruction = Instruction.getInstructionFromName(Instruction.getNameFromOpcode(opcode));

        if (Instruction.getNameFromOpcode(opcode) == ""){
        }
        else if (decodedInstruction instanceof ITypeInst){
            regAData = getIntRegister(((ITypeInst)decodedInstruction).getRT());
            regBData = getIntRegister(((ITypeInst)decodedInstruction).getRS());
            immediate = ((ITypeInst)decodedInstruction).getImmed();
        }
        else if (decodedInstruction instanceof RTypeInst){
            regAData = getIntRegister(((RTypeInst)decodedInstruction).getRT());
            regBData = getIntRegister(((RTypeInst)decodedInstruction).getRS());
            //immediate = 0;
        }
        else if (decodedInstruction instanceof JTypeInst){
            //regAData = 0;
            //regBData = 0;
            immediate = ((JTypeInst)decodedInstruction).getOffset();
        }
    }
}
