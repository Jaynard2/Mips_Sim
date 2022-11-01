package mips64;

import mips64.Registers.FORWARD_STAGES;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int regA;
    int regB;
    int immediate;
    int destReg;
    int shftAmount;
    Instruction inst;

    private final int jumpCode = Instruction.getOpcodeFromName("J");
    private final int jalCode = Instruction.getOpcodeFromName("JAL");
    private final int jrCode = Instruction.getOpcodeFromName("JR");
    private final int jalrCode = Instruction.getOpcodeFromName("JALR");

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
        opcode = Instruction.getOpcodeFromName("NOP");
        inst = Instruction.getInstructionFromName("NOP");
    }

    Integer getIntRegister(int regNum) {
        Registers regs = simulator.regs;
        regs.setConcerned(FORWARD_STAGES.NONE);
        return regs.readReg(regNum);
    }

    public void update() {
        if(!simulator.stall)
        {
            instPC = simulator.getIfIdStage().instPC;
            opcode = simulator.getIfIdStage().opcode;
            inst = simulator.getIfIdStage().inst;
            shouldWriteback = true;
            destReg = -1;
            regA = -1;
            regB = -1;
            regAData = 0;
            regBData = 0;

            Registers regs = simulator.regs;
            regs.setConcerned(FORWARD_STAGES.ALL);

            if (inst instanceof ITypeInst){
                regA = ((ITypeInst)inst).getRS();
                regAData = regs.readReg(regA);
                regB = destReg = ((ITypeInst)inst).getRT();
                regBData = regs.readReg(destReg);
                immediate = ((ITypeInst)inst).getImmed();

                if(opcode == jrCode || opcode == jalrCode)
                {
                    if(opcode == jalrCode)
                    {
                        simulator.regs.writeReg(31, simulator.pc.pc);
                    }
                    simulator.pc.pc = regAData;
                    destReg = -1;
                }
            }
            else if (inst instanceof RTypeInst){
                regA = ((RTypeInst)inst).getRS();
                regB = ((RTypeInst)inst).getRT();
                regAData = regs.readReg(regA);
                regBData = regs.readReg(regB);
                destReg = ((RTypeInst)inst).getRD();
                shftAmount = ((RTypeInst)inst).getShamt();
            }
            else if (inst instanceof JTypeInst){
                if(opcode == jumpCode || opcode == jalCode)
                {
                    if(opcode == jalCode)
                    {
                        simulator.regs.writeReg(31, simulator.pc.pc);
                    }
                    simulator.pc.pc += ((JTypeInst)inst).getOffset();
                    destReg = -1;
                }
            }
        }
    }
}
