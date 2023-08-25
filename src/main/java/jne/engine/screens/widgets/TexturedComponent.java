package jne.engine.screens.widgets;

import jne.editor.utils.constructors.SelectTextureConstructor;
import jne.engine.screens.components.constructor.ComponentConstructor;
import jne.engine.texture.Texture;
import jne.engine.texture.TextureContainer;
import org.json.JSONObject;

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
        if (!area.isEmpty()) {
            RENDER.color(color, () -> {
                if (texture != null) {
                    RENDER.drawTexturedQuad(area.x, area.y, area.z, area.x2, area.y2, texture);
                } else {
                    RENDER.drawQuad(area.x, area.y, area.z, area.x2, area.y2);
                }
            });
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put("texture", texture.name);
        json.put("color", color.getRGB());
        json.put("alpha", alpha);

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        super.fromJson(json);

        this.texture = TextureContainer.get(json.getString("texture"));
        this.color = new Color(json.getInt("color"));
        this.alpha = json.getFloat("alpha");
    }

    public SELF self() {
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

        @ComponentConstructor(text = "Texture", example = "image without '.png'", option = SelectTextureConstructor.class)
        public SELF texture(String texture) {
            instance().texture = TextureContainer.get(texture);
            return self();
        }

        public SELF color(Color color) {
            instance().color = color;
            return self();
        }

        @ComponentConstructor(text = "Color", example = "example: RGB")
        public SELF color(int color) {
            instance().color = new Color(color);
            return self();
        }

    }

}
