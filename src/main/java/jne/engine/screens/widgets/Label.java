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

            RENDER.scale(size, size, () -> {
                if (isCentered) {
                    Area center = area.getCenter();
                    FONT.drawText(text, (center.x) / size, (center.y) / size, area.z, color, true);
                } else {
                    FONT.drawText(text, (area.x) / size, (area.y) / size, area.z, color, false);
                }
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
