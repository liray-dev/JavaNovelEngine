package jne.scenemaker.screens;

import jne.engine.screens.listeners.ScreenListener;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;
import jne.engine.utils.MouseClickType;
import jne.engine.texture.Texture;
import jne.scenemaker.screens.sub.ButtonSettingsSubScreen;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadScreen extends ScreenListener {

    public final List<Component> sceneComponents = new ArrayList<Component>();

    Texture resize;
    Texture move;

    @Override
    public void init() {
        this.resize = new Texture(new File(jne.assetsDir.getAbsoluteFile() + "/resize.png"));
        this.move = new Texture(new File(jne.assetsDir.getAbsoluteFile() + "/move.png"));

        add(GRAPHICS.button()
                .area(new Area(100, 100, 1, 256, 256))
                .texture(resize)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Resize");
                })
                .onPress((component, type) -> {
                    if (type == MouseClickType.RELEASED) {
                        this.subscreen(new ButtonSettingsSubScreen(this, true, false));
                    }
                })
                .build());

        add(GRAPHICS.button()
                .area(new Area(0, 0, 1, 256, 256))
                .texture(move)
                .onTooltip((component, mouseX, mouseY, tick) -> {
                    System.out.println("Move");
                })
                .build());

        add(GRAPHICS.texture().area(new Area(200, 200, -3, 256, 256)).texture(move).build());

        add(GRAPHICS.label().area(new Area(200, 200, 10, 256, 256)).text("Resize1 \n Resize2").color(Color.RED).size(1f).build());

        super.init();
    }

    @Override
    public void render(float partialTicks) {
        super.render(partialTicks);
    }
}
