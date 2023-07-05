package jne.scenemaker.screens.main;

import jne.engine.events.types.ScreenEvent;
import jne.engine.constants.EventPriority;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.screens.widgets.Button;
import jne.engine.texture.TextureContainer;
import jne.engine.constants.MouseClickType;
import jne.scenemaker.screens.components.AddComponentScreen;
import jne.scenemaker.utils.EditingTypes;

import java.awt.*;
import java.util.List;

public class SceneMakerScreen extends ComponentsListener {

    private final int Z_LEVEL = 0;

    public int currentButton = Integer.MIN_VALUE;
    public EditingTypes currentEditingType = EditingTypes.NONE;

    private final Color clickedToolColor = new Color(0x525252);
    private final Color toolColor = new Color(0x383838);
    private final Color barColor = new Color(0x262626);

    @Override
    public void init() {
        add(GRAPHICS.button()
                .id(0)
                .area(new Area(5, 5, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("move"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.MOVE;
                        currentButton = component.id;
                        recreate();
                        System.out.println("Click");
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id(1)
                .area(new Area(5, 60, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("resize"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.RESIZE;
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id(2)
                .area(new Area(5, 115, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("zoom"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.ZOOM;
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id(3)
                .area(new Area(5, 170, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("add"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        openSubscreen(new AddComponentScreen());
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void render(ScreenEvent.Render event) {
        RENDER.color(barColor, () -> {
            RENDER.drawQuad(0, 0, Z_LEVEL, 60, height);
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
        this.keyTyped(event.getCharacter(), event.getButton(), event.getType());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void tick(ScreenEvent.Tick event) {
        this.tick();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onCloseSubScreen(ScreenEvent.Close event) {

    }

    @Override
    public void update() {
        List<Button> buttons = getComponentsByID(currentButton, Button.class);
        buttons.forEach(it -> it.color = clickedToolColor);
    }

}
