package lu.ewelo.qmk.oled.keyboard.screen;

import lu.ewelo.qmk.oled.keyboard.Keyboard;
import lu.ewelo.qmk.oled.keyboard.instructions.ClearScreenInstruction;
import lu.ewelo.qmk.oled.keyboard.instructions.WriteImageInstruction;
import lu.ewelo.qmk.oled.keyboard.instructions.WritePixelInstruction;
import lu.ewelo.qmk.oled.keyboard.instructions.WriteStringInstruction;
import lu.ewelo.qmk.oled.keyboard.screen.annotations.Screen;
import lu.ewelo.qmk.oled.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class CustomScreen {

    private static final Logger logger = LoggerFactory.getLogger(CustomScreen.class);

    private final String id;
    private final String name;

    private Keyboard keyboard;
    private ScreenManager screenManager;

    public CustomScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.id = this.getClass().getAnnotation(Screen.class).id();
        this.name = this.getClass().getAnnotation(Screen.class).name();
    }

    public abstract void draw();

    public void render() {
        clearScreen();
        draw();
    }

    public void clearScreen() {
        keyboard.sendInstruction(new ClearScreenInstruction());
    }

    /**
     * Writes a string to the buffer at current cursor position
     * Advances the cursor while writing, inverts the pixels if true
     * See <code>void oled_write(const char *data, bool invert);</code> in the QMK Oled Driver
     * @param text The text written to the OLED Screen
     * @param inverted Inverts the pixels if true
     */
    public void writeString(String text, boolean inverted) {
        keyboard.sendInstruction(new WriteStringInstruction(text, inverted));
    }

    /**
     * Writes a string to the buffer at current cursor position
     * Advances the cursor while writing, inverts the pixels if true
     * See <code>void oled_write(const char *data, bool invert);</code> in the QMK Oled Driver
     * @param text The text written to the OLED Screen
     */
    public void writeString(String text) {
        writeString(text, false);
    }

    /**
     * Sets a specific pixel on or off
     * Coordinates start at top-left and go right and down for positive x and y
     * See <code>void oled_write_pixel(uint8_t x, uint8_t y, bool on);</code> in the QMK Oled Driver
     * @param x The x coordinate of the pixel
     * @param y The y coordinate of the pixel
     * @param on If true, the pixel will be turned on
     */
    public void writePixel(int x, int y, boolean on) {
        keyboard.sendInstruction(new WritePixelInstruction(x, y, on));
    }

    public void writeImage(Image img) {
        if (img.getWidth(null) > keyboard.getScreenSize().getWidth() || img.getHeight(null) > keyboard.getScreenSize().getHeight()) {
            logger.warn("Image exceeds OLED Screen Size!");
        }
        keyboard.sendInstruction(new WriteImageInstruction(img));
    }

    public BufferedImage getEmptyImage() {
        return new BufferedImage(128, 32, BufferedImage.TYPE_BYTE_BINARY);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.keyboard = screenManager.getKeyboard();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public Config getConfig() {
        return screenManager.getConfig();
    }

    public Keyboard.ScreenSize getScreenSize() {
        return getScreenManager().getKeyboard().getScreenSize();
    }

    @Override
    public String toString() {
        return name + "(id: " + id + ")";
    }

}
