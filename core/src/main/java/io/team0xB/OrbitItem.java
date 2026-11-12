package io.team0xB;

import com.badlogic.gdx.math.MathUtils;

public class OrbitItem extends Item {
    private static final float COOLDOWN_SECONDS = 12f;
    private static final float DURATION_SECONDS = 5f;
    private static final int ORBIT_COUNT = 1;

    public OrbitItem() {
        super("Orbit", COOLDOWN_SECONDS);
    }

    @Override
    protected void activate(GameWorld world) {
        Player player = world.getPlayer();
        for (int i = 0; i < ORBIT_COUNT; i++) {
            // Spread several orbiting shots evenly around the circle
            float startAngle = MathUtils.PI2 * i / ORBIT_COUNT;
            world.addProjectile(new OrbitingProjectile(player, startAngle, DURATION_SECONDS));
        }
    }
}
