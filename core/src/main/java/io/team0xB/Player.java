package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    public static final float SPEED = 5f;
    public static final float RADIUS = 0.4f;
    // Minimum seconds between shots
    public static final float FIRE_COOLDOWN_SECONDS = 0.35f;

    public final Vector2 facing = new Vector2(0f, -1f);
    private final Vector2 direction = new Vector2();
    private final Vector2 aimDirection = new Vector2();
    private final CountdownTimer fireCooldown = new CountdownTimer(FIRE_COOLDOWN_SECONDS);

    private boolean readyToAttack = false;

    private float speedMultiplier = 1f;

    private final Inventory inventory = new Inventory();

    private final GameMap map;

    public Player(GameMap map) {
        super(32f, 28f, RADIUS);
        // Starts with the attack ready
        fireCooldown.expire();
        this.map = map;
    }

    @Override
    public void update(float delta) {
        fireCooldown.update(delta);
        inventory.update(delta);

        direction.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) direction.y += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) direction.y -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) direction.x -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) direction.x += 1f;

        if (!direction.isZero()) {
            direction.nor();
            facing.set(direction);
            position.mulAdd(direction, SPEED * speedMultiplier * delta);
        }

        //Clamping player position within the bounds of the map
        position.x = MathUtils.clamp(position.x, RADIUS, map.getWidth() - RADIUS);
        position.y = MathUtils.clamp(position.y, RADIUS, map.getWidth() - RADIUS);
    }

    public Projectile tryFire(Vector2 aimPoint) {
        boolean pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        if (!pressed) {
            readyToAttack = true;
            return null;
        }

        if (!readyToAttack) return null;
        if (!fireCooldown.isFinished()) return null;

        // From the player towards the cursor, in world coordinates
        aimDirection.set(aimPoint).sub(position);
        // Cursor exactly on Player: fall back to the last movement direction
        if (aimDirection.len2() < 0.0001f) {
            aimDirection.set(facing);
        }

        aimDirection.nor();
        // The white dot on the Player now points where the shot went
        facing.set(aimDirection);

        fireCooldown.reset();

        return new Projectile(
            position.x + aimDirection.x * RADIUS,
            position.y + aimDirection.y * RADIUS,
            aimDirection
        );
    }

    public void setSpeedMultiplier(float multiplier) {
        this.speedMultiplier = multiplier;
    }

    public Inventory getInventory() {
        return inventory;
    }

    // Returns the slot whose key (1 to 5) was pressed this frame, or -1 if none was
    public int pollItemKey() {
        for (int slot = 0; slot < Inventory.SLOT_COUNT; slot++) {
            // The number keys have consecutive key codes, so slot 0 is key 1, slot 1 is key 2, and so on
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + slot)) {
                return slot;
            }
        }

        return -1;
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
