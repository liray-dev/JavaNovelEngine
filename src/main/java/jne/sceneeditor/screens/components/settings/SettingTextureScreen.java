package jne.sceneeditor.screens.components.settings;

import jne.engine.screens.components.Area;
import jne.engine.screens.components.ComponentBuilderHelper;
import jne.engine.screens.widgets.TexturedComponent;
import jne.sceneeditor.screens.components.SettingComponentScreen;

public class SettingTextureScreen extends SettingComponentScreen {

    public TexturedComponent<? extends TexturedComponent<?>> texture;

    public SettingTextureScreen(Area area) {
        super(new ComponentBuilderHelper(GRAPHICS.texture().self()), area);
    }

    @Override
    public void collect() {
        errored = false;

        TexturedComponent.Builder<? extends TexturedComponent.Builder<?, ?>, ? extends TexturedComponent<?>> builder = GRAPHICS.texture().self();
        Class<? extends TexturedComponent.Builder> clazz = builder.getClass();

        boolean build = build(clazz, builder);

        if (build) {
            init = true;

            if (this.texture != null) {
                remove(this.texture);
            }

            this.texture = builder.build();
            Area center = this.area.getCenter();
            this.texture.setArea(new Area(center.x - 100, center.y - 100, Z_LEVEL, 200, 200));
            add(this.texture);
        }
    }

}