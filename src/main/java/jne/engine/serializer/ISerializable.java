package jne.engine.serializer;

import org.json.JSONObject;

public interface ISerializable {

    JSONObject toJson();

    void fromJson(JSONObject json);

}
