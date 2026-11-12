package io.team0xB;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import javax.swing.*;
import java.util.Random;

// temporary shape rendering will replace with sprites later
public class Enemy {
    private final Random random = new Random();
    private final float animationOffset;
    private boolean flippedSprite;

    private float health;
    private Vector2 size = new Vector2(1.2f, 1.2f);
    private final float ORIGINAL_WIDTH = size.x;
    private Vector2 position;
    private final Vector2 direction = Vector2.Zero;
    private float speed = 4f;


    public Enemy(float health, Vector2 position){
        this.animationOffset = random.nextFloat(0f, 48f);
        this.health = health;
        this.position = position;

    }

    public void draw(Animator animator, SpriteBatch spriteBatch, float animTime , float delta) {
        TextureRegion spriteRegion = animator.getNextFrame(
            Animator.AnimKey.ENEMY,
            getAnimationOffset(),
            animTime,
            delta
        );

        float turnSpeed = 8f;
        if (flippedSprite){
            if (size.x > -ORIGINAL_WIDTH){
                size.x -= turnSpeed * delta;
            }
        } else {
            if (size.x <= ORIGINAL_WIDTH){
                size.x += turnSpeed * delta;
            }
        }
        spriteBatch.draw(spriteRegion, position.x, position.y, size.x, size.y);
    }

    public void update(Vector2 playerPosition, float delta) {
//        if (this.health <= 0) this; need to dispose of this class after death (being fed)

        direction.x = playerPosition.x - position.x;
        direction.y = playerPosition.y - position.y;
        // convert direction to unit vector
        float directionMag = (float) Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.y, 2));
        direction.x /= directionMag;
        direction.y /= directionMag;
        position.mulAdd(direction, speed * delta);

        flippedSprite = playerPosition.x > position.x;

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

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public float getAnimationOffset() {
        return animationOffset;
    }
}
