package lu.ewelo.qmk.oled.keyboard.instructions;

import lu.ewelo.qmk.oled.keyboard.KeyboardInstruction;

import java.nio.charset.StandardCharsets;

public class WritePixelInstruction extends KeyboardInstruction {

    private static final byte INSTRUCTION_CODE = (byte) 0x04;

    private final int x;
    private final int y;
    private final boolean on;

    public WritePixelInstruction(int x, int y, boolean on) {
        super(INSTRUCTION_CODE);
        this.x = x;
        this.y = y;
        this.on = on;
    }

    @Override
    public byte[] getInstructionData() {
        //TODO Check with signed/unsigned x value (signed max = 127)
        return new byte[]{(byte) x, (byte) y, (byte) (on ? 1 : 0)};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOn() {
        return on;
    }

    @Override
    public String toString() {
        return "WritePixelInstruction{" +
                "x=" + x +
                ", y=" + y +
                ", on=" + on +
                '}';
    }
}
