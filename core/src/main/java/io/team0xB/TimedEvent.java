package io.team0xB;

public abstract class TimedEvent extends Event {
    private final String message;
    private final CountdownTimer duration;

    protected TimedEvent(String message, float durationSeconds) {
        this.message = message;
        this.duration = new CountdownTimer(durationSeconds);
    }

    @Override
    public void update(float delta) {
        duration.update(delta);
    }

    @Override
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
