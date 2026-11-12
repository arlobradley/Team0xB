package io.team0xB;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    // public position allows other classes to read it
    public final Vector2 position = new Vector2();
    // every entity is a circle for collision
    private final float radius;
    // the world deletes removed entities at the end of each frame
    private boolean removed;

    protected Entity(float x, float y, float radius) {
        this.position.set(x, y);
        this.radius = radius;
    }

    // each subclass decides how it behaves each frame
    public abstract void update(float delta);

    // draws into a renderer the world has already begun
    public abstract void render(ShapeRenderer shapes);

    public float getRadius() {
        return radius;
    }

    // circle vs circle collision
    public boolean overlaps(Entity other) {
        float combined = radius + other.radius;
        return position.dst2(other.position) <= combined * combined;
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }
}
