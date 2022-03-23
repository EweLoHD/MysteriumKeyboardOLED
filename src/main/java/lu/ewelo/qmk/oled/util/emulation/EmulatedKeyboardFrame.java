package lu.ewelo.qmk.oled.util.emulation;

import lu.ewelo.qmk.oled.keyboard.KeyboardInstruction;
import lu.ewelo.qmk.oled.keyboard.instructions.ClearScreenInstruction;
import lu.ewelo.qmk.oled.keyboard.instructions.WriteImageInstruction;
import lu.ewelo.qmk.oled.keyboard.instructions.WritePixelInstruction;
import lu.ewelo.qmk.oled.keyboard.instructions.WriteStringInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

public class EmulatedKeyboardFrame extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(EmulatedKeyboard.class);

    private final Dimension RESOLUTION = new Dimension(128, 32);
    private final int SCALE = 4;

    private final Color BACKGROUND_COLOR = Color.BLACK;
    private final Color FOREGROUND_COLOR = Color.WHITE;

    private final List<KeyboardInstruction> instructions;

    public EmulatedKeyboardFrame() {
        super("OLED Screen");

        this.instructions = new ArrayList<>();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.error("Exception while setting the System LnF", e);
        }

        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE),
                BorderFactory.createMatteBorder(4, 4, 4, 4, BACKGROUND_COLOR)
        ));

        body.add(new OLEDScreen(RESOLUTION, SCALE));

        this.setContentPane(body);
        //this.setResizable(false);
        this.pack();
    }

    public void displayInstruction(KeyboardInstruction instruction) {
        if (instruction instanceof ClearScreenInstruction) {
            instructions.clear();
        } else {
            instructions.add(instruction);
        }

        repaint();
    }

    public class OLEDScreen extends JPanel {

        private final int scale;
        private final Dimension resolution;

        private int cursor;

        public OLEDScreen(Dimension resolution, int scale) {
            this.resolution = resolution;
            this.scale = scale;
            this.cursor = 0;

            this.setMinimumSize(resolution);
            this.setPreferredSize(new Dimension((int) (resolution.getWidth() * scale), (int) (resolution.getHeight() * scale)));
            //this.setSize(new Dimension((int) (resolution.getWidth() * scale), (int) (resolution.getHeight() * scale)));

            this.setBorder(null);
            //setBorder(new LineBorder(Color.WHITE, 5));

            this.setBackground(BACKGROUND_COLOR);
        }

        @Override
        public void paintComponent(Graphics g) {
            this.cursor = 0;

            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(FOREGROUND_COLOR);
            instructions.forEach(instruction -> {
                if (instruction instanceof WriteStringInstruction writeStringInstruction) {
                    drawString(writeStringInstruction.getText(), g);
                } else if (instruction instanceof WritePixelInstruction writePixelInstruction) {
                    if (writePixelInstruction.isOn()) {
                        g.setColor(FOREGROUND_COLOR);
                    } else {
                        g.setColor(BACKGROUND_COLOR);
                    }
                    g.fillRect(writePixelInstruction.getX(), writePixelInstruction.getY(), 4, 4);
                    g.setColor(FOREGROUND_COLOR);
                } else if (instruction instanceof WriteImageInstruction writeImageInstruction) {
                    Image img = writeImageInstruction.getImage();

                    ImageFilter filter = new RGBImageFilter() {
                        int transparentColor = Color.white.getRGB() | 0xFF000000;

                        public final int filterRGB(int x, int y, int rgb) {
                            if ((rgb | 0xFF000000) == transparentColor) {
                                return FOREGROUND_COLOR.getRGB();
                            } else {
                                return 0x00FFFFFF & rgb;
                            }
                        }
                    };

                    ImageProducer filteredImgProd = new FilteredImageSource(img.getSource(), filter);
                    Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);

                    g.drawImage(transparentImg, 0, 0, img.getWidth(null)*scale, img.getHeight(null)*scale, null,null);
                }
            });
        }

        private void setCursor(int col, int line) {
            int maxCharPerLine = (int) resolution.getWidth()/Font.FONT_WIDTH;
            cursor += col + line*maxCharPerLine;
        }

        private void drawChar(char c, Graphics g) {
            g.setColor(FOREGROUND_COLOR);

            for (int k = 0; k < Font.FONT_WIDTH; k++) {
                char b = Font.FONT[c*Font.FONT_WIDTH + k];
                for (int i = 1, j = 0; i <= b ; i<<=1, j++) {
                    boolean bit = (b&i) != 0;
                    if (bit) {
                        int maxCharPerLine = (int) resolution.getWidth()/Font.FONT_WIDTH;
                        int col = cursor % maxCharPerLine;
                        int line = cursor / maxCharPerLine;

                        g.fillRect(col*Font.FONT_WIDTH*scale + k*scale, line*Font.FONT_HEIGHT*scale + j*scale, scale, scale);
                    }
                }
            }

            cursor++;
        }

        private void drawString(String s, Graphics g) {
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                drawChar(chars[i], g);
            }
        }
    }

}
