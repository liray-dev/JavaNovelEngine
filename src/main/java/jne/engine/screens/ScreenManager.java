package jne.engine.screens;

import jne.engine.screens.listeners.ScreenListener;
import jne.engine.screens.listeners.SubScreenListener;
import jne.engine.utils.IWrapper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

import static jne.engine.utils.Util.getSystemTime;

/**
 * This class is a manager for screens and sub-screens, and then makes a calculation of mouse and keyboard positions for correct display in the main application displays
 */
public class ScreenManager implements IWrapper {

    public int width, height;

    public ScreenListener initScreen;
    public ScreenListener currentScreen;
    public SubScreenListener subScreen;

    /**
     * Method processing the OpenGL engine renderer
     */
    public void render(float partialTick) {
        RENDER.color(Color.WHITE, () -> {
            RENDER.drawQuad(50, 50, -1, width - 50, height - 50);
        });
    }

    /**
     * A method that handles mouse input with mouse movement
     */
    private void mouseMove(int x, int y) {
        System.out.println(x + "|" + y);
    }

    /**
     * A method that handles mouse input when the mouse is clicked and moved
     */
    private void mouseClickMove(int x, int y, int eventButton, long last) {

    }

    /**
     * A method that handles mouse input when the key is lifted
     */
    private void mouseReleased(int x, int y, int button) {

    }

    /**
     * A method that handles mouse click input
     */
    private void mouseClicked(int x, int y, int eventButton) {
    }

    /**
     * A method that handles keyboard input
     */
    private void keyTyped(char character, int eventKey) {
    }

    /**
     * A method that handles every millisecond of engine update
     */
    public void tick() {
        input();
    }

    /**
     * It is VERY IMPORTANT to set the current screen via this method, otherwise it will not initialize
     *
     * @param screen An instance of the ScreenListener class
     */
    public void setScreen(ScreenListener screen) {
        if (screen == null || this.currentScreen == screen) {
            return;
        }

        ScreenListener old = this.currentScreen;

        if (old != null) {
            old.close();
        }

        this.currentScreen = screen;
        this.resize(WINDOW.displayWidth, WINDOW.displayHeight);
    }

    private long lastMouseEvent;
    private int eventButton;
    private double scaleWidth, scaleHeight;

    /**
     * Called when resizing the screen, to clearly track the position of the cursor relative to the maximum resolution of the screen
     */
    public void resize(int width, int height) {
        DisplayMode displaymode = Display.getDesktopDisplayMode();
        this.width = displaymode.getWidth();
        this.height = displaymode.getHeight();

        this.scaleWidth = (float) this.width / width;
        this.scaleHeight = (float) this.height / height;
    }

    /**
     * Reads possible mouse and keyboard updates
     */
    public void input() {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.mouse();
            }
        }

        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.keyboard();
            }
        }
    }

    /**
     * Handles computer mouse events
     */
    private void mouse() {
        int x = (int) ((Mouse.getEventX()) * scaleWidth);
        int y = (int) (height - ((Mouse.getEventY()) * scaleHeight) - 0.5);
        int button = Mouse.getEventButton();

        boolean flag = true;

        if (Mouse.getEventButtonState()) {
            this.eventButton = button;
            this.lastMouseEvent = getSystemTime();
            this.mouseClicked(x, y, this.eventButton);
            flag = false;
        } else if (button != -1) {
            this.eventButton = -1;
            this.mouseReleased(x, y, button);
            flag = false;
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long last = getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(x, y, this.eventButton, last);
            flag = false;
        }

        if (flag) {
            this.mouseMove(x, y);
        }
    }

    /**
     * Handles keyboard events
     */
    private void keyboard() {
        char character = Keyboard.getEventCharacter();

        if (Keyboard.getEventKey() == 0 && character >= ' ' || Keyboard.getEventKeyState()) {
            int eventKey = Keyboard.getEventKey();
            this.keyTyped(character, eventKey);
        }

    }


}
