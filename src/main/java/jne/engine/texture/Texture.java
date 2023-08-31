package jne.engine.texture;

import jne.engine.debug.DebugManager;
import jne.engine.utils.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {

    private int textureId;
    public String name;
    public int width, height;
    public boolean available = true;
    public boolean system = false;

    public Texture(int width, int height, ByteBuffer buffer) {
        textureId = GL11.glGenTextures();
        createTexture(width, height, buffer);
    }

    public Texture(ResourceLocation location) {
        this(location.getFile());
    }

    public Texture(File file) {
        textureId = GL11.glGenTextures();
        this.name = file.getName();
        load(file);
    }

    public void createTexture(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;

        bind();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        setParameters();
        unbind();
    }

    public void load(File file) {
        try {
            BufferedImage image = ImageIO.read(file);

            int width = image.getWidth();
            int height = image.getHeight();

            ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    buffer.put((byte) ((rgb >> 16) & 0xFF)); // Красный
                    buffer.put((byte) ((rgb >> 8) & 0xFF)); // Зеленый
                    buffer.put((byte) (rgb & 0xFF)); // Синий
                    buffer.put((byte) ((rgb >> 24) & 0xFF)); // Альфа
                }
            }

            buffer.flip();

            this.createTexture(width, height, buffer);
        } catch (IOException e) {
            this.available = false;
            DebugManager.error(e);
        }
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }

    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void delete() {
        GL11.glDeleteTextures(textureId);
    }

    private void setParameters() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }
}