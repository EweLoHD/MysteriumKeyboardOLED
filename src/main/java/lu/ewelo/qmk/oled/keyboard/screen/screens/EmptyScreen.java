package lu.ewelo.qmk.oled.keyboard.screen.screens;

import lu.ewelo.qmk.oled.keyboard.screen.CustomScreen;
import lu.ewelo.qmk.oled.keyboard.screen.ScreenManager;
import lu.ewelo.qmk.oled.keyboard.screen.annotations.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Screen(id="empty", name="Empty")
public class EmptyScreen extends CustomScreen {

    private static final Logger logger = LoggerFactory.getLogger(EmptyScreen.class);

    public EmptyScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void draw() {
        writeString("Hello World");
    }
}
