package lu.ewelo.qmk.oled.keyboard.screen;

import lu.ewelo.qmk.oled.keyboard.Keyboard;
import lu.ewelo.qmk.oled.keyboard.screen.screens.EmptyScreen;
import lu.ewelo.qmk.oled.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager {

    private static final Logger logger = LoggerFactory.getLogger(ScreenManager.class);

    private Config config;
    private Keyboard keyboard;
    private List<CustomScreen> screens;
    private CustomScreen currentScreen;

    public ScreenManager(Keyboard keyboard) {
        this.keyboard = keyboard;
        this.config = null;
        this.screens = new ArrayList<>();
        this.currentScreen = new EmptyScreen(this);
    }

    public void addScreen(CustomScreen screen) {
        screen.setScreenManager(this);
        screens.add(screen);

        if (currentScreen instanceof EmptyScreen) {
            this.currentScreen = screen;
        }
    }

    public void switchToNextScreen() {
        if (!screens.isEmpty()) {
            logger.info("Switching to next Screen");

            int i = screens.indexOf(currentScreen);
            currentScreen = screens.get(i < screens.size() ? i + 1 : 0);

            render();
        } else {
            logger.warn("There are no Screens");
        }
    }

    public void switchToPrevScreen() {
        if (!screens.isEmpty()) {
            logger.info("Switching to previous Screen");

            int i = screens.indexOf(currentScreen);
            currentScreen = screens.get(i == 0 ? screens.size() - 1 : i - 1);

            render();
        } else {
            logger.warn("There are no Screens");
        }
    }

    public void render() {
        logger.info("Rendering Screen: " + currentScreen.toString());
        currentScreen.render();
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
