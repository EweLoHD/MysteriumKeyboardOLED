package lu.ewelo.qmk.oled.screen;

import lu.ewelo.qmk.oled.screen.annotations.Screen;

@Screen(id="screen", name = "Screen")
public abstract class CustomScreen {

    private final String id;
    private final String name;

    private final ScreenManager screenManager;

    public CustomScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;

        this.id = this.getClass().getAnnotation(Screen.class).id();
        this.name = this.getClass().getAnnotation(Screen.class).name();
    }

    public abstract void draw();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }
}
