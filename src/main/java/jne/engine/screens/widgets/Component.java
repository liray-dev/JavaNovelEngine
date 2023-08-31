package jne.engine.screens.widgets;


import jne.engine.api.IComponent;
import jne.engine.api.IPressable;
import jne.engine.api.ITooltip;
import jne.engine.api.IWrapper;
import jne.engine.constants.EnumScriptType;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.debug.DebugManager;
import jne.engine.events.types.ScriptEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.ComponentBuilder;
import jne.engine.scripts.ScriptContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.UUID;

public class Component<SELF extends Component<SELF>> implements IComponent, IWrapper {

    protected String id;

    protected Area area;
    protected Area initialArea;
    protected float depth;

    protected ScriptContainer scriptContainer;

    protected int mouseX, mouseY;

    protected boolean active;
    protected boolean visible;
    protected boolean focused;

    protected boolean markDirty = false;

    @Nullable
    protected IComponent parent;

    protected boolean isTooltip = false;
    protected int ticks = 0;

    public IPressable<SELF> onPress;
    public IPressable<SELF> onFailPress;
    public ITooltip<SELF> onTooltip;

    protected Component() {
        this.id = UUID.randomUUID().toString();
        this.area = new Area();
        this.initialArea = area;
        this.depth = area.z;
        this.scriptContainer = new ScriptContainer();
        this.visible = true;
        this.active = false;
        this.focused = false;
    }

    /*
        Interface realization
     */

    final public boolean checkFocus(float mouseX, float mouseY) {
        return area.onArea(mouseX, mouseY);
    }

    @Override
    final public void render(float partialTicks) {
        if (visible()) {
            onRender(partialTicks);

            if (onTooltip != null && this.focused) {
                onTooltip.onTooltip(self(), mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    final public void mouseMove(int mouseX, int mouseY) {
        if (visible()) {
            focused = checkFocus(mouseX, mouseY);
            if (this.focused) {
                onFocused(mouseX, mouseY);
                if (isTooltip) {
                    ScriptEvent.Tooltip tooltip = new ScriptEvent.Tooltip(this);
                    scriptContainer.run(EnumScriptType.TOOLTIP, tooltip);
                    tooltip.post();
                }
            }
        }

        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    final public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (visible()) {
            focused = checkFocus(mouseX, mouseY);
            if (focused) {
                this.active = true;
                onClicked(mouseX, mouseY, mouseButton, MouseClickType.CLICKED);

                if (onPress != null) {
                    onPress.onPress(self(), MouseClickType.CLICKED);
                }

                ScriptEvent.Press press = new ScriptEvent.Press(this);
                scriptContainer.run(EnumScriptType.PRESS, press);
                press.post();
            } else {
                this.active = false;
                onFailClick(mouseX, mouseY, mouseButton, MouseClickType.CLICKED);

                if (onFailPress != null) {
                    onFailPress.onPress(self(), MouseClickType.CLICKED);
                }

                ScriptEvent.FailPress failPress = new ScriptEvent.FailPress(this);
                scriptContainer.run(EnumScriptType.FAIL_PRESS, failPress);
                failPress.post();
            }
        }
    }

    @Override
    final public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (visible()) {
            focused = checkFocus(mouseX, mouseY);
            if (focused) {
                this.active = true;
                onClicked(mouseX, mouseY, mouseButton, MouseClickType.RELEASED);
                if (onPress != null) {
                    onPress.onPress(self(), MouseClickType.RELEASED);
                }
            } else {
                this.active = false;
                onFailClick(mouseX, mouseY, mouseButton, MouseClickType.RELEASED);

                if (onFailPress != null) {
                    onFailPress.onPress(self(), MouseClickType.RELEASED);
                }
            }
        }
    }

    @Override
    final public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        if (visible()) {
            focused = checkFocus(mouseX, mouseY);
            if (focused) {
                this.active = true;
                onMoveClick(mouseX, mouseY, mouseButton, timeSinceLastClick);
                if (onPress != null) {
                    onPress.onPress(self(), MouseClickType.MOVED);
                }
            } else {
                this.active = false;
                onFailClick(mouseX, mouseY, mouseButton, MouseClickType.MOVED);

                if (onFailPress != null) {
                    onFailPress.onPress(self(), MouseClickType.MOVED);
                }
            }
        }
    }

    @Override
    final public void keyTyped(char typedChar, int keyCode, KeyboardType type) {
        if (visible() && this.active) {
            onKeyTyped(keyCode, typedChar, type);
        }
    }

    @Override
    final public void tick() {
        ticks++;
        if (ticks > 20) {
            ticks = 0;
        }
        if (focused) {
            if (ticks == 20) {
                isTooltip = true;
            }
        } else {
            isTooltip = false;
        }

        ScriptEvent.Update update = new ScriptEvent.Update(this);
        scriptContainer.run(EnumScriptType.UPDATE, update);
        update.post();

        if (checkUpdates()) {
            this.markDirty = false;
            update();
        }

        onTick();
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public boolean visible() {
        return visible;
    }

    @Override
    public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }

    @Override
    public float getAbsoluteX() {
        return area.x2;
    }

    @Override
    public float getX() {
        return area.x;
    }

    @Override
    public void setX(float x) {
        area.move(x, area.y, 0, 0);
    }

    @Override
    public float getAbsoluteY() {
        return area.y2;
    }

    @Override
    public float getY() {
        return area.y;
    }

    @Override
    public void setY(float y) {
        this.area.move(area.x, y, 0, 0);
    }

    @Override
    public float getWidth() {
        return area.width;
    }

    @Override
    public void setWidth(float width) {
        area.width = width;
    }

    @Override
    public float getHeight() {
        return area.height;
    }

    @Override
    public void setHeight(float height) {
        area.height = height;
    }

    @Override
    final public void setArea(Area area) {
        this.area = area.clone();
    }

    @Override
    final public Area getArea() {
        return this.area;
    }

    final public Area getInitialArea() {
        return initialArea;
    }

    final public void setInitialArea(Area initialArea) {
        this.initialArea = initialArea;
    }

    @Override
    public int getDepth() {
        return (int) depth;
    }

    @Override
    public void setDepth(int value) {
        this.depth = value;
        this.area.z = value;
    }

    @Override
    public void markDirty() {
        this.markDirty = true;
    }

    @Override
    public boolean checkUpdates() {
        return markDirty;
    }

    @Override
    public void update() {

    }

    @Override
    public @Nullable IComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@NotNull IComponent component) {
        this.parent = component;
    }

    @Override
    public String getScript() {
        return scriptContainer.script;
    }

    @Override
    public void setScript(String script) {
        scriptContainer.script = script;
    }

    @Override
    public ScriptContainer getScriptContainer() {
        return scriptContainer;
    }

    private Object answer;

    @Override
    public void setAnswer(Object obj) {
        this.answer = obj;
    }

    @Override
    public Object getAnswer() {
        return answer;
    }

    @Override
    public void setOnPress(IPressable<?> press) {
        this.onPress = (IPressable<SELF>) press;
    }

    @Override
    public void setOnFailPress(IPressable<?> failPress) {
        this.onFailPress = (IPressable<SELF>) failPress;
    }

    @Override
    public void setOnTooltip(IPressable<?> tooltip) {
        this.onTooltip = (ITooltip<SELF>) tooltip;
    }

    @Override
    public boolean inFocus() {
        return focused;
    }

    public SELF self() {
        return (SELF) this;
    }

    @Override
    final public String getComponentType() {
        return this.getClass().getName();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("id", this.id);
        json.put("type", this.getComponentType());

        json.put("area", this.area.toJson());
        json.put("initialArea", this.initialArea.toJson());
        json.put("depth", this.depth);

        json.put("script", this.scriptContainer.script);

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        this.id = json.getString("id");

        this.area.fromJson(json.getJSONObject("area"));
        this.initialArea.fromJson(json.getJSONObject("initialArea"));
        this.depth = json.getFloat("depth");

        this.scriptContainer.script = json.getString("script");
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Component<T>> extends ComponentBuilder<SELF, T> {

        protected T create() {
            return (T) new Component();
        }

        public SELF id(String id) {
            instance().id = id;
            return self();
        }

        public SELF visible(boolean flag) {
            instance().visible = flag;
            return self();
        }

        public SELF depth(int depth) {
            instance().depth = depth;
            instance().area.z = depth;
            return self();
        }

        public SELF area(Area area) {
            instance().area = area.clone();
            instance().initialArea = area.clone();
            instance().depth = area.z;
            return self();
        }

        public SELF script(String code) {
            instance().scriptContainer.script = code;
            return self();
        }

        public SELF scriptContainer(ScriptContainer container) {
            instance().scriptContainer = container;
            return self();
        }

        public SELF onPress(IPressable<T> onPress) {
            instance().onPress = onPress;
            return self();
        }

        public SELF onFailPress(IPressable<T> onFailPress) {
            instance().onFailPress = onFailPress;
            return self();
        }

        public SELF onTooltip(ITooltip<T> onTooltip) {
            instance().onTooltip = onTooltip;
            return self();
        }

        public SELF answer(Object obj) {
            instance().setAnswer(obj);
            return self();
        }

    }
    /*
        Overwrite realization
     */

    @Override
    public void onRender(float partialTicks) {

    }

    @Override
    public void onFocused(int mouseX, int mouseY) {

    }

    @Override
    public void onClicked(int mouseX, int mouseY, int button, MouseClickType type) {

    }

    @Override
    public void onFailClick(int mouseX, int mouseY, int button, MouseClickType type) {

    }

    @Override
    public void onWheel(int mouseX, int mouseY, int value) {

    }

    @Override
    public void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime) {

    }

    @Override
    public void onKeyTyped(int keyCode, char typedChar, KeyboardType type) {

    }

    @Override
    public void onTick() {

    }

    public IComponent clone() {
        try {
            IComponent clone = (IComponent) super.clone();
            clone.setArea(area.clone());
            clone.setInitialArea(initialArea.clone());
            return clone;
        } catch (CloneNotSupportedException e) {
            DebugManager.error(e);
        }
        return null;
    }

}
