package io.team0xB;

import com.badlogic.gdx.utils.Array;

public class GameMap {
    public record TriggerPoint(float x, float y, float radius, String... pages) {}

    private final Array<TriggerPoint> triggerPoints = new Array<>();

    public GameMap() {
        // Temporary hard-coded layout; later this will be read from a map file
        triggerPoints.add(new TriggerPoint(
            7f, 3f, 0.9f,
            "You are late for your lecture, and the geese have noticed you.",
            "Hold the left mouse button to scare them away. Keep moving!"));
    }

    public Array<TriggerPoint> getTriggerPoints() {
        return triggerPoints;
    }
}
