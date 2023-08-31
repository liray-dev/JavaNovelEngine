package jne.engine.renderer;

import jne.engine.screens.components.Area;
import jne.engine.texture.Texture;
import jne.engine.api.IWrapper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static jne.engine.constants.Colors.*;
import static org.lwjgl.opengl.GL11.*;

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

    public void scissor(float x, float y, float width, float height, Runnable runnable) {
        glEnable(GL_SCISSOR_TEST);

        if (width < 0) {
            width = 1;
        }

        if (height < 0) {
            height = 1;
        }
        
        glScissor((int) x, (int) (WINDOW.displayHeight - y - height), (int) width, (int) height);
        runnable.run();
        glDisable(GL_SCISSOR_TEST);
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

    public void drawLine(float startX, float startY, float endX, float endY, float z, float lineWidth) {
        BUILDER.begin();
        BUFFER_HELPER.addLineData(BUILDER, startX, startY, endX, endY, z, lineWidth);
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


    public void drawOutline(Area area) {
        drawOutline(area, blackOutline, whiteOutline);
    }

    public void drawOutline(Area area, Color color, Color color2) {
        RENDER.color(color, () -> {
            //TOP
            RENDER.drawQuad(area.x - 1, area.y - 1, area.z, area.x2 + 2, area.y + 2);
            //RIGHT
            RENDER.drawQuad(area.x2 - 1, area.y - 1, area.z, area.x2 + 2, area.y2 + 2);
            //BOTTOM
            RENDER.drawQuad(area.x2 - 1, area.y2 - 1, area.z, area.x, area.y2 + 2);
            //LEFT
            RENDER.drawQuad(area.x - 1, area.y2 + 1, area.z, area.x + 2, area.y + 2);
        });

        RENDER.color(color2, () -> {
            //TOP
            RENDER.drawQuad(area.x, area.y, area.z, area.x2 + 1, area.y + 1);
            //RIGHT
            RENDER.drawQuad(area.x2, area.y, area.z, area.x2 + 1, area.y2 + 1);
            //BOTTOM
            RENDER.drawQuad(area.x2, area.y2, area.z, area.x, area.y2 + 1);
            //LEFT
            RENDER.drawQuad(area.x, area.y2, area.z, area.x + 1, area.y + 1);
        });
    }

}
