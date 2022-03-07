package lu.ewelo.qmk.oled.screen;

import lu.ewelo.qmk.oled.screen.annotations.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lu.ewelo.qmk.oled.screen.annotations.ConfigValue;

@Screen(id="test-screen", name="Test Screen")
public class TestScreen extends CustomScreen {

    private static final Logger logger = LoggerFactory.getLogger(TestScreen.class);

    @ConfigValue(key = "test-key", defaultValue = "some default shit")
    private String apiKey;

    public TestScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void draw() {
    }
}
