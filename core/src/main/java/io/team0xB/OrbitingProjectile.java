package io.team0xB;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class OrbitingProjectile extends Projectile {
    // distance from the centre of the player, in world units
    private static final float ORBIT_DISTANCE = 1.6f;
    // radians per second
    private static final float ANGULAR_SPEED = 4f;
    private static final float RADIUS = 0.3f;

    private final Entity center;
    private float angle;

    public OrbitingProjectile(Entity center, float startAngle, float durationSeconds) {
        super(center.position.x, center.position.y, Vector2.X, RADIUS, durationSeconds);
        this.center = center;
        this.angle = startAngle;
        withColor(0.30f, 0.80f, 0.95f);
        placeOnOrbit();
    }

    @Override
    public void update(float delta) {
        if (tickLifetime(delta)) return;

        angle += ANGULAR_SPEED * delta;
        placeOnOrbit();
    }

    @Override
    public void onHit() {
        // an orbiting shot is not used up by a hit: it keeps spinning until its time runs out
    }

    private void placeOnOrbit() {
        position.set(
            center.position.x + MathUtils.cos(angle) * ORBIT_DISTANCE,
            center.position.y + MathUtils.sin(angle) * ORBIT_DISTANCE);
    }
}
