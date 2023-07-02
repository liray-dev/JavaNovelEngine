package jne.scenemaker.screens;

import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.EventPriority;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.Texture;
import jne.engine.utils.MouseClickType;

import java.io.File;

public class ButtonSettingsScreen2 extends ComponentsListener {
    
    Texture resize;
    Texture move;

    @Override
    public void init() {
        this.resize = new Texture(new File(ENGINE.assetsDir.getAbsoluteFile() + "/resize.png"));
        this.move = new Texture(new File(ENGINE.assetsDir.getAbsoluteFile() + "/move.png"));

        add(GRAPHICS.button()
                .area(new Area(292, 100, 5, 128, 128))
                .texture(resize)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Resize2");
                })
                .onPress((component, type) -> {
                    if (type == MouseClickType.RELEASED) {
                        killAll();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .area(new Area(420, 100, 5, 128, 128))
                .texture(move)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Move2");
                })
                .build());

        add(GRAPHICS.texture().area(new Area(200, 200, -3, 256, 256)).texture(move).build());

        super.init();
    }

    @SubscribeEvent(priority = EventPriority.LOW, exclusion = {ScreenCreator.class, ButtonSettingsScreen.class})
    public void render(ScreenEvent.Render event) {
        float partialTick = event.getPartialTick();
        render(partialTick);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void move(ScreenEvent.MouseMove event) {
        this.mouseMove(event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void input(ScreenEvent.MouseInput event) {
        MouseClickType type = event.getType();
        if (type == MouseClickType.CLICKED) {
            this.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
        }
        if (type == MouseClickType.RELEASED) {
            this.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void clickMove(ScreenEvent.MouseClickMove event) {
        this.mouseClickMove(event.getMouseX(), event.getMouseY(), event.getButton(), event.getLast());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void keyboard(ScreenEvent.Keyboard event) {
        this.keyTyped(event.getCharacter(), event.getButton());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void tick(ScreenEvent.Tick event) {
        this.tick();
    }

}
