package jne.engine.screens.components.constructor;

import jne.editor.utils.constructors.AbstractComponentConstructor;
import jne.engine.screens.widgets.Component;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Data
@RequiredArgsConstructor
public class MethodConstructor {

    private final Method method;
    private final Class<? extends Component> component;
    private final AbstractComponentConstructor option;
    private final Class<?>[] types;

    // UI VISUAL
    private final String ghostText;
    private final String infoText;

}
