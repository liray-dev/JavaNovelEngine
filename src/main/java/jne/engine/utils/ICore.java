package jne.engine.utils;

import jne.engine.core.JNE;
import jne.engine.core.Window;
import jne.engine.texture.TextureContainer;

public interface ICore {

    JNE ENGINE = JNE.getInstance();
    Window WINDOW = Window.getInstance();
    TextureContainer TEXTURE_CONTAINER = TextureContainer.getInstance();
}
