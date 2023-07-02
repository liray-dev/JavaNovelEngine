package jne.scenemaker.screens;

import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.EventPriority;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.Texture;
import jne.engine.utils.MouseClickType;

import java.io.File;

public class ButtonSettingsScreen extends ComponentsListener {

    Texture resize;
    Texture move;

    @Override
    public void init() {
        this.resize = new Texture(new File(ENGINE.assetsDir.getAbsoluteFile() + "/resize.png"));
        this.move = new Texture(new File(ENGINE.assetsDir.getAbsoluteFile() + "/move.png"));

        add(GRAPHICS.button()
                .area(new Area(164, 100, 1, 64, 64))
                .texture(resize)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Resize3");
                })
                .onPress((component, type) -> {
                    if (type == MouseClickType.RELEASED) {
                        subscreen(new ButtonSettingsScreen2());
                    }
                })
                .build());

        add(GRAPHICS.button()
                .area(new Area(228, 100, 1, 64, 64))
                .texture(move)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Move3");
                })
                .build());

        add(GRAPHICS.texture().area(new Area(200, 200, -3, 256, 256)).texture(move).build());

        super.init();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, exclusion = {ScreenCreator.class})
    public void render(ScreenEvent.Render event) {
        float partialTick = event.getPartialTick();
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
