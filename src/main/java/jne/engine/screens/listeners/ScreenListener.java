package jne.engine.screens.listeners;

import jne.engine.core.JNE;
import jne.engine.utils.IScreenListener;
import jne.engine.utils.IWrapper;
import jne.engine.screens.components.Component;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jne.engine.utils.Util.getSystemTime;

public class ScreenListener implements IScreenListener, IWrapper {

    public JNE jne;
    public int width, height;

    private long lastMouseEvent;
    private int eventButton;

    @Override
    public void init() {

    }

    @Override
    public void close() {
        this.components.forEach(Component::close);
    }

    @Override
    public void render(float partialTicks) {
        if (this.subScreenListener == null || this.subScreenListener.isRenderLastScreen) {
            this.components.forEach(it -> it.render(partialTicks));
        }

        if (this.subScreenListener != null) {
            this.subScreenListener.render(partialTicks);
        }
    }

    @Override
    public void mouseMove(int mouseX, int mouseY) {
        if (subScreenListener == null || subScreenListener.isInteractLastScreen) {
            this.components.forEach(it -> it.mouseMove(mouseX, mouseY));
        }
        if (subScreenListener != null) {
            subScreenListener.mouseMove(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (subScreenListener == null || subScreenListener.isInteractLastScreen) {
            this.components.forEach(it -> it.mouseClicked(mouseX, mouseY, this.eventButton));
        }
        if (subScreenListener != null) {
            subScreenListener.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (subScreenListener == null || subScreenListener.isInteractLastScreen) {
            this.components.forEach(it -> it.mouseReleased(mouseX, mouseY, mouseButton));
        }
        if (subScreenListener != null) {
            subScreenListener.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        if (subScreenListener == null || subScreenListener.isInteractLastScreen) {
            this.components.forEach(it -> it.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick));
        }
        if (subScreenListener != null) {
            subScreenListener.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (subScreenListener == null || subScreenListener.isInteractLastScreen) {
            this.components.forEach(it -> it.keyTyped(typedChar, keyCode));
        }
        if (subScreenListener != null) {
            subScreenListener.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void tick() {
        this.components.forEach(Component::tick);
        if (subScreenListener != null) {
            subScreenListener.tick();
            subScreenListener.width = this.width;
            subScreenListener.height = this.height;
        }
    }

    public void onCloseSubScreen(SubScreenListener subScreen) {

    }

    /* Components management */

    public final List<Component> components = new ArrayList<Component>();

    public <T extends Component> void add(T component) {
        components.add(component);
    }

    public <T extends Component> void remove(T component) {
        components.remove(component);
    }

    public <T extends Component> List<T> getComponentsByType(Class<T> componentType) {
        return components.stream()
                .filter(componentType::isInstance)
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }


    public SubScreenListener subScreenListener = null;

    public void subscreen(SubScreenListener subScreenListener) {
        if (this.subScreenListener != null) {
            if (this.subScreenListener == subScreenListener) {
                return;
            }
            this.subScreenListener.close();
        }

        if (subScreenListener == null) {
            this.subScreenListener = null;
        } else {
            this.subScreenListener = subScreenListener;
            this.subScreenListener.init();
        }
    }

    /* Input manipulation */

    private int windowWidth, windowHeight;
    private double scaleWidth, scaleHeight;

    final public void updateResolution(JNE jne, int width, int height) {
        this.jne = jne;
        this.windowWidth = width;
        this.windowHeight = height;

        DisplayMode displaymode = Display.getDesktopDisplayMode();
        this.width = displaymode.getWidth();
        this.height = displaymode.getHeight();

        this.scaleWidth = (float) this.width / windowWidth;
        this.scaleHeight = (float) this.height / windowHeight;
    }

    final public void input() {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.handleMouseInput();
            }
        }

        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.handleKeyboardInput();
            }
        }
    }

    final public void handleMouseInput() {
        int x = (int) ((Mouse.getEventX()) * scaleWidth);
        int y = (int) ((this.windowHeight - Mouse.getEventY()) * scaleHeight) - ((int) (scaleHeight));
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

    final public void handleKeyboardInput() {
        char character = Keyboard.getEventCharacter();

        if (Keyboard.getEventKey() == 0 && character >= ' ' || Keyboard.getEventKeyState()) {
            int eventKey = Keyboard.getEventKey();
            this.keyTyped(character, eventKey);
        }

    }

}
