package jne.scenemaker.screens.sub;

import jne.engine.screens.listeners.ScreenListener;
import jne.engine.screens.listeners.SubScreenListener;
import jne.engine.screens.components.Area;

import java.awt.*;

public class ButtonSettingsSubScreen extends SubScreenListener {

    public ButtonSettingsSubScreen(ScreenListener screen) {
        super(screen);
    }

    public ButtonSettingsSubScreen(ScreenListener screen, boolean render, boolean interact) {
        super(screen, render, interact);
    }

    @Override
    public void init() {
        add(GRAPHICS.label().area(new Area(200, 200, 15, 100, 100)).color(Color.WHITE).text("SubScreen текст").build());
    }

    @Override
    public void render(float partialTicks) {
        super.render(partialTicks);
//        RENDER.color(Color.BLACK, () -> {
//            RENDER.drawQuad(100, 100, 10, width - 100, height - 100);
//        });
    }
}
