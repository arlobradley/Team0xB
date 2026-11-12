package io.team0xB;

public abstract class Event {
    public abstract void start(GameWorld world);

    public abstract void end(GameWorld world);

    public abstract void update(float delta);

    public abstract boolean isFinished();
}
