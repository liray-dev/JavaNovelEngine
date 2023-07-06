package jne.engine.screens.widgets;

import jne.engine.constants.MouseClickType;
import jne.engine.screens.components.Component;
import jne.engine.screens.components.ComponentConstructor;
import jne.engine.texture.TextureContainer;

import java.awt.*;

public class CheckBox<SELF extends CheckBox<SELF>> extends Component<SELF> {

    public Color trueColor = new Color(0x88B781);
    public Color falseColor = new Color(0xB78181);
    public Color backgroundColor = new Color(0x383838);

    public boolean flag = false;

    @Override
    public void onRender(float partialTicks) {
        RENDER.color(backgroundColor, () -> {
            RENDER.drawQuad(area.x, area.y, area.z, area.x2, area.y2);
        });
        if (!area.isEmpty()) {
            RENDER.color(flag ? trueColor : falseColor, () -> {
                RENDER.drawTexturedQuad(area.x, area.y, area.z, area.x2, area.y2, flag ? TextureContainer.get("true") : TextureContainer.get("false"));
            });
        }
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int button, MouseClickType type) {
        if (type == MouseClickType.RELEASED) return;
        this.flag = !this.flag;
    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends CheckBox<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new CheckBox();
        }


        @ComponentConstructor(text = "Check Box")
        public SELF flag(boolean flag) {
            instance().flag = flag;
            return self();
        }

        @ComponentConstructor(text = "Enabled color")
        public SELF trueColor(int color) {
            instance().trueColor = new Color(color);
            return self();
        }

        @ComponentConstructor(text = "Disabled color")
        public SELF falseColor(int color) {
            instance().trueColor = new Color(color);
            return self();
        }

        @ComponentConstructor(text = "Background color")
        public SELF backgroundColor(int color) {
            instance().trueColor = new Color(color);
            return self();
        }

    }


}
