package jne.editor.utils.constructors;

import jne.engine.constants.Direction;
import jne.engine.screens.components.Area;
import jne.engine.screens.widgets.Panel;
import jne.engine.texture.Texture;
import jne.engine.texture.TextureContainer;
import jne.engine.api.IComponent;

import java.util.Map;
import java.util.Set;

import static jne.engine.constants.Colors.toolColor;

public class SelectTextureConstructor extends AbstractComponentConstructor {

    Panel<? extends Panel<?>> panel;

    @Override
    public IComponent collect() {

        panel = GRAPHICS.panel().color(toolColor).wrapContent().build();

        Set<Map.Entry<String, Texture>> textures = TextureContainer.get().entrySet();

        Area area = new Area(1, -51, this.getDepth(), 50, 50);

        for (Map.Entry<String, Texture> unit : textures) {
            String key = unit.getKey();
            Texture value = unit.getValue();
            if (value.system) continue;
            area = area.offset(0, 5, Direction.BOTTOM);
            panel.addComponent(0, GRAPHICS.texture().texture(value).area(area).answer(key).build());
            panel.addComponent(1, GRAPHICS.label().area(area.offset(5, 10, Direction.RIGHT)).answer(key).text(key).build());
        }

        return panel;
    }

}
