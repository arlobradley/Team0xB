package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public static final float SPEED = 5f;
    public static final float RADIUS = 0.4f;

    public final Vector2 position = new Vector2(0, 0);
    public final Vector2 facing = new Vector2(0f, -1f);
    private final Vector2 direction = new Vector2();

    public void update(float delta) {
        direction.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) direction.y += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) direction.y -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) direction.x -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) direction.x += 1f;

        if (!direction.isZero()) {
            direction.nor();
            facing.set(direction);
            position.mulAdd(direction, SPEED * delta);
        }
    }
}
