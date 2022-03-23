package lu.ewelo.qmk.oled.keyboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class KeyboardInstruction {

    private static final Logger logger = LoggerFactory.getLogger(KeyboardInstruction.class);

    private final byte instructionCode;

    public KeyboardInstruction(byte instructionCode) {
        this.instructionCode = instructionCode;
    }

    public abstract byte[] getInstructionData();

    public byte[] getData() {
        byte[] data = new byte[getInstructionData().length + 1];

        System.arraycopy(getInstructionData(), 0, data, 1, data.length-1);
        data[0] = getInstructionCode();

        return data;
    }

    public byte getInstructionCode() {
        return instructionCode;
    }
}
