package jne.engine.utils;

import jne.engine.core.JNE;
import jne.engine.core.Window;

public interface ICore {
    JNE ENGINE = JNE.getInstance();
    Window WINDOW = Window.getInstance();
}
