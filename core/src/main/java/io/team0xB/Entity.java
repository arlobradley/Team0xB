package io.team0xB;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

//     draws a part of a spritesheet, determined by an animation to a provided sprite batch.
//     Should uncomment this once all other animated entities have sprite sheets.
//     or maybe move to a AnimatedEntity class

    // public abstract void render(Animator animator, SpriteBatch spriteBatch, float animTime, float delta);

    public float getRadius() {
        return radius;
    }

    // circle vs circle collision
    public boolean overlaps(Entity other) {
        float combined = radius + other.radius;
        return position.dst2(other.position) <= combined * combined;
    }

    public boolean overlaps(Entity other, float padding) {
        float combined = radius + other.radius;
        return position.dst2(other.position) <= padding + combined * combined;
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }
}
