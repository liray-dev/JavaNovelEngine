package jne.sceneeditor.screens.components;

import jne.engine.constants.EventPriority;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.texture.TextureContainer;
import jne.sceneeditor.screens.SceneEditorScreen;
import jne.sceneeditor.screens.components.settings.SettingButtonScreen;
import jne.sceneeditor.screens.components.settings.SettingLabelScreen;
import jne.sceneeditor.screens.components.settings.SettingTextureScreen;

import java.awt.*;

public class AddComponentScreen extends ComponentsListener {

    private final int Z_LEVEL = 15;

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
                        openSubscreen(new SettingButtonScreen(this.area));
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
                        openSubscreen(new SettingLabelScreen(this.area));
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
                        openSubscreen(new SettingTextureScreen(this.area));
                    }
                })
                .build());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void render(ScreenEvent.Render event) {
        RENDER.color(new Color(0f, 0f, 0f, 0.5F), () -> {
            RENDER.drawQuad(0, 0, Z_LEVEL, width, height);
        });

        RENDER.color(barColor, () -> {
            RENDER.drawQuad(area.x, area.y, Z_LEVEL, area.x2, area.y2);
        });

        super.render(event.getPartialTick());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, exclusion = SceneEditorScreen.class)
    public void input(ScreenEvent.MouseInput event) {
        super.input(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void keyboard(ScreenEvent.Keyboard event) {
        super.keyboard(event);
    }

}
