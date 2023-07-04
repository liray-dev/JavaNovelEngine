package jne.engine.renderer;

import jne.engine.texture.Texture;
import jne.engine.utils.IWrapper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderHelper implements IWrapper {

    private static final RenderHelper INSTANCE = new RenderHelper();

    public static RenderHelper getInstance() {
        return INSTANCE;
    }

    public void color(Color color, Runnable runnable) {
        color(color);
        runnable.run();
        clearColor();
    }

    public void scale(float x, float y, Runnable runnable) {
        GL11.glScalef(x, y, 1F);
        runnable.run();
        GL11.glScalef(-x, -y, 1F);
    }

    public void matrix(Runnable runnable) {
        push();
        runnable.run();
        pop();
    }

    public void push() {
        GL11.glPushMatrix();
    }

    public void pop() {
        GL11.glPopMatrix();
    }

    public void color(Color color) {
        this.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    private void color(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
    }

    public void clearColor() {
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public void drawQuad(float x, float y, float width, float height) {
        BUILDER.begin();
        BUFFER_HELPER.addQuadData(BUILDER, x, y, width, height);
        TESSELLATOR.draw();
    }

    public void drawQuad(float x, float y, float z, float width, float height) {
        BUILDER.begin();
        BUFFER_HELPER.addQuadData(BUILDER, x, y, z, width, height);
        TESSELLATOR.draw();
    }

    public void drawTexturedQuad(float x, float y, float width, float height, Texture texture) {
        texture.bind();
        BUILDER.begin();
        BUFFER_HELPER.addQuadData(BUILDER, x, y, width, height);
        TESSELLATOR.draw();
        texture.unbind();
    }

    public void drawTexturedQuad(float x, float y, float z, float width, float height, Texture texture) {
        texture.bind();
        BUILDER.begin();
        BUFFER_HELPER.addQuadData(BUILDER, x, y, z, width, height);
        TESSELLATOR.draw();
        texture.unbind();
    }

}
