package jne.engine.renderer;

import jne.engine.screens.widgets.Button;
import jne.engine.screens.widgets.Label;
import jne.engine.screens.widgets.TextBox;
import jne.engine.screens.widgets.TexturedComponent;

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

    public TextBox.Builder<? extends TextBox.Builder<?, ?>, ? extends TextBox<?>> textbox() {
        return new TextBox.Builder<>();
    }

}
