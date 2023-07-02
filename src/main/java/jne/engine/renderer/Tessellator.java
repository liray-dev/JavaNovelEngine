package jne.engine.renderer;

import jne.engine.renderer.buffer.BufferBuilder;
import jne.engine.renderer.buffer.BufferHelper;

public class Tessellator {

    private final BufferBuilder buffer;
    private static final BufferHelper helper = new BufferHelper();
    private static final Tessellator INSTANCE = new Tessellator(1024 * 1024 * 4);

    public static Tessellator getInstance() {
        return INSTANCE;
    }

    public Tessellator(int bufferSize) {
        this.buffer = new BufferBuilder(bufferSize);
    }

    public void draw() {
        buffer.flush();
        buffer.draw();
    }

    public BufferBuilder getBuffer() {
        return this.buffer;
    }

    public BufferHelper getHelper() {
        return helper;
    }
}
