package jne.editor;

import jne.engine.events.EventListenerHelper;
import jne.engine.events.types.TextureRegistryEvent;
import jne.engine.events.utils.Novel;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.texture.Texture;
import jne.engine.texture.TextureContainer;
import jne.engine.utils.IWrapper;
import jne.engine.utils.ResourceLocation;

@Novel
public class EditorCore implements IWrapper {

    public static Texture error;
    public static Texture background;
    public static Texture resize;
    public static Texture move;
    public static Texture zoom;
    public static Texture add;
    public static Texture exit;
    public static Texture trueIcon;
    public static Texture falseIcon;

    public EditorCore() {
        EventListenerHelper.register(this);
    }

    @SubscribeEvent
    public void onTextureRegistry(TextureRegistryEvent event) {
        TextureContainer container = event.getContainer();

        error = container.register(new ResourceLocation("editor/error.png"));
        background = container.register(new ResourceLocation("editor/background.png"));
        resize = container.register(new ResourceLocation("editor/resize.png"));
        move = container.register(new ResourceLocation("editor/move.png"));
        zoom = container.register(new ResourceLocation("editor/zoom.png"));
        add = container.register(new ResourceLocation("editor/add.png"));
        exit = container.register(new ResourceLocation("editor/exit.png"));
        trueIcon = container.register(new ResourceLocation("editor/true.png"));
        falseIcon = container.register(new ResourceLocation("editor/false.png"));

        container.register(new ResourceLocation("back.png"));
        container.register(new ResourceLocation("girl.png"));
        container.register(new ResourceLocation("phil_alpha.png"));

    }

}
