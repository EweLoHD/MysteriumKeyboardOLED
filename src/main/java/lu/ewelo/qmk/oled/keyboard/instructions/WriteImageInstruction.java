package lu.ewelo.qmk.oled.keyboard.instructions;

import lu.ewelo.qmk.oled.keyboard.KeyboardInstruction;

import java.awt.*;

public class WriteImageInstruction extends KeyboardInstruction {

    private static final byte INSTRUCTION_CODE = (byte) 0x05;

    private final Image image;

    public WriteImageInstruction(Image image) {
        super(INSTRUCTION_CODE);
        this.image = image;
    }

    @Override
    public byte[] getInstructionData() {
        //TODO
        //-> See TestScreen

        return new byte[0];
    }

    public Image getImage() {
        return image;
    }
}
