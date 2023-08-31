package jne.engine.renderer.buffer;

import jne.engine.api.IWrapper;
import jne.engine.texture.Texture;

import static java.lang.Math.sqrt;

public class BufferHelper implements IWrapper {

    private static final float Z_LEVEL = -5000F;

    public void addLineData(BufferBuilder builder, float startX, float startY, float endX, float endY, float z, float lineWidth) {
        float dx = endX - startX;
        float dy = endY - startY;
        float length = (float) sqrt(dx * dx + dy * dy);
        float unitX = dx / length;
        float unitY = dy / length;

        float offsetX = unitY * lineWidth;
        float offsetY = -unitX * lineWidth;

        builder.addVertex(startX + offsetX, startY + offsetY, z, 0F, 1F);
        builder.addVertex(endX + offsetX, endY + offsetY, z, 1F, 0F);
        builder.addVertex(endX - offsetX, endY - offsetY, z, 1F, 1F);

        builder.addVertex(startX + offsetX, startY + offsetY, z, 0F, 1F);
        builder.addVertex(endX - offsetX, endY - offsetY, z, 1F, 1F);
        builder.addVertex(startX - offsetX, startY - offsetY, z, 0F, 0F);
    }



    public void addQuadData(BufferBuilder builder, float x, float y, float width, float height) {
        this.addQuadData(builder, x, y, Z_LEVEL, width, height);
    }

    public void addQuadData(BufferBuilder builder, float x, float y, float z, float width, float height) {
        builder.addVertex(x, height, z, 0F, 1F);
        builder.addVertex(width, height, z, 1F, 1F);
        builder.addVertex(width, y, z, 1F, 0F);

        builder.addVertex(x, height, z, 0F, 1F);
        builder.addVertex(width, y, z, 1F, 0F);
        builder.addVertex(x, y, z, 0F, 0F);
    }

    public void drawTextureRegion(BufferBuilder builder, Texture texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight) {
        float width = x + regWidth;
        float height = y + regHeight;

        float u1 = regX / texture.width;
        float v1 = regY / texture.height;
        float u2 = (regX + regWidth) / texture.width;
        float v2 = (regY + regHeight) / texture.height;

        drawTextureRegion(builder, x, y, z, width, height, u1, v1, u2, v2);
    }

    public void drawTextureRegion(BufferBuilder builder, float x, float y, float z, float width, float height, float u1, float v1, float u2, float v2) {
        builder.addVertex(x, y, z, u1, v2);
        builder.addVertex(width, height, z, u2, v1);
        builder.addVertex(width, y, z, u2, v2);

        builder.addVertex(x, y, z, u1, v2);
        builder.addVertex(x, height, z, u1, v1);
        builder.addVertex(width, height, z, u2, v1);
    }


}
