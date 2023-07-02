package jne.scenemaker.screens;

import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.EventPriority;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.Texture;
import jne.engine.utils.MouseClickType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScreenCreator extends ComponentsListener {

    public final List<Component> sceneComponents = new ArrayList<Component>();

    Texture resize;
    Texture move;

    @Override
    public void init() {
        this.resize = new Texture(new File(ENGINE.assetsDir.getAbsoluteFile() + "/resize.png"));
        this.move = new Texture(new File(ENGINE.assetsDir.getAbsoluteFile() + "/move.png"));

        add(GRAPHICS.button()
                .area(new Area(100, 100, 1, 32, 32))
                .texture(resize)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Resize");
                })
                .onPress((component, type) -> {
                    if (type == MouseClickType.RELEASED) {
                        subscreen(new ButtonSettingsScreen());
                    }
                })
                .build());

        add(GRAPHICS.button()
                .area(new Area(132, 100, 1, 32, 32))
                .texture(move)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Move");
                })
                .build());

        add(GRAPHICS.texture().area(new Area(200, 200, -3, 256, 256)).texture(move).build());

        super.init();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void render(ScreenEvent.Render event) {
        float partialTick = event.getPartialTick();
        render(partialTick);
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
