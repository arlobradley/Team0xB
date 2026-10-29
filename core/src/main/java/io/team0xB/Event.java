package io.team0xB;

public abstract class Event {
    private final String message;
    private final CountdownTimer duration;

    protected Event(String message, float durationSeconds) {
        this.message = message;
        this.duration = new CountdownTimer(durationSeconds);
    }

    // Applies the effect to the game
    public abstract void start(GameWorld world);

    // Undoes the effect
    public abstract void end(GameWorld world);

    public void update(float delta) {
        duration.update(delta);
    }

    public boolean isFinished() {
        return duration.isFinished();
    }

    public String getMessage() {
        return message;
    }

    public int getSecondsRemaining() {
        return duration.getWholeSecondsRemaining();
    }
}
