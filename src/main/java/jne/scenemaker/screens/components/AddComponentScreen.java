package jne.scenemaker.screens.components;

import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.EventPriority;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.TextureContainer;
import jne.engine.utils.MouseClickType;
import jne.scenemaker.screens.main.SceneMakerScreen;
import jne.scenemaker.utils.EditingTypes;

import java.awt.*;

public class AddComponentScreen extends ComponentsListener {

    private final int Z_LEVEL = 1;

    private final Color clickedToolColor = new Color(0x525252);
    private final Color toolColor = new Color(0x383838);
    private final Color barColor = new Color(0x181818);

    private Area area = new Area();

    @Override
    public void init() {
        int width = WINDOW.desktop.getWidth();
        int height = WINDOW.desktop.getHeight();

        this.area = new Area(120, 120, width - 120, height - 120, Z_LEVEL, false);

        add(GRAPHICS.button()
                .id(0)
                .area(new Area(area.x2 - 50, area.y, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("exit"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        kill();
                    }
                })
                .build());

    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH)
    public void render(ScreenEvent.Render event) {
        float partialTick = event.getPartialTick();

        RENDER.color(new Color(0f, 0f, 0f, 0.8F), () -> {
            RENDER.drawQuad(0, 0, Z_LEVEL, width, height);
        });

        RENDER.color(barColor, () -> {
            RENDER.drawQuad(area.x, area.y, Z_LEVEL, area.x2, area.y2);
        });

        render(partialTick);
    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH)
    public void move(ScreenEvent.MouseMove event) {
        this.mouseMove(event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH, exclusion = SceneMakerScreen.class)
    public void input(ScreenEvent.MouseInput event) {
        MouseClickType type = event.getType();
        if (type == MouseClickType.CLICKED) {
            this.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
        }
        if (type == MouseClickType.RELEASED) {
            this.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton());
        }
    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH)
    public void clickMove(ScreenEvent.MouseClickMove event) {
        this.mouseClickMove(event.getMouseX(), event.getMouseY(), event.getButton(), event.getLast());
    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH)
    public void keyboard(ScreenEvent.Keyboard event) {
        this.keyTyped(event.getCharacter(), event.getButton());
    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH)
    public void tick(ScreenEvent.Tick event) {
        this.tick();
    }

}
