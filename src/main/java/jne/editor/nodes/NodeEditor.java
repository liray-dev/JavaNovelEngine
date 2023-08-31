package jne.editor.nodes;

import jne.editor.EditorCore;
import jne.editor.scenes.SceneUnit;
import jne.editor.utils.EditingTypes;
import jne.editor.utils.NodeMovableComponent;
import jne.engine.constants.EventPriority;
import jne.engine.constants.MouseClickType;
import jne.engine.events.EventListenerHelper;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.screens.widgets.Button;
import jne.engine.screens.widgets.Component;
import jne.engine.api.ISerializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static jne.engine.constants.Colors.*;

public class NodeEditor extends ComponentsListener implements ISerializable {

    public final int Z_LEVEL = 0;
    public final NodeHandler handler;

    public NodeMovableComponent nodeMovableComponent = new NodeMovableComponent(this);

    public EditingTypes currentEditingType = EditingTypes.NONE;
    public Component lastClickedComponent = null;
    public String currentToolID;

    public NodeEditor() {
        this.handler = new NodeHandler(this);
    }

    @Override
    public void init() {
        EventListenerHelper.register(this.handler);
        EventListenerHelper.register(this.handler.storage);

        add(GRAPHICS.button()
                .id("create")
                .area(new Area(5, 5, Z_LEVEL, 50, 50))
                .texture(EditorCore.selection)
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentEditingType = EditingTypes.NODE;
                        nodeMovableComponent.setType(currentEditingType);
                        currentToolID = component.getID();
                        recreate();
                    }
                })
                .build());
    }

    @Override
    public void close() {

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
        this.nodeMovableComponent.render(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void input(ScreenEvent.MouseInput event) {
        super.input(event);
        this.nodeMovableComponent.input(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void clickMove(ScreenEvent.MouseClickMove event) {
        super.clickMove(event);
        this.nodeMovableComponent.clickMove(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void move(ScreenEvent.MouseMove event) {
        super.move(event);
        this.nodeMovableComponent.move(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void keyboard(ScreenEvent.Keyboard event) {
        super.keyboard(event);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
        this.nodeMovableComponent.tick(event);
    }

    public HashMap<String, SceneUnit> scenes = new HashMap<>();

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        scenes.forEach((name, scene) -> {
            json.put(name, scene.toJson());
        });

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        scenes.clear();

        for (String key : json.keySet()) {
            JSONObject sceneJson = json.getJSONObject(key);

            SceneUnit scene = new SceneUnit(this);
            scene.fromJson(sceneJson);

            scenes.put(key, scene);
        }
    }

}
