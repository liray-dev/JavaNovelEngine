package jne.engine.screens.widgets;

import org.json.JSONObject;

public class Button<SELF extends Button<SELF>> extends TexturedComponent<SELF> {

    public Label label;

    @Override
    public void onRender(float partialTicks) {
        super.onRender(partialTicks);

        if (label != null && !label.text.isEmpty()) {
            label.onRender(partialTicks);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        if (label != null) {
            json.put("label", label.toJson());
        }

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        super.fromJson(json);
        if (json.has("label")) {
            label.fromJson(json.getJSONObject("label"));
        }
    }

    public SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Button<T>> extends TexturedComponent.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new Button();
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
