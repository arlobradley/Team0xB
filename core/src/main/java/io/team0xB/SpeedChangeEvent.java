package io.team0xB;

public class SpeedChangeEvent extends TimedEvent {
    private final float multiplier;

    public SpeedChangeEvent(String message, float multiplier, float durationSeconds) {
        super(message, durationSeconds);
        this.multiplier = multiplier;
    }

    @Override
    public void start(GameWorld world) {
        world.getPlayer().setSpeedMultiplier(multiplier);
    }

    @Override
    public void end(GameWorld world) {
        world.getPlayer().setSpeedMultiplier(1f);
    }
}
