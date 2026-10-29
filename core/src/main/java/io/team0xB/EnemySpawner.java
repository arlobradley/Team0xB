package io.team0xB;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;

public class EnemySpawner {
    private int rate = 3;
    private final float SPAWN_DELAY = 3f;
    private float spawnTimer = SPAWN_DELAY;
    private final Random random = new Random();

    public ArrayList<Enemy> spawn(Vector2 playerPosition, float screenWidth, float screenHeight, float delta){

        if (spawnTimer > 0){
            spawnTimer -= 1 * delta;
            return new ArrayList<>();
        }

        spawnTimer = SPAWN_DELAY;
        ArrayList<Enemy> spawnedEnemies = new ArrayList<>();
        for (int i = 0; i < rate; i++) {
            float SPAWN_OFFSET_BOUNDS = 5f;
            Vector2 spawnPositon = new Vector2(playerPosition);
            Vector2 spawnOffset = new Vector2();
            spawnOffset.x = random.nextFloat(-SPAWN_OFFSET_BOUNDS, SPAWN_OFFSET_BOUNDS);
            spawnOffset.y = random.nextFloat(-SPAWN_OFFSET_BOUNDS, SPAWN_OFFSET_BOUNDS);

            // offset enemy spawns so they spawn in offscreen
            if (spawnOffset.x < 0){
                spawnOffset.x -= screenWidth;
            } else {
                spawnOffset.x += screenWidth;
            }

            if (spawnOffset.y < 0){
                spawnOffset.y -= screenHeight;
            } else {
                spawnOffset.y += screenHeight;
            }
            spawnPositon.add(spawnOffset);
            spawnedEnemies.add(new Enemy(10, spawnPositon));
        }
        return  spawnedEnemies;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}

