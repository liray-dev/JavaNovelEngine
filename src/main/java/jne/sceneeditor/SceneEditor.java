package jne.sceneeditor;

import jne.engine.events.EventListenerHelper;
import jne.engine.events.types.TextureRegistryEvent;
import jne.engine.events.utils.Novel;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.texture.Texture;
import jne.engine.texture.TextureContainer;
import jne.engine.utils.IWrapper;
import jne.engine.utils.ResourceLocation;

@Novel
public class SceneEditor implements IWrapper {

    public static Texture error;
    public static Texture background;
    public static Texture resize;
    public static Texture move;
    public static Texture zoom;
    public static Texture add;
    public static Texture exit;
    public static Texture trueIcon;
    public static Texture falseIcon;

    public SceneEditor() {
        EventListenerHelper.register(this);
    }

    @SubscribeEvent
    public void onTextureRegistry(TextureRegistryEvent event) {
        TextureContainer container = event.getContainer();

        error = container.register(new ResourceLocation("sceneeditor/error.png"));
        background = container.register(new ResourceLocation("sceneeditor/background.png"));
        resize = container.register(new ResourceLocation("sceneeditor/resize.png"));
        move = container.register(new ResourceLocation("sceneeditor/move.png"));
        zoom = container.register(new ResourceLocation("sceneeditor/zoom.png"));
        add = container.register(new ResourceLocation("sceneeditor/add.png"));
        exit = container.register(new ResourceLocation("sceneeditor/exit.png"));
        trueIcon = container.register(new ResourceLocation("sceneeditor/true.png"));
        falseIcon = container.register(new ResourceLocation("sceneeditor/false.png"));


    }

}