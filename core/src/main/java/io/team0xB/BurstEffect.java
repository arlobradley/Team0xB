package io.team0xB;

import com.badlogic.gdx.math.Vector2;

public class BurstEffect extends ItemEffect {
    private static final float DURATION_SECONDS = 3f;
    private static final float PULSE_INTERVAL_SECONDS = 0.5f;
    private static final int DIRECTIONS = 8;
    private static final float PROJECTILE_RADIUS = 0.25f;

    private final GameWorld world;
    private final CountdownTimer duration = new CountdownTimer(DURATION_SECONDS);
    private final CountdownTimer pulseTimer = new CountdownTimer(PULSE_INTERVAL_SECONDS);
    private final Vector2 direction = new Vector2();

    public BurstEffect(GameWorld world) {
        this.world = world;
        firePulse();
    }

    private void firePulse() {
        Player player = world.getPlayer();
        for (int i = 0; i < DIRECTIONS; i++) {
            // 8 evenly spaced angles: 0, 45, 90 ... 315 degrees
            direction.set(1f, 0f).setAngleDeg(i * 360f / DIRECTIONS);
            world.addProjectile(new Projectile(
                player.position.x + direction.x * player.getRadius(),
                player.position.y + direction.y * player.getRadius(),
                direction,
                PROJECTILE_RADIUS).withColor(1f, 0.55f, 0.10f));
        }
    }

    @Override
    public void update(float delta) {
        duration.update(delta);
        if (duration.isFinished()) return;

        pulseTimer.update(delta);
        if (pulseTimer.isFinished()) {
            firePulse();
            pulseTimer.reset();
        }
    }

    @Override
    public boolean isFinished() {
        return duration.isFinished();
    }
}
