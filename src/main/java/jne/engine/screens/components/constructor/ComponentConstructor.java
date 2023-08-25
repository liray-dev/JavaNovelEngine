package jne.engine.screens.components.constructor;

import jne.editor.utils.constructors.AbstractComponentConstructor;
import jne.engine.screens.widgets.Component;
import jne.engine.screens.widgets.TextBox;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * It is necessary to mark with this annotation the methods that will need to be edited through the UI
 */
@Retention(value = RUNTIME)
@Target(value = METHOD)
public @interface ComponentConstructor {

    String text() default "Enter the value";
    String example() default "";
    Class<? extends Component> builder() default TextBox.class;

    Class<? extends AbstractComponentConstructor> option() default AbstractComponentConstructor.class;

}