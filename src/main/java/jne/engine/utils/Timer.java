package jne.engine.utils;

public class Timer {

    public float partialTick;
    public float tickDelta;
    private long lastMs;
    private final float msPerTick;

    public Timer(float tps, long lastMs) {
        this.msPerTick = 1000.0F / tps;
        this.lastMs = lastMs;
    }

    public int updateTimer(long lastMs) {
        this.tickDelta = (float) (lastMs - this.lastMs) / this.msPerTick;
        this.lastMs = lastMs;
        this.partialTick += this.tickDelta;
        int i = (int) this.partialTick;
        this.partialTick -= (float) i;
        return i;
    }
}
