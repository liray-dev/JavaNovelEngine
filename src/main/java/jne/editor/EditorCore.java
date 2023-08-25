package jne.editor;

import jne.engine.events.EventListenerHelper;
import jne.engine.events.types.TextureRegistryEvent;
import jne.engine.events.utils.Novel;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.texture.Texture;
import jne.engine.texture.TextureContainer;
import jne.engine.api.IWrapper;
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
    public static Texture plus;
    public static Texture question;

    public EditorCore() {
        EventListenerHelper.register(this);
    }

    @SubscribeEvent
    public void onTextureRegistry(TextureRegistryEvent event) {
        TextureContainer container = event.getContainer();

        error = container.registerSystem(new ResourceLocation("editor/error.png"));
        background = container.registerSystem(new ResourceLocation("editor/background.png"));
        resize = container.registerSystem(new ResourceLocation("editor/resize.png"));
        move = container.registerSystem(new ResourceLocation("editor/move.png"));
        zoom = container.registerSystem(new ResourceLocation("editor/zoom.png"));
        add = container.registerSystem(new ResourceLocation("editor/add.png"));
        exit = container.registerSystem(new ResourceLocation("editor/exit.png"));
        trueIcon = container.registerSystem(new ResourceLocation("editor/true.png"));
        falseIcon = container.registerSystem(new ResourceLocation("editor/false.png"));
        plus = container.registerSystem(new ResourceLocation("editor/plus.png"));
        question = container.registerSystem(new ResourceLocation("editor/question.png"));

        container.register(new ResourceLocation("back.png"));
        container.register(new ResourceLocation("girl.png"));
        container.register(new ResourceLocation("100.jpg"));
        container.register(new ResourceLocation("101.jpg"));
        container.register(new ResourceLocation("102.jpg"));
        container.register(new ResourceLocation("103.jpg"));
        container.register(new ResourceLocation("400.jpg"));
        container.register(new ResourceLocation("417.jpg"));
        container.register(new ResourceLocation("418.jpg"));
        container.register(new ResourceLocation("429.jpg"));
        container.register(new ResourceLocation("599.jpg"));

    }

}
