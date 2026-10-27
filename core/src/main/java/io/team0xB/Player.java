package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    public static final float SPEED = 5f;
    public static final float RADIUS = 0.4f;

    public final Vector2 facing = new Vector2(0f, -1f);
    private final Vector2 direction = new Vector2();

    public Player() {
        super(0f, 0f, RADIUS);
    }

    @Override
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

    @Override
    public void render(ShapeRenderer shapes) {
        shapes.setColor(0.05f, 0.10f, 0.35f, 1f);
        shapes.circle(position.x, position.y, RADIUS, 24);

        shapes.setColor(1f, 1f, 1f, 1f);
        shapes.circle(
            position.x + facing.x * RADIUS * 0.7f,
            position.y + facing.y * RADIUS * 0.7f,
            0.12f,
            12
        );
    }
}
