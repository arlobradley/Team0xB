package io.team0xB;

import com.badlogic.gdx.utils.Array;

public class EventManager {
    private static final float BOOST_START_SECONDS = 10f;
    private static final float BOOST_DURATION_SECONDS = 8f;
    private static final float BOOST_MULTIPLIER = 1.5f;

    private final GameWorld world;
    private final CountdownTimer boostStartTimer = new CountdownTimer(BOOST_START_SECONDS);
    private TimedEvent activeEvent = null;
    private CutsceneEvent cutscene = null;
    private boolean boostStarted = false;

    public EventManager(GameWorld world) {
        this.world = world;
    }

    public void update(float delta) {
        if (cutscene != null) {
            cutscene.update(delta);

            if (cutscene.isFinished()) {
                cutscene.end(world);
                cutscene = null;
            }

            return;
        }

        checkTriggerZones();
        if (cutscene != null) return;

        if (activeEvent != null) {
            activeEvent.update(delta);

            if (activeEvent.isFinished()) {
                activeEvent.end(world);
                activeEvent = null;
            }

            return;
        }

        if (boostStarted) return;

        boostStartTimer.update(delta);

        if (boostStartTimer.isFinished()) {
            boostStarted = true;
            startEvent(new SpeedChangeEvent(
                "Energy boost! Speed up",
                BOOST_MULTIPLIER,
                BOOST_DURATION_SECONDS));
        }
    }

    public void startEvent(TimedEvent event) {
        if (activeEvent != null) return;

        activeEvent = event;
        activeEvent.start(world);
        world.getStats().recordEvent();
    }

    private void checkTriggerZones() {
        Array<TriggerZone> zones = world.getTriggerZones();
        Player player = world.getPlayer();

        for (int i = 0; i < zones.size; i++) {
            TriggerZone zone = zones.get(i);
            if (zone.isRemoved() || !zone.contains(player.position)) continue;

            cutscene = new CutsceneEvent(zone.getPages());
            cutscene.start(world);
            world.getStats().recordEvent();

            zone.remove();
            return;
        }
    }

    public boolean isGameFrozen() {
        return cutscene != null;
    }

    public TimedEvent getActiveEvent() {
        return activeEvent;
    }

    public CutsceneEvent getCutscene() {
        return cutscene;
    }
}
