package io.team0xB;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends Entity {
    private static final float SPEED = 12f;
    private static final float RADIUS = 0.15f;
    // Seconds before the projectile removes itself
    // Range = SPEED * LIFETIME
    private static final float LIFETIME_SECONDS = 1.5f;

    // Unit-length direction of travel, fixed at launch
    private final Vector2 trajectory = new Vector2();
    private final CountdownTimer lifetime = new CountdownTimer(LIFETIME_SECONDS);

    public Projectile(float x, float y, Vector2 direction) {
        super(x, y, RADIUS);
        trajectory.set(direction).nor();
    }

    @Override
    public void update(float delta) {
        lifetime.update(delta);

        if (lifetime.isFinished()) {
            remove();
            return;
        }

        position.mulAdd(trajectory, SPEED * delta);
    }

    @Override
    public void render(ShapeRenderer shapes) {
        shapes.setColor(0.10f, 0.10f, 0.10f, 1f);
        shapes.circle(position.x, position.y, RADIUS + 0.05f, 12);
        shapes.setColor(1f, 0.90f, 0.20f, 1f);
        shapes.circle(position.x, position.y, RADIUS, 12);
    }
}
