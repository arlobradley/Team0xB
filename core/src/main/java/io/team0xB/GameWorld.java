package io.team0xB;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class GameWorld {
    private final Player player = new Player(new GameMap());
    private final Array<Projectile> projectiles = new Array<>();
    private final Array<TriggerZone> triggerZones = new Array<>();
    private final Array<ItemEffect> effects = new Array<>();

    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final EnemySpawner spawner = new EnemySpawner();

    private final GameStats stats = new GameStats();

    public GameWorld(GameMap map) {
        Array<GameMap.TriggerPoint> points = map.getTriggerPoints();

        for (int i = 0; i < points.size; i++) {
            GameMap.TriggerPoint point = points.get(i);
            triggerZones.add(new TriggerZone(point.x(), point.y(), point.radius(), point.pages()));
        }

        player.getInventory().add(new BurstItem());
        player.getInventory().add(new OrbitItem());
    }

    // aimPoint is the cursor position in world coordinates
    public void update(float delta, Vector2 aimPoint, float viewWidth, float viewHeight) {
        player.update(delta);

        Projectile shot = player.tryFire(aimPoint);
        if (shot != null) {
            projectiles.add(shot);
        }

        int slot = player.pollItemKey();
        if (slot >= 0 && player.getInventory().use(slot, this)) {
            stats.recordItemUse();
        }

        for (int i = 0; i < effects.size; i++) {
            effects.get(i).update(delta);
        }

        for (int i = 0; i < projectiles.size; i++) {
            projectiles.get(i).update(delta);
        }

        enemies.addAll(spawner.spawn(player.position, viewWidth, viewHeight, delta));

        for (Enemy enemy : enemies){
            enemy.update(player.position, delta);
        }

        float BULLET_DAMAGE = 5f;
        for (Projectile projectile : projectiles){
            for (Enemy enemy : enemies){
                if (enemy.overlaps(projectile, 120f)){
                    enemy.damage(BULLET_DAMAGE);
                }
            }
        }

        removeFinishedEntities();
    }

    private void removeFinishedEntities() {
        for (int i = effects.size - 1; i >= 0; i--) {
            if (effects.get(i).isFinished()) {
                effects.removeIndex(i);
            }
        }

        for (int i = 0; i < projectiles.size; i ++) {
            if (projectiles.get(i).isRemoved()) {
                projectiles.removeIndex(i);
            }
        }

        enemies.removeIf(Entity::isRemoved);

        for (int i = triggerZones.size - 1; i >= 0; i--) {
            if (triggerZones.get(i).isRemoved()) {
                triggerZones.removeIndex(i);
            }
        }

    }

    public void render(ShapeRenderer shapes, SpriteBatch sprites, Animator animator, float delta) {
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


        // Make sure no Shape based drawing happens in the section below (causes all sprites to fail to render)
        sprites.begin(); // All sprite + sprite animation drawing should be within this section
        sprites.enableBlending(); // blending is required because the sprites have transparency

        for (Enemy enemy : enemies){
            enemy.render(animator, sprites, delta);
        }
        sprites.disableBlending();
        sprites.end();
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

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void addEffect(ItemEffect effect) {
        effects.add(effect);
    }

    public GameStats getStats() {
        return stats;
    }
}
