package jne.engine.renderer.buffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

public class BufferBuilder
{
    /* 6 vertices * (3f vertex + 3f normal + 2f uv) */
    private static final int BYTES_PER_VERTEX = (3 * 4 + 3 * 4 + 2 * 4);

    public int vertices;
    private int vbo;
    private ByteBuffer buffer;

    public BufferBuilder(int size)
    {
        this.buffer = BufferUtils.createByteBuffer(size);

        this.vbo = GL15.glGenBuffers();
    }

    public void begin()
    {
        this.vertices = 0;

        this.buffer.clear();
    }

    public void addVertex(float x, float y, float z, float nx, float ny, float nz, float u, float v)
    {
        this.buffer.putFloat(x);
        this.buffer.putFloat(y);
        this.buffer.putFloat(z);

        this.buffer.putFloat(nx);
        this.buffer.putFloat(ny);
        this.buffer.putFloat(nz);

        this.buffer.putFloat(u);
        this.buffer.putFloat(v);

        this.vertices += 1;
    }

    public void addVertex(float x, float y, float z, float u, float v)
    {
        this.buffer.putFloat(x);
        this.buffer.putFloat(y);
        this.buffer.putFloat(z);

        this.buffer.putFloat(0F);
        this.buffer.putFloat(0F);
        this.buffer.putFloat(0F);

        this.buffer.putFloat(u);
        this.buffer.putFloat(v);

        this.vertices += 1;
    }

    public void flush()
    {
        this.buffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.buffer, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void draw()
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, BYTES_PER_VERTEX, 0);
        GL11.glNormalPointer(GL11.GL_FLOAT, BYTES_PER_VERTEX, 3 * 4);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, BYTES_PER_VERTEX, 6 * 4);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        /* Render with index buffer */
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertices);

        /* Unbind the buffer. REQUIRED to avoid OpenGL crash */
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }
}

