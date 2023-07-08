package jne.engine.screens.components;


import jne.engine.constants.EnumScriptType;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScriptEvent;
import jne.engine.scripts.ScriptContainer;
import jne.engine.utils.*;

public class Component<SELF extends Component<SELF>> implements IComponentsListener, IComponent, IWrapper {

    public int id;
    public Area area;
    public ScriptContainer scriptContainer;

    protected int mouseX, mouseY;

    protected boolean active;
    protected boolean visible;
    protected boolean focused;

    protected boolean isTooltip = false;
    protected int ticks = 0;

    public IPressable<SELF> onPress;
    public IPressable<SELF> onFailPress;
    public ITooltip<SELF> onTooltip;

    protected Component() {
        this.id = -1;
        this.area = new Area();
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
        if (this.visible) {
            onRender(partialTicks);

            if (onTooltip != null && this.focused) {
                onTooltip.onTooltip(self(), mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    final public void mouseMove(int mouseX, int mouseY) {
        if (this.visible) {
            this.focused = checkFocus(mouseX, mouseY);
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
        if (this.visible) {
            boolean focused = checkFocus(mouseX, mouseY);
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
        if (this.visible) {
            boolean focused = checkFocus(mouseX, mouseY);
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
        if (this.visible) {
            boolean focused = checkFocus(mouseX, mouseY);
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
        if (this.visible && this.active) {
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

        onTick();
    }

    @Override
    final public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }

    @Override
    final public void setArea(Area area) {
        this.area = area;
    }

    @Override
    final public Area getArea() {
        return this.area;
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
    public void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime) {

    }

    @Override
    public void onKeyTyped(int keyCode, char typedChar, KeyboardType type) {

    }

    @Override
    public void onTick() {

    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Component<T>> extends ComponentBuilder<SELF, T> {

        protected T create() {
            return (T) new Component();
        }

        @ComponentConstructor(text = "ID", example = "example: 1")
        public SELF id(int id) {
            instance().id = id;
            return self();
        }

        public SELF visible(boolean flag) {
            instance().visible = flag;
            return self();
        }

        public SELF area(Area area) {
            instance().area = area;
            return self();
        }

        @ComponentConstructor(text = "Script", example = "example: code...")
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

    }

}
