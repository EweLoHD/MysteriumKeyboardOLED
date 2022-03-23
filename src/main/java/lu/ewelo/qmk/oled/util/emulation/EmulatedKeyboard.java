package lu.ewelo.qmk.oled.util.emulation;

import lu.ewelo.qmk.oled.keyboard.Keyboard;
import lu.ewelo.qmk.oled.keyboard.KeyboardInstruction;
import org.hid4java.HidDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmulatedKeyboard extends Keyboard {

    private static final Logger logger = LoggerFactory.getLogger(EmulatedKeyboard.class);

    private final EmulatedKeyboardFrame emulatedKeyboardFrame;

    public EmulatedKeyboard(HidDevice hidDevice) {
        super(hidDevice);
        this.emulatedKeyboardFrame = new EmulatedKeyboardFrame();
    }

    public EmulatedKeyboard() {
        this(null);
    }

    @Override
    public void open() {
        logger.info("Opened emulated Keyboard");
        emulatedKeyboardFrame.setVisible(true);
    }

    @Override
    public void write(byte[] message) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < message.length; i++) {
            sb.append(String.format("0x%02X", message[i]));

            if (i != message.length - 1) {
                sb.append(", ");
            }
        }

        logger.info("Write: " + sb);
    }

    @Override
    public void sendInstruction(KeyboardInstruction instruction) {
        emulatedKeyboardFrame.displayInstruction(instruction);
    }

    @Override
    public void close() {
        logger.info("Closed emulated Keyboard");
    }
}
