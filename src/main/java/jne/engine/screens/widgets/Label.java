package jne.engine.screens.widgets;

import jne.engine.constants.Colors;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.constructor.ComponentConstructor;
import org.json.JSONObject;

import java.awt.*;

public class Label<SELF extends Label<SELF>> extends Component<SELF> {

    public Color color = Colors.textColor;
    public float size = 1F;
    public String text;
    public boolean isCentered;

    @Override
    public void onRender(float partialTicks) {
        if (text != null && !text.isEmpty()) {
            if (isCentered) {
                Area center = area.getCenter();
                FONT.drawColoredShadowedText(text, center.x, center.y, area.z, color, true, size);
            } else {
                FONT.drawColoredShadowedText(text, area.x, area.y, area.z, color, false, size);
            }

        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put("color", color.getRGB());
        json.put("size", size);
        json.put("text", text);
        json.put("isCentered", isCentered);

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        super.fromJson(json);
        this.color = new Color(json.getInt("color"));
        this.size = json.getInt("size");
        this.text = json.getString("text");
        this.isCentered = json.getBoolean("isCentered");
    }

    public SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Label<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new Label();
        }

        @ComponentConstructor(text = "Text", example = "example: Hello World!")
        public SELF text(String text) {
            instance().text = text;
            return self();
        }

        @ComponentConstructor(text = "Size", example = "example: 1.0")
        public SELF size(float size) {
            instance().size = size;
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

        @ComponentConstructor(text = "Centered", builder = CheckBox.class)
        public SELF centered(boolean flag) {
            instance().isCentered = flag;
            return self();
        }

    }


}
