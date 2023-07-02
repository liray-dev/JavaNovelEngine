package jne.engine.screens.widgets;

import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;

import java.awt.*;

public class Label<SELF extends Label<SELF>> extends Component<SELF> {

    public Color color = Color.WHITE;
    public float size = 1F;
    public String text;
    public boolean isCentered;

    @Override
    public void onRender(float partialTicks) {
        if (text != null && !text.isEmpty()) {
            Area center = this.area.getCenter();

            float fontHeight = FONT.getHeight(text) / FONT.size * 2;
            float fontWidth = FONT.getWidth(text) / FONT.size * 2;

            RENDER.scale(size, size, () -> {
                FONT.drawText(text, (center.x - fontWidth) / size, (center.y) / size, area.z, color);
            });
        }
    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Label<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new Label();
        }

        public SELF text(String text) {
            instance().text = text;
            return self();
        }

        public SELF size(float size) {
            instance().size = size;
            return self();
        }

        public SELF color(Color color) {
            instance().color = color;
            return self();
        }

        public SELF centered(boolean flag) {
            instance().isCentered = flag;
            return self();
        }

    }


}
