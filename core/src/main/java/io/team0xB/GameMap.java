package io.team0xB;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

public class GameMap {
    public record TriggerPoint(float x, float y, float radius, String... pages) {}

    private final Array<TriggerPoint> triggerPoints = new Array<>();

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    public GameMap() {
        map = new TmxMapLoader().load("campus.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
        // Temporary hard-coded layout; later this will be read from a map file
        triggerPoints.add(new TriggerPoint(
            32f, 22f, 0.9f,
            "You are late for your lecture, and the geese have noticed you.",
            "Hold the left mouse button to scare them away. Keep moving!"));
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public Array<TriggerPoint> getTriggerPoints() {
        return triggerPoints;
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}

