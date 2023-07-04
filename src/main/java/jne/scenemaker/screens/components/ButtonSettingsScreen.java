package jne.scenemaker.screens.components;

import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.EventPriority;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.Texture;
import jne.engine.texture.TextureContainer;
import jne.engine.utils.MouseClickType;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;

public class ButtonSettingsScreen extends ComponentsListener {

    @Override
    public void init() {

        add(GRAPHICS.button()
                .area(new Area(164, 100, 10, 64, 64))
                .texture(TextureContainer.get("resize"))
                .label(GRAPHICS.label().size(1F).text("Ку!\nЧд?\n").centered(true).build(), true)
                .onPress((component, type) -> {

                })
                .build());

        add(GRAPHICS.button()
                .area(new Area(228, 100, 10, 64, 64))
                .texture(TextureContainer.get("move"))
                .build());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void render(ScreenEvent.Render event) {
        float partialTick = event.getPartialTick();

        RENDER.color(new Color(0f, 0f, 0f, 0.85F), () -> {
            RENDER.drawQuad(0,0, 1, width, height);
        });

        RENDER.color(new Color(0.5f, 0.5f, 0.5f, 1F), () -> {
            float x = width / 2F;
            float y = height / 2F;
            RENDER.drawQuad(x - 200, y - 250, 2, x + 200, y + 250);
        });

        render(partialTick);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void move(ScreenEvent.MouseMove event) {
        this.mouseMove(event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void input(ScreenEvent.MouseInput event) {
        MouseClickType type = event.getType();
        if (type == MouseClickType.CLICKED) {
            this.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
        }
        if (type == MouseClickType.RELEASED) {
            this.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton());
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void clickMove(ScreenEvent.MouseClickMove event) {
        this.mouseClickMove(event.getMouseX(), event.getMouseY(), event.getButton(), event.getLast());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void keyboard(ScreenEvent.Keyboard event) {
        this.keyTyped(event.getCharacter(), event.getButton());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void tick(ScreenEvent.Tick event) {
        this.tick();
    }

}
