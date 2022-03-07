package lu.ewelo.qmk.oled;

import lu.ewelo.qmk.oled.screen.TestScreen;
import lu.ewelo.qmk.oled.util.Config;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MysteriumKeyboardOLED {

    public static void main(String[] args) {
        /*TestScreen testScreen = new TestScreen(null);
        testScreen.getName();

        Config config = new Config.ConfigBuilder()
                .setPath("config.properties")
                .addDefault("spotify-client-id", "<client-id>")
                .addDefault("spotify-client-secret", "<client-secret>")
                .addDefault("spotify-redirect-url", "<redirect-url>")
                .build();*/

        KeyboardManager keyboardManager = new KeyboardManager();
        List<Keyboard> attachedKeyboards = keyboardManager.getAttachedKeyboards(KeyboardManager.QMK_DEFAULT_USAGE_ID);

        for (Keyboard k : attachedKeyboards) {
            System.out.println(k);
        }

        Keyboard keyboard = attachedKeyboards.get(0);

        keyboard.open();
        keyboard.write("Hello".getBytes(StandardCharsets.UTF_8));
    }

}
