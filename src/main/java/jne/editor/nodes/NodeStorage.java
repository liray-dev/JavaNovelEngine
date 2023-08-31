package jne.editor.nodes;

import jne.engine.api.ISerializable;
import jne.engine.constants.EventPriority;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.listeners.ComponentsListener;
import org.json.JSONObject;

public class NodeStorage extends ComponentsListener implements ISerializable {

    protected final int Z_LEVEL = -1000;

    public final NodeHandler handler;

    public NodeStorage(NodeHandler handler) {
        this.handler = handler;
    }

    @Override
    public void init() {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void render(ScreenEvent.Render event) {
        super.render(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void input(ScreenEvent.MouseInput event) {
        super.input(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void clickMove(ScreenEvent.MouseClickMove event) {
        super.clickMove(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void move(ScreenEvent.MouseMove event) {
        super.move(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void wheel(ScreenEvent.Wheel event) {
        super.wheel(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void keyboard(ScreenEvent.Keyboard event) {
        super.keyboard(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onCloseSubScreen(ScreenEvent.Close event) {

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {

    }

}
