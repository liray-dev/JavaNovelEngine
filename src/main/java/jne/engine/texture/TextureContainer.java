package jne.engine.texture;

import jne.engine.utils.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TextureContainer {

    private static final HashMap<String, Texture> textures = new HashMap<>();

    public Texture register(ResourceLocation location) {
        Texture texture = new Texture(location);
        register(location.getName(), texture);
        return texture;
    }

    public Texture registerSystem(ResourceLocation location) {
        Texture texture = new Texture(location);
        texture.system = true;
        register(location.getName(), texture);
        return texture;
    }

    public void register(Texture texture) {
        if (texture.name != null && !texture.name.isEmpty()) {
            if (texture.available) {
                register(texture.name, texture);
            } else {
                System.out.println(texture.name + " texture has not been loaded");
            }
        } else {
            System.out.println("The unknown texture was not registered");
        }
    }

    public void register(String name, Texture texture) {
        if (textures.containsKey(name)) {
            System.out.println(name + " this texture is already loaded");
        } else {
            textures.put(name, texture);
        }
    }

    public static Texture get(String name) {
        if (name == null || name.isEmpty()) {
            return textures.get("error");
        }

        if (textures.containsKey(name)) {
            return textures.get(name);
        }
        
        return textures.get("error");
    }

    public static Map<String, Texture> get() {
       return Collections.unmodifiableMap(textures);
    }

    private static final TextureContainer INSTANCE = new TextureContainer();

    public static TextureContainer getInstance() {
        return INSTANCE;
    }

}
