package jne.editor.nodes;

import jne.engine.api.ISerializable;
import jne.engine.constants.EventPriority;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.listeners.ComponentsListener;
import org.json.JSONObject;

public class NodeHandler extends ComponentsListener implements ISerializable {

    protected final int Z_LEVEL = -500;

    public final NodeStorage storage;
    public final NodeEditor node;

    public NodeHandler(NodeEditor node) {
        this.storage = new NodeStorage(this);
        this.node = node;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void render(ScreenEvent.Render event) {
        super.render(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void input(ScreenEvent.MouseInput event) {
        super.input(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void move(ScreenEvent.MouseMove event) {
        super.move(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void keyboard(ScreenEvent.Keyboard event) {
        super.keyboard(event);
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("storage", storage.toJson());

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        JSONObject storageJson = json.getJSONObject("storage");
        storage.fromJson(storageJson);
    }

}
