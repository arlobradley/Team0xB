package io.team0xB;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

// temporary shape rendering will replace with sprites later
public class Enemy {
    private float speed;
    private Vector2 direction = Vector2.Zero;
    private float health;
    private Vector2 position;

    public Enemy(float health){
        this.health = health;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.circle(100, 100, 50);
        renderer.end();
    }

    public void updateDirection(PathfindingGrid gameGrid) {
        this.direction = gameGrid.getFlow(this.position);
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
}
