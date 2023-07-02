package jne.sandbox.sreens;

import jne.engine.screens.listeners.ScreenListener;
import jne.engine.texture.Texture;

import java.io.File;

public class MainScreen extends ScreenListener {

    Texture girl;
    Texture background;

    @Override
    public void init() {
        this.girl = new Texture(new File(jne.assetsDir.getAbsoluteFile() + "/girl.png"));
        this.background = new Texture(new File(jne.assetsDir.getAbsoluteFile() + "/back.png"));

    }

    @Override
    public void render(float partialTicks) {
        RENDER.drawTexturedQuad(0, 0, width, height, background);
        FONT.drawText("Hello world! Привет мир!", 100, 100, 0);
    }

    @Override
    public void mouseMove(int mouseX, int mouseY) {
        //System.out.println(mouseX + "|" + mouseY + " WindowRender: " + width + "|" + height);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //System.out.println("Clicked: [" + mouseButton + "] " + mouseX + "|" + mouseY);
        //Mouse.setCursorPosition(width / 2, height / 2);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        //System.out.println("Move Clicked: [" + mouseButton + "] " + mouseX + "|" + mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        //System.out.println("Typed: " + typedChar);
    }

    int seconds = 0;
    int ticks = 0;

    @Override
    public void tick() {
        ticks++;
        if (ticks >= 40) {
            ticks = 0;
        }
    }
}
