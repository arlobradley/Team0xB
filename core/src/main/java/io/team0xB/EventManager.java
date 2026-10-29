package io.team0xB;

import com.badlogic.gdx.math.MathUtils;

public class EventManager {
    // First event - Boost
    private static final float BOOST_START_SECONDS = 10f;
    private static final float BOOST_DURATION_SECONDS = 8f;
    private static final float BOOST_MULTIPLIER = 1.5f;

    private final GameWorld world;
    private final CountdownTimer boostStartTimer = new CountdownTimer(BOOST_START_SECONDS);
    // Only one event can be active at a time
    // null = none
    private Event activeEvent = null;
    private boolean boostStarted = false;

    public EventManager(GameWorld world) {
        this.world = world;
    }

    public void update(float delta) {
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
                "Energy boost! Speed up!",
                BOOST_MULTIPLIER,
                BOOST_DURATION_SECONDS));
        }
    }

    public void startEvent(Event event) {
        if (activeEvent != null) return;

        activeEvent = event;
        activeEvent.start(world);
    }

    public Event getActiveEvent() {
        return activeEvent;
    }
}
