package jne.engine.screens.components;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Data
@RequiredArgsConstructor
public class MethodConstructor {

    private final Method method;
    private final Class<? extends Component> component;
    private final Class<?>[] types;

    // UI VISUAL
    private final String ghostText;
    private final String infoText;

}
