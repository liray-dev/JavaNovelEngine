package jne.engine.screens.widgets;

import jne.engine.screens.components.Component;
import jne.engine.texture.Texture;

import java.awt.*;


public class TexturedComponent<SELF extends TexturedComponent<SELF>> extends Component<SELF> {

    public Texture texture;
    public Color color;
    public float alpha;

    protected TexturedComponent() {
        this.color = Color.WHITE;
        this.alpha = 1F;
    }

    @Override
    public void onRender(float partialTicks) {
        super.onRender(partialTicks);
        if (texture != null && !area.isEmpty()) {

            RENDER.matrix(() -> {
                RENDER.color(color);
                RENDER.drawQuad(area.x, area.y, area.z, area.x2, area.y2);
                RENDER.clearColor();

                RENDER.color(Color.WHITE);
                RENDER.drawTexturedQuad(area.x, area.y, area.z, area.x2, area.y2, texture);
                RENDER.clearColor();
            });
        }
    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends TexturedComponent<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new TexturedComponent();
        }

        public SELF texture(Texture texture) {
            instance().texture = texture;
            return self();
        }

    }

}
