package jne.engine.screens.widgets;

import jne.engine.utils.IPressable;
import jne.engine.utils.ITooltip;
import jne.engine.utils.MouseClickType;

public class Button<SELF extends Button<SELF>> extends TexturedComponent<SELF> {

    public Label label;

    public IPressable<SELF> onPress;
    public IPressable<SELF> onFailPress;
    public ITooltip<SELF> onTooltip;

    @Override
    public void onRender(float partialTicks) {
        super.onRender(partialTicks);

        if (label != null && !label.text.isEmpty()) {
            label.onRender(partialTicks);
        }

        if (onTooltip != null && this.focused) {
            onTooltip.onTooltip(self(), mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int button, MouseClickType type) {
        if (onPress != null) {
            onPress.onPress(self(), type);
        }
    }

    @Override
    public void onFailClick(int mouseX, int mouseY, int button, MouseClickType type) {
        if (onFailPress != null) {
            onFailPress.onPress(self(), type);
        }
    }

    @Override
    public void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime) {
        area.move(mouseX, mouseY, mouseOffsetX, mouseOffsetY);
    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Button<T>> extends TexturedComponent.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new Button();
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

        public SELF label(Label label, boolean buttonArea) {
            if (buttonArea) {
                label.setArea(instance().area);
            }
            instance().label = label;
            return self();
        }

    }

}
