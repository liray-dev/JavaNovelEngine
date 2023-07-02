package jne.engine.texture;

import jne.engine.utils.ResourceLocation;

import java.util.HashMap;

public class TextureContainer {

    public final HashMap<String, Texture> textures = new HashMap<>();

    public void register(ResourceLocation location) {
        register(location.getName(), new Texture(location));
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


    private static final TextureContainer INSTANCE = new TextureContainer();

    public static TextureContainer getInstance() {
        return INSTANCE;
    }

}
