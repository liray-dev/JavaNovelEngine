package jne.editor.nodes;

import jne.editor.scenes.SceneUnit;
import jne.engine.serializer.ISerializable;
import org.json.JSONObject;

import java.util.HashMap;

public class NodeEditor implements ISerializable {

    public HashMap<String, SceneUnit> scenes = new HashMap<>();

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        scenes.forEach((name, scene) -> {
            json.put(name, scene.toJson());
        });

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        scenes.clear();

        for (String key : json.keySet()) {
            JSONObject sceneJson = json.getJSONObject(key);

            SceneUnit scene = new SceneUnit();
            scene.fromJson(sceneJson);

            scenes.put(key, scene);
        }
    }

}
