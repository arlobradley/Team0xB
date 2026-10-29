package io.team0xB;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

// temporary shape rendering will replace with sprites later
public class Enemy {
    private final Random random = new Random();
    private final Vector2 direction = Vector2.Zero;

    private Vector2 position;
    private float speed = 4f;
    private float health;
    private float size = 0.4f;


    public Enemy(float health, Vector2 position){
        this.health = health;
        this.position = position;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.circle(position.x, position.y, size);
        renderer.end();
    }

    public void update(Vector2 playerPosition, float delta) {
        if (this.health <= 0) this.dispose(new ShapeRenderer());
        direction.x = playerPosition.x - position.x;
        direction.y = playerPosition.y - position.y;
        // convert direction to unit vector
        float directionMag = (float) Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.y, 2));
        direction.x /= directionMag;
        direction.y /= directionMag;
        position.mulAdd(direction, speed * delta);
    }

    public void dispose(ShapeRenderer renderer){
        renderer.dispose();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
