package jne.engine.screens;

import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.events.EventListenerHelper;
import jne.engine.events.types.ScreenEvent;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.TextureContainer;
import jne.engine.api.IWrapper;
import jne.engine.utils.Discord;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static jne.engine.utils.Util.getSystemTime;

/**
 * This class is a manager for screens and sub-screens, and then makes a calculation of mouse and keyboard positions for correct display in the main application displays
 */
public class ScreenManager implements IWrapper {

    public int width, height;

    public ComponentsListener initScreen;
    public ComponentsListener currentScreen;

    public List<ComponentsListener> subScreens = new ArrayList<>();

    /**
     * Method processing the OpenGL engine renderer
     */
    public void render(float partialTick) {
        RENDER.color(Color.WHITE, () -> {
            RENDER.drawTexturedQuad(0, 0, WINDOW.desktop.getWidth(), WINDOW.desktop.getHeight(), TextureContainer.get("background"));
        });

        new ScreenEvent.Render(partialTick, width, height).post();
    }

    /**
     * A method that handles mouse input with mouse movement
     */
    private void mouseMove(int mouseX, int mouseY) {
        new ScreenEvent.MouseMove(mouseX, mouseY).post();
    }

    /**
     * A method that handles mouse input when the mouse is clicked and moved
     */
    private void mouseClickMove(int mouseX, int mouseY, int button, long last)  {
        new ScreenEvent.MouseClickMove(mouseX, mouseY, button, last).post();
    }

    /**
     * A method that handles mouse input when the key is lifted
     */
    private void mouseReleased(int mouseX, int mouseY, int button) {
        new ScreenEvent.MouseInput(MouseClickType.RELEASED, mouseX, mouseY, button).post();
    }

    /**
     * A method that handles mouse click input
     */
    private void mouseClicked(int mouseX, int mouseY, int button) {
        new ScreenEvent.MouseInput(MouseClickType.CLICKED, mouseX, mouseY, button).post();
    }

    /**
     * A method that handles keyboard input
     */
    private void keyTyped(char character, int button, KeyboardType type) {
        new ScreenEvent.Keyboard(type, character, button).post();
    }

    /**
     * A method that handles every millisecond of engine update
     */
    public void tick() {
        new ScreenEvent.Tick().post();
        input();
    }

    /**
     * It is VERY IMPORTANT to set the current screen via this method, otherwise it will not initialize
     *
     * @param screen An instance of the ScreenListener class
     */
    public void setScreen(ComponentsListener screen) {
        if (screen == null || this.currentScreen == screen) {
            return;
        }

        ComponentsListener old = this.currentScreen;

        if (old != null) {
            EventListenerHelper.unregister(old);
            old.close();
        }

        EventListenerHelper.register(screen);
        Discord.state = screen.getClass().getSimpleName();
        Discord.update();
        this.currentScreen = screen;
        this.resize(WINDOW.displayWidth, WINDOW.displayHeight);
    }

    public void addSubscreen(ComponentsListener subScreen) {
        if (this.subScreens.contains(subScreen)) {
            System.out.println("The additional screen has already been added");
        } else {
            EventListenerHelper.register(subScreen);
            this.subScreens.add(subScreen);
            Discord.state = subScreen.getClass().getSimpleName();
            Discord.update();
            subScreen.resize(width, height);
        }
    }

    public void removeSubscreen(ComponentsListener subScreen) {
        if (this.subScreens.contains(subScreen)) {
            new ScreenEvent.Close(subScreen).post();
            EventListenerHelper.unregister(subScreen);
            subScreen.close();
            this.subScreens.remove(subScreen);
            if (!subScreens.isEmpty()) {
                Discord.state = subScreens.get(subScreens.size() - 1).getClass().getSimpleName();
                Discord.update();
            } else {
                Discord.state = currentScreen.getClass().getSimpleName();
                Discord.update();
            }
        } else {
            System.out.println("Additional screen not found");
        }
    }

    public void clearSubscreens() {
        Iterator<ComponentsListener> iterator = this.subScreens.iterator();
        while (iterator.hasNext()) {
            ComponentsListener componentsListener = iterator.next();
            EventListenerHelper.unregister(componentsListener);
            componentsListener.close();
            iterator.remove();
        }
    }

    private long lastMouseEvent;
    private int mouseEventButton;

    private char lastChar;
    private long lastKeyboardEvent;
    private int keyboardEventButton;

    /**
     * Called when resizing the screen, to clearly track the position of the cursor relative to the maximum resolution of the screen
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        if (currentScreen != null) {
            this.currentScreen.resize(width, height);
        }
        this.subScreens.forEach(it -> it.resize(width, height));
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
            if (Keyboard.isKeyDown(keyboardEventButton) && getSystemTime() - lastKeyboardEvent >= 200L) {
                this.keyTyped(lastChar, keyboardEventButton, KeyboardType.PRESSED);
            }
        }
    }

    /**
     * Handles computer mouse events
     */
    private void mouse() {
        int x = Mouse.getEventX();
        int y = height - Mouse.getEventY();
        int button = Mouse.getEventButton();

        boolean flag = true;

        if (Mouse.getEventButtonState()) {
            this.mouseEventButton = button;
            this.lastMouseEvent = getSystemTime();
            this.mouseClicked(x, y, this.mouseEventButton);
            flag = false;
        } else if (button != -1) {
            this.mouseEventButton = -1;
            this.mouseReleased(x, y, button);
            flag = false;
        } else if (this.mouseEventButton != -1 && this.lastMouseEvent > 0L) {
            long last = getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(x, y, this.mouseEventButton, last);
            flag = false;
        }

        int dWheel = Mouse.getDWheel();

        if (Mouse.hasWheel() && dWheel != 0) {
            new ScreenEvent.Wheel(x, y, dWheel).post();
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
        int eventKey = Keyboard.getEventKey();

        if (Keyboard.getEventKeyState()) {
            this.lastChar = character;
            this.keyboardEventButton = eventKey;
            this.lastKeyboardEvent = getSystemTime();
            this.keyTyped(character, eventKey, KeyboardType.START);
        } else {
            this.keyTyped(lastChar, keyboardEventButton, KeyboardType.STOP);
        }
    }


}
