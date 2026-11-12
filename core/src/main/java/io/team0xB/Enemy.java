package io.team0xB;



import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

// temporary shape rendering will replace with sprites later
public class Enemy {
    private final Random random = new Random();
    private final float animationOffset;
    private boolean flippedSprite;

    private float health;
    private Vector2 size = new Vector2(1.2f, 1.2f);
    private Vector2 position;
    private float fullWidth = size.x;
    private final Vector2 direction = Vector2.Zero;
    private float speed = 4f;


    public Enemy(float health, Vector2 position){
        this.animationOffset = random.nextFloat(0f, 48f);
        this.health = health;
        this.position = position;
    }

    /**
     * Draws the current frame of the enemies animation to the provided sprite batch.
     *
     * @param animator The games animator object.
     * @param spriteBatch The games sprite batch
     * @param animTime The float value of how much time has passed since the animation started.
     * @param delta The float value of how much time has passed since the last frame.
     */
    public void draw(Animator animator, SpriteBatch spriteBatch, float animTime , float delta) {
        TextureRegion spriteRegion = animator.getNextFrame(
            Animator.AnimKey.ENEMY,
            getAnimationOffset(),
            animTime,
            delta
        );

        float turnSpeed = 8f;
        if (flippedSprite){
            if (size.x > -fullWidth){
                size.x -= turnSpeed * delta;
            }
        } else {
            if (size.x <= fullWidth){
                size.x += turnSpeed * delta;
            }
        }
        spriteBatch.draw(spriteRegion, position.x, position.y, size.x, size.y);
    }

    /** Updates the location of the enemy, and changes it's velocity to head towards the player.
     * @param playerPosition A float 2D Vector value of the players coordinates.
     * @param delta A float value of how much time has passed since the last frame.
     */
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

    /**
     * @param speed float value that sets the speed that the enemy moves at.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * @return returns float value health of the enemy.
     */
    public float getHealth() {
        return health;
    }

    /**
     * @param health sets the health of the enemy to this float value.
     */
    public void setHealth(float health) {
        this.health = health;
    }

    /**
     * @return A 2D float Vector containing the coordinates of the enemy's current position.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @param position A 2D float Vector containing coordinates to move the enemy to.
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * @return The unit vector of the enemy's acceleration.
     */
    public Vector2 getDirection() {
        return direction;
    }

    /** sets the size of the enemy.
     * @param size 2D float Vector of width and height.
     */
    public void setSize(Vector2 size) {
        fullWidth = size.x;
        this.size = size;
    }

    public float getAnimationOffset() {
        return animationOffset;
    }
}
