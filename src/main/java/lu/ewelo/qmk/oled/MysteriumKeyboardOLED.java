package lu.ewelo.qmk.oled;

import lu.ewelo.qmk.oled.keyboard.Keyboard;
import lu.ewelo.qmk.oled.keyboard.screen.ScreenManager;
import lu.ewelo.qmk.oled.keyboard.screen.screens.SpotifyScreen;
import lu.ewelo.qmk.oled.keyboard.screen.screens.TestScreen;
import lu.ewelo.qmk.oled.util.Config;
import lu.ewelo.qmk.oled.util.emulation.EmulatedKeyboard;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

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


        /*KeyboardManager keyboardManager = new KeyboardManager();
        List<Keyboard> attachedKeyboards = keyboardManager.getAttachedKeyboards(KeyboardManager.QMK_DEFAULT_USAGE_ID);

        for (Keyboard k : attachedKeyboards) {
            System.out.println(k);
        }

        Keyboard keyboard = attachedKeyboards.get(0);

        keyboard.open();
        keyboard.write("Hello".getBytes(StandardCharsets.UTF_8));*/



        Keyboard keyboard = new EmulatedKeyboard();
        ScreenManager screenManager = new ScreenManager(keyboard);

        Config config = new Config.ConfigBuilder()
                .setPath("config.properties")
                .addDefault("spotify-client-id", "<client-id>")
                .addDefault("spotify-client-secret", "<client-secret>")
                //.addDefault("spotify-redirect-url", "<redirect-url>")
                .build();

        screenManager.setConfig(config);

        //screenManager.addScreen(new TestScreen());
        screenManager.addScreen(new SpotifyScreen(screenManager));

        keyboard.setScreenManager(screenManager);
        keyboard.open();

        screenManager.render();

        keyboard.close();
    }

}
