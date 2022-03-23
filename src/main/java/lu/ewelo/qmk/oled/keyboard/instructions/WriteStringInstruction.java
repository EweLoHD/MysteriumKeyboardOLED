package lu.ewelo.qmk.oled.keyboard.instructions;

import lu.ewelo.qmk.oled.keyboard.KeyboardInstruction;

import java.nio.charset.StandardCharsets;

public class WriteStringInstruction extends KeyboardInstruction {

    private static final byte INSTRUCTION_CODE = (byte) 0x03;

    private final String text;
    private final boolean inverted;
    private final int startingLine;

    public WriteStringInstruction(String text, boolean inverted, int startingLine) {
        super(INSTRUCTION_CODE);
        this.text = text;
        this.inverted = inverted;
        this.startingLine = startingLine;
    }

    public WriteStringInstruction(String text, boolean inverted) {
        this(text, inverted, 0);
    }

    @Override
    public byte[] getInstructionData() {
        byte[] textData = text.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[textData.length + 2];

        System.arraycopy(textData, 0, data, 2, textData.length);

        data[0] = inverted ? (byte) 0x01 : (byte) 0x00;
        data[1] = (byte) startingLine;

        return data;
    }

    public String getText() {
        return text;
    }

    public boolean isInverted() {
        return inverted;
    }

    public int getStartingLine() {
        return startingLine;
    }

    @Override
    public String toString() {
        return "WriteStringInstruction{" +
                "text='" + text + '\'' +
                ", inverted=" + inverted +
                ", startingLine=" + startingLine +
                '}';
    }
}
