package jne.editor.scenes;

import jne.editor.utils.EditingTypes;
import jne.engine.constants.EventPriority;
import jne.engine.constants.Hotkeys;
import jne.engine.constants.MouseClickType;
import jne.engine.events.EventListenerHelper;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.screens.widgets.Button;
import jne.engine.texture.TextureContainer;

import java.util.List;

import static jne.engine.constants.Colors.*;

public class SceneEditor extends ComponentsListener {

    protected final int Z_LEVEL = 0;

    public final FrameHandler frameHandler;

    public EditingTypes currentEditingType = EditingTypes.NONE;
    public String currentButton = "";


    public SceneEditor() {
        this.frameHandler = new FrameHandler(this);
        EventListenerHelper.register(this.frameHandler);
        EventListenerHelper.register(this.frameHandler.storage);
    }

    @Override
    public void init() {
        add(GRAPHICS.button()
                .id("move")
                .area(new Area(5, 5, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("move"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.MOVE;
                        currentButton = component.getID();
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id("resize")
                .area(new Area(5, 60, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("resize"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.RESIZE;
                        currentButton = component.getID();
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id("zoom")
                .area(new Area(5, 115, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("zoom"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.ZOOM;
                        currentButton = component.getID();
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id("add")
                .area(new Area(5, 170, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("add"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        openSubscreen(new AddComponentScreen());
                        currentButton = component.getID();
                        this.frameHandler.storage.selectComponent(null);
                        recreate();
                    }
                })
                .build());

        frameHandler.init();

    }

    @Override
    public void close() {
        EventListenerHelper.unregister(this.frameHandler);
        EventListenerHelper.unregister(this.frameHandler.storage);
    }

    @Override
    public void update() {
        List<Button> buttons = getComponentsByID(currentButton, Button.class);
        buttons.forEach(it -> it.color = clickedToolColor);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void render(ScreenEvent.Render event) {
        RENDER.color(brightBarColor, () -> {
            RENDER.drawQuad(0, 0, Z_LEVEL, 60, height);
        });
        super.render(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void input(ScreenEvent.MouseInput event) {
        super.input(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void keyboard(ScreenEvent.Keyboard event) {
        int button = event.getButton();
        if (button == Hotkeys.moveKey) {
            currentEditingType = EditingTypes.MOVE;
            currentButton = "move";
            recreate();
        }
        if (button == Hotkeys.resizeKey) {
            currentEditingType = EditingTypes.RESIZE;
            currentButton = "resize";
            recreate();
        }
        if (button == Hotkeys.zoomKey) {
            currentEditingType = EditingTypes.ZOOM;
            currentButton = "zoom";
            recreate();
        }
        if (button == Hotkeys.addKey) {
            openSubscreen(new AddComponentScreen());
            currentButton = "add";
            this.frameHandler.storage.selectComponent(null);
            recreate();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.frameHandler.resize(width, height);
        this.frameHandler.storage.resize(width, height);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
    }
}
