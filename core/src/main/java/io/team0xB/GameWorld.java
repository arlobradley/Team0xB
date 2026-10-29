package io.team0xB;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    private final Player player = new Player();
    private final Array<Projectile> projectiles = new Array<>();
    private final Array<TriggerZone> triggerZones = new Array<>();

    // aimPoint is the cursor position in world coordinates
    public void update(float delta, Vector2 aimPoint) {
        player.update(delta);

        Projectile shot = player.tryFire(aimPoint);
        if (shot != null) {
            projectiles.add(shot);
        }

        for (int i = 0; i < projectiles.size; i++) {
            projectiles.get(i).update(delta);
        }

        removeFinishedEntities();
    }

    private void removeFinishedEntities() {
        for (int i = 0; i < projectiles.size; i ++) {
            if (projectiles.get(i).isRemoved()) {
                projectiles.removeIndex(i);
            }
        }

        for (int i = triggerZones.size - 1; i >= 0; i--) {
            if (triggerZones.get(i).isRemoved()) {
                triggerZones.removeIndex(i);
            }
        }
    }

    public void render(ShapeRenderer shapes) {
        // One begin-end for all entities
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < triggerZones.size; i++) {
            TriggerZone zone = triggerZones.get(i);
            if (!zone.isRemoved()) {
                zone.render(shapes);
            }
        }

        for (int i = 0; i < projectiles.size; i ++) {
            projectiles.get(i).render(shapes);
        }

        player.render(shapes);
        shapes.end();
    }

    public Player getPlayer() {
        return player;
    }

    public void addTriggerZone(TriggerZone zone) {
        triggerZones.add(zone);
    }

    public Array<TriggerZone> getTriggerZones() {
        return triggerZones;
    }
}
