package io.team0xB;

public class BurstItem extends Item {
    private static final float COOLDOWN_SECONDS = 10f;

    public BurstItem() {
        super("Burst", COOLDOWN_SECONDS);
    }

    @Override
    protected void activate(GameWorld world) {
        world.addEffect(new BurstEffect(world));
    }
}
