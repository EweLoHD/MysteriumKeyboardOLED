package lu.ewelo.qmk.oled;

import lu.ewelo.qmk.oled.screen.ScreenManager;
import org.hid4java.HidDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Keyboard {

    private static final Logger logger = LoggerFactory.getLogger(Keyboard.class);

    private final HidDevice hidDevice;
    private ScreenManager screenManager;

    public Keyboard(HidDevice hidDevice) {
        this.hidDevice = hidDevice;
    }

    /**
     * Opens the connection to the keyboard
     */
    public void open() {
        if (!hidDevice.open()) {
            logger.error("Could not open Connection to Keyboard! Last Error Message: " + hidDevice.getLastErrorMessage());
        }
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
            hidDevice.write(message, message.length, (byte) 0x00);
        } else {
            logger.error("Could not write message: Connection has not been opened!");
        }
    }


    public void setScreenManager(ScreenManager screenManager) {
        screenManager.setKeyboard(this);
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

    @Override
    public String toString() {
        return hidDevice.toString();
    }
}
