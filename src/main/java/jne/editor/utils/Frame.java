package jne.editor.utils;

import jne.engine.api.IComponent;
import jne.engine.errors.DebugManager;
import jne.engine.serializer.ISerializable;
import lombok.Data;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Data
public class Frame implements ISerializable {

    private final int id;
    private List<IComponent> components;

    public Frame(int id) {
        this.id = id;
        this.components = new ArrayList<>();
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        int amount = 0;
        for (IComponent component : new ArrayList<>(components)) {
            json.put(String.valueOf(amount++), component.toJson());
        }

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        components.clear();

        for (String key : json.keySet()) {
            JSONObject componentJson = json.getJSONObject(key);

            try {
                Class<IComponent> clazz = (Class<IComponent>) Class.forName(componentJson.getString("type"));
                IComponent component = clazz.newInstance();
                component.fromJson(componentJson);
                components.add(component);
            } catch (Exception e) {
                DebugManager.error(e);
            }
        }
    }

}
