package jne.engine.screens;

import jne.engine.screens.components.Component;
import jne.engine.screens.widgets.*;

public class Graphics {

    private static final Graphics INSTANCE = new Graphics();

    public static Graphics getInstance() {
        return INSTANCE;
    }

    public Label.Builder<? extends Label.Builder<?, ?>, ? extends Label<?>> label() {
        return new Label.Builder<>();
    }

    public Button.Builder<? extends Button.Builder<?, ?>, ? extends Button<?>> button() {
        return new Button.Builder<>();
    }

    public TexturedComponent.Builder<? extends TexturedComponent.Builder<?, ?>, ? extends TexturedComponent<?>> texture() {
        return new TexturedComponent.Builder<>();
    }

    public CheckBox.Builder<? extends CheckBox.Builder<?, ?>, ? extends CheckBox<?>> checkbox() {
        return new CheckBox.Builder<>();
    }

    public TextBox.Builder<? extends TextBox.Builder<?, ?>, ? extends TextBox<?>> textbox() {
        return new TextBox.Builder<>();
    }

    public Panel.Builder<? extends Panel.Builder<?, ?>, ? extends Panel<?>> panel() {
        return new Panel.Builder<>();
    }

    public <T extends Component.Builder<T, E>, E extends Component<E>> T getBuilder(Class<T> builderClass) {
        try {
            return builderClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
