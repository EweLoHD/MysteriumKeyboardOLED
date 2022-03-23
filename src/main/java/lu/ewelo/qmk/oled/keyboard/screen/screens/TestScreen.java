package lu.ewelo.qmk.oled.keyboard.screen.screens;

import lu.ewelo.qmk.oled.keyboard.screen.CustomScreen;
import lu.ewelo.qmk.oled.keyboard.screen.ScreenManager;
import lu.ewelo.qmk.oled.keyboard.screen.annotations.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lu.ewelo.qmk.oled.keyboard.screen.annotations.ConfigValue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

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
        writeString("Hello");
        writeString(" World!");

        BufferedImage bufferedImage = new BufferedImage(128, 32, BufferedImage.TYPE_BYTE_BINARY);

        Graphics g = bufferedImage.getGraphics();
        g.setColor(Color.GREEN);

        g.fillRect(0, 0, 20, 20);


        /*byte[] pixels = ((DataBufferByte) bufferedImage.getData().getDataBuffer()).getData();
        System.out.println(pixels.length);

        for (int i = 0; i < pixels.length; i++) {
            if (true) {
                String s = Integer.toBinaryString(pixels[i]);

                if (s.length() >= 8) {
                    s = s.substring(s.length() - 8, s.length());
                } else {
                    s = String.format("%08d", Integer.valueOf(s));
                }

                System.out.print(s);
            }

            if ((i+1)%16 == 0) {
                System.out.println();
            }
        }*/

        writeImage(bufferedImage);
    }
}
