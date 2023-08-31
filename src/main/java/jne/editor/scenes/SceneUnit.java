package jne.editor.scenes;

import jne.editor.nodes.NodeEditor;
import jne.editor.utils.EditingTypes;
import jne.engine.api.ISerializable;
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
import org.json.JSONObject;

import java.util.List;

import static jne.engine.constants.Colors.*;

public class SceneUnit extends ComponentsListener implements ISerializable {

    protected final int Z_LEVEL = 0;

    public final FrameHandler frameHandler;
    private final NodeEditor editor;

    public EditingTypes currentEditingType = EditingTypes.NONE;
    public String currentToolID;


    public SceneUnit(NodeEditor editor) {
        this.editor = editor;
        this.frameHandler = new FrameHandler(this);
    }

    @Override
    public void init() {
        EventListenerHelper.register(this.frameHandler);
        EventListenerHelper.register(this.frameHandler.storage);

        add(GRAPHICS.button()
                .id("move")
                .area(new Area(5, 5, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("move"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.MOVE;
                        currentToolID = component.getID();
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
                        currentToolID = component.getID();
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
                        currentToolID = component.getID();
                        recreate();
                        System.out.println(toJson());
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
                        currentToolID = component.getID();
                        this.frameHandler.storage.selectComponent(null);
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id("back")
                .area(new Area(5, WINDOW.displayHeight - 50, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("back"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        WINDOW.screenManager.setScreen(editor);
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
        List<Button> buttons = getComponentsByID(currentToolID, Button.class);
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
            currentToolID = "move";
            recreate();
        }
        if (button == Hotkeys.resizeKey) {
            currentEditingType = EditingTypes.RESIZE;
            currentToolID = "resize";
            recreate();
        }
        if (button == Hotkeys.zoomKey) {
            currentEditingType = EditingTypes.ZOOM;
            currentToolID = "zoom";
            recreate();
        }
        if (button == Hotkeys.addKey) {
            openSubscreen(new AddComponentScreen());
            currentToolID = "add";
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("frameHandler", frameHandler.toJson());

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        frameHandler.fromJson(json.getJSONObject("frameHandler"));
    }

}
