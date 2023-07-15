package jne.editor.components;

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
import jne.editor.utils.EditingTypes;
import java.util.List;

import static jne.engine.constants.Colors.*;

public class SceneEditor extends ComponentsListener {

    protected final int Z_LEVEL = 0;

    public final ComponentStore componentStore;
    public final FrameHandler frameHandler;

    public EditingTypes currentEditingType = EditingTypes.NONE;
    public int currentButton = Integer.MIN_VALUE;


    public SceneEditor() {
        this.componentStore = new ComponentStore(this);
        this.frameHandler = new FrameHandler(this);
        EventListenerHelper.register(this.componentStore);
        EventListenerHelper.register(this.frameHandler);
    }

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
                        this.componentStore.selectComponent(null);
                        recreate();
                    }
                })
                .build());

        frameHandler.init();

    }

    @Override
    public void close() {
        EventListenerHelper.unregister(this.componentStore);
        EventListenerHelper.unregister(this.frameHandler);
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
            currentButton = 0;
            recreate();
        }
        if (button == Hotkeys.resizeKey) {
            currentEditingType = EditingTypes.RESIZE;
            currentButton = 1;
            recreate();
        }
        if (button == Hotkeys.zoomKey) {
            currentEditingType = EditingTypes.ZOOM;
            currentButton = 2;
            recreate();
        }
        if (button == Hotkeys.addKey) {
            openSubscreen(new AddComponentScreen());
            currentButton = 3;
            this.componentStore.selectComponent(null);
            recreate();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.componentStore.resize(width, height);
        this.frameHandler.resize(width, height);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
    }
}
