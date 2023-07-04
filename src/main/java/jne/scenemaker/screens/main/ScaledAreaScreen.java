package jne.scenemaker.screens.main;

import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.EventPriority;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.utils.MouseClickType;

import java.awt.*;

public class ScaledAreaScreen extends ComponentsListener {


    public ScaledAreaScreen() {

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void render(ScreenEvent.Render event) {
        RENDER.color(Color.DARK_GRAY, () -> {
            RENDER.drawQuad(0, 0, 1, 60, height);
            RENDER.drawQuad(0, height, 1, width, height - 60);
        });

        render(event.getPartialTick());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void move(ScreenEvent.MouseMove event) {
        this.mouseMove(event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void input(ScreenEvent.MouseInput event) {
        MouseClickType type = event.getType();
        if (type == MouseClickType.CLICKED) {
            this.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
        }
        if (type == MouseClickType.RELEASED) {
            this.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void clickMove(ScreenEvent.MouseClickMove event) {
        this.mouseClickMove(event.getMouseX(), event.getMouseY(), event.getButton(), event.getLast());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void keyboard(ScreenEvent.Keyboard event) {
        this.keyTyped(event.getCharacter(), event.getButton());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void tick(ScreenEvent.Tick event) {
        this.tick();
    }

}
