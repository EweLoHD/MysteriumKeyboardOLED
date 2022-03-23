package lu.ewelo.qmk.oled.keyboard;

import lu.ewelo.qmk.oled.keyboard.screen.ScreenManager;
import org.hid4java.HidDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;


public class Keyboard {

    private static final Logger logger = LoggerFactory.getLogger(Keyboard.class);

    private final HidDevice hidDevice;
    private ScreenManager screenManager;
    private final ScreenSize screenSize;

    public Keyboard(HidDevice hidDevice, ScreenSize screenSize) {
        this.hidDevice = hidDevice;
        this.screenSize = screenSize;
    }

    public Keyboard(HidDevice hidDevice) {
        this(hidDevice, new ScreenSize(128, 32));
    }

    /**
     * Opens the connection to the keyboard
     */
    public void open() {
        if (!hidDevice.open()) {
            logger.error("Could not open Connection to Keyboard! Last Error Message: " + hidDevice.getLastErrorMessage());
        } else {
            logger.info("Opened Connection to the Keyboard.");
        }
    }

    public void render() {
        screenManager.render();
    }

    /**
     * Closes the connection to the keyboard
     */
    public void close() {
        hidDevice.close();
    }

    /**
     * Sends an array of bytes to the keyboard. The connection to the keyboard has to be open!
     * @param message The message to be sent to the keyboard
     */
    public void write(byte[] message) {
        if (hidDevice.isOpen()) {
            logger.info("Writing to Keyboard: " + Arrays.asList(message).stream().map(x -> String.format("0x%02X", x)).collect(Collectors.joining()));
            hidDevice.write(message, message.length, (byte) 0x00);
        } else {
            logger.error("Could not write message: Connection has not been opened!");
        }
    }

    public void sendInstruction(KeyboardInstruction instruction) {
        logger.info("Sending Instruction to Keyboard: " + instruction);
        write(instruction.getData());
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    /**
     * Returns the "PRODUCT" Name of the Keyboard.
     * Simply calls <code>hidDevice.getProduct()</code>
     * @return the name of the keyboard.
     */
    public String getName() {
        return hidDevice.getProduct();
    }

    /**
     * Returns the {@link HidDevice HidDevice}, which is used to communicate over HID between the computer and keyboard. For more information look <a href="https://github.com/gary-rowe/hid4java">here</a>
     * @see HidDevice
     * @return the {@link HidDevice HidDevice} of the keyboard
     */
    public HidDevice getHidDevice() {
        return hidDevice;
    }

    public ScreenSize getScreenSize() {
        return screenSize;
    }

    @Override
    public String toString() {
        return hidDevice.toString();
    }

    public static class ScreenSize {
        private int width;
        private int height;

        public ScreenSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

}
