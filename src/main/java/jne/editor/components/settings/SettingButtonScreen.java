package jne.editor.components.settings;

import jne.engine.screens.components.Area;
import jne.engine.screens.components.ComponentBuilderHelper;
import jne.engine.screens.widgets.Button;
import jne.editor.components.SettingComponentScreen;

public class SettingButtonScreen extends SettingComponentScreen {

    public Button<? extends Button<?>> button;

    public SettingButtonScreen(Area area) {
        super(new ComponentBuilderHelper(GRAPHICS.button().self()), area);
    }

    @Override
    public void collect() {
        errored = false;

        Button.Builder<? extends Button.Builder<?, ?>, ? extends Button<?>> builder = GRAPHICS.button().self();
        Class<? extends Button.Builder> clazz = builder.getClass();

        boolean build = build(clazz, builder);

        if (build) {
            init = true;

            if (this.button != null) {
                remove(this.button);
            }

            this.button = builder.build();
            Area center = this.area.getCenter();
            this.button.setArea(new Area(center.x - 100, center.y - 100, Z_LEVEL, 200, 200));
            add(this.button);
        }
    }

}