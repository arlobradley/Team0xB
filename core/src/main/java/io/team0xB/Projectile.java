package io.team0xB;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends Entity {
    private static final float SPEED = 12f;
    private static final float DEFAULT_RADIUS = 0.15f;
    // Seconds before the projectile removes itself
    // Range = SPEED * LIFETIME
    private static final float DEFAULT_LIFETIME_SECONDS = 1.5f;
    private static final float OUTLINE_WIDTH = 0.05f;

    // Unit-length direction of travel, fixed at launch
    private final Vector2 trajectory = new Vector2();
    private final CountdownTimer lifetime;
    private final Color fillColor = new Color(1f, 0.90f, 0.20f, 1f);

    public Projectile(float x, float y, Vector2 direction) {
        this(x, y, direction, DEFAULT_RADIUS, DEFAULT_LIFETIME_SECONDS);
    }

    public Projectile(float x, float y, Vector2 direction, float radius) {
        this(x, y, direction, radius, DEFAULT_LIFETIME_SECONDS);
    }

    protected Projectile(float x, float y, Vector2 direction, float radius, float lifetimeSeconds) {
        super(x, y, radius);
        trajectory.set(direction).nor();
        lifetime = new CountdownTimer(lifetimeSeconds);
    }

    // Sets the fill colour and returns this projectile, so it can be chained onto the constructor
    public Projectile withColor(float r, float g, float b) {
        fillColor.set(r, g, b, 1f);
        return this;
    }

    @Override
    public void update(float delta) {
        if (tickLifetime(delta)) return;
        position.mulAdd(trajectory, SPEED * delta);
    }

    // Returns true when the lifetime is over, after flagging this projectile for removal
    protected boolean tickLifetime(float delta) {
        lifetime.update(delta);

        if (lifetime.isFinished()) {
            remove();
            return true;
        }

        return false;
    }

    public void onHit() {
        remove();
    }

    @Override
    public void render(ShapeRenderer shapes) {
        shapes.setColor(0.10f, 0.10f, 0.10f, 1f);
        shapes.circle(position.x, position.y, getRadius() + OUTLINE_WIDTH, 12);

        shapes.setColor(fillColor);
        shapes.circle(position.x, position.y, getRadius(), 12);
    }
}
