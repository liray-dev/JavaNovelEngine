package jne.engine.screens.components;


import jne.engine.utils.*;

public class Component<SELF extends Component<SELF>> implements IComponentsListener, IComponent, IWrapper {

    protected Area area;

    protected int mouseX, mouseY;
    protected int mouseOffsetX, mouseOffsetY;

    protected boolean active;
    protected boolean visible;
    protected boolean focused;

    protected Component() {
        this.area = new Area();
        this.visible = true;
        this.active = false;
        this.focused = false;
    }

    /*
        Interface realization
     */

    public boolean checkFocus(float mouseX, float mouseY) {
        return area.onArea(mouseX, mouseY);
    }

    @Override
    public void render(float partialTicks) {
        if (this.visible) {
            onRender(partialTicks);
        }
    }

    @Override
    public void mouseMove(int mouseX, int mouseY) {
        if (this.visible) {
            this.focused = checkFocus(mouseX, mouseY);
            if (this.focused) {
                onFocused(mouseX, mouseY);
            }
        }

        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.visible) {
            boolean focused = checkFocus(mouseX, mouseY);
            if (focused) {
                this.active = true;
                this.mouseOffsetX = (int) (mouseX - this.area.x);
                this.mouseOffsetY = (int) (mouseY - this.area.y);
                onClicked(mouseX, mouseY, mouseButton, MouseClickType.CLICKED);
            } else {
                this.active = false;
                onFailClick(mouseX, mouseY, mouseButton, MouseClickType.CLICKED);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (this.visible) {
            boolean focused = checkFocus(mouseX, mouseY);
            if (focused) {
                this.active = true;
                onClicked(mouseX, mouseY, mouseButton, MouseClickType.RELEASED);
            } else {
                this.active = false;
                onFailClick(mouseX, mouseY, mouseButton, MouseClickType.RELEASED);
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
            } else {
                this.active = false;
                onFailClick(mouseX, mouseY, mouseButton, MouseClickType.MOVED);
            }
        }
    }

    @Override
    final public void keyTyped(char typedChar, int keyCode) {
        if (this.visible && this.active) {
            onKeyTyped(keyCode, typedChar, KeyboardType.START);
        }
    }

    /*
        Overwrite realization
     */

    @Override
    public void tick() {

    }

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
    public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }

    @Override
    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public Area getArea() {
        return this.area;
    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Component<T>> extends ComponentBuilder<SELF, T> {

        protected T create() {
            return (T) new Component();
        }

        public SELF area(Area area) {
            instance().area = area;
            return self();
        }

    }

}
