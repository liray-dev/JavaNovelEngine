package jne.scenemaker.screens.components;

import jne.engine.constants.EventPriority;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.ComponentBuilderHelper;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.TextureContainer;
import jne.scenemaker.screens.main.SceneMakerScreen;

import java.awt.*;

public class AddComponentScreen extends ComponentsListener {

    private final int Z_LEVEL = 1;

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
                .area(new Area(area.x2 - 55, area.y + 5, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("exit"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        kill();
                    }
                })
                .build());


        add(GRAPHICS.label()
                .area(new Area(area.x + 10, area.y + 5, area.z, 1, 1))
                .text("Select the component you want to add to the screen")
                .size(0.5F)
                .build());

        Area button = new Area(area.x + 10, area.y + 20, Z_LEVEL, 150, 50);
        add(GRAPHICS.button()
                .id(1)
                .area(button)
                .color(toolColor)
                .label(GRAPHICS.label().text("Button").centered(true).build(), true)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        ComponentBuilderHelper builder = new ComponentBuilderHelper(GRAPHICS.button().self());
                        openSubscreen(new SettingComponentScreen(builder, this.area));
                    }
                })
                .build());

        Area label = button.offset(-150, 5);

        add(GRAPHICS.button()
                .id(2)
                .area(label)
                .color(toolColor)
                .label(GRAPHICS.label().text("Label").centered(true).build(), true)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        ComponentBuilderHelper builder = new ComponentBuilderHelper(GRAPHICS.label().self());
                        openSubscreen(new SettingComponentScreen(builder, this.area));
                    }
                })
                .build());

        Area texture = label.offset(-150, 5);

        add(GRAPHICS.button()
                .id(3)
                .area(texture)
                .color(toolColor)
                .label(GRAPHICS.label().text("Texture").centered(true).build(), true)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        ComponentBuilderHelper builder = new ComponentBuilderHelper(GRAPHICS.texture().self());
                        openSubscreen(new SettingComponentScreen(builder, this.area));
                    }
                })
                .build());
    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH)
    public void render(ScreenEvent.Render event) {
        float partialTick = event.getPartialTick();

        RENDER.color(new Color(0f, 0f, 0f, 0.5F), () -> {
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
        this.keyTyped(event.getCharacter(), event.getButton(), event.getType());
    }

    @SubscribeEvent(priority = EventPriority.VERY_HIGH)
    public void tick(ScreenEvent.Tick event) {
        this.tick();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onCloseSubScreen(ScreenEvent.Close event) {
        if (event.getScreen().getClass().equals(SettingComponentScreen.class)) {
            SettingComponentScreen screen = (SettingComponentScreen) event.getScreen();



        }
    }

}
