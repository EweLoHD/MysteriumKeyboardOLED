package lu.ewelo.qmk.oled.screen;

import lu.ewelo.qmk.oled.Keyboard;
import lu.ewelo.qmk.oled.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenManager {

    private static final Logger logger = LoggerFactory.getLogger(ScreenManager.class);

    private Config config;
    private Keyboard keyboard;

    public ScreenManager() {

    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
