package jne.engine.screens.listeners;

import jne.engine.screens.components.Component;
import org.lwjgl.input.Keyboard;

public class SubScreenListener extends ScreenListener {

    public ScreenListener lastScreen;

    public boolean isRenderLastScreen;
    public boolean isInteractLastScreen;

    public SubScreenListener(ScreenListener screen) {
        this.lastScreen = screen;
        this.isRenderLastScreen = true;
        this.isInteractLastScreen = false;
        this.width = screen.width;
        this.height = screen.height;
    }

    public SubScreenListener(ScreenListener screen, boolean render, boolean interact) {
        this.lastScreen = screen;
        this.isRenderLastScreen = render;
        this.isInteractLastScreen = interact;
        this.width = screen.width;
        this.height = screen.height;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        boolean ESC = keyCode == Keyboard.KEY_ESCAPE;
        if (ESC && this.subScreenListener == null){
            death();
            return;
        }
        if (subScreenListener != null) {
            boolean isInteract = subScreenListener.isInteractLastScreen;
            subScreenListener.keyTyped(typedChar, keyCode);
            if (!isInteract) return;
        }
        this.components.forEach(component -> component.keyTyped(typedChar, keyCode));
    }

    public void death() {
        this.lastScreen.onCloseSubScreen(this);
        this.components.forEach(Component::close);
        this.lastScreen.subscreen(null);
    }

}
