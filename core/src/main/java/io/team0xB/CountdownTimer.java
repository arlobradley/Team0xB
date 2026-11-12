package io.team0xB;

public class CountdownTimer {
    private final float duration;
    private float remaining;
    private boolean running = true;

    public CountdownTimer(float durationSeconds) {
        this.duration = durationSeconds;
        this.remaining = durationSeconds;
    }

    public void update(float delta) {
        if (!running || isFinished()) return;
        remaining = Math.max(0f, remaining - delta);
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    public void reset() {
        remaining = duration;
        running = true;
    }

    public boolean isFinished() {
        return remaining <= 0f;
    }

    public float getRemaining() {
        return remaining;
    }

    public float getElapsed() {
        return duration - remaining;
    }

    public int getWholeSecondsRemaining() {
        return (int) Math.ceil(remaining);
    }

    public void expire() {
        remaining = 0f;
    }
}
