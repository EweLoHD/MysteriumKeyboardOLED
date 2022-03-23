package lu.ewelo.qmk.oled.keyboard.instructions;

import lu.ewelo.qmk.oled.keyboard.KeyboardInstruction;

public class ClearScreenInstruction extends KeyboardInstruction {

    private static final byte INSTRUCTION_CODE = (byte) 0x00;

    public ClearScreenInstruction() {
        super(INSTRUCTION_CODE);
    }

    @Override
    public byte[] getInstructionData() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return "ClearScreenInstruction{}";
    }
}
