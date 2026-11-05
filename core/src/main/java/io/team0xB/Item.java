package io.team0xB;

public abstract class Item {
    private final String name;
    private final CountdownTimer cooldown;

    protected Item(String name, float cooldownSeconds) {
        this.name = name;
        this.cooldown = new CountdownTimer(cooldownSeconds);
        // Start with items ready
        this.cooldown.expire();
    }

    public void update(float delta) {
        cooldown.update(delta);
    }

    public boolean isReady() {
        return cooldown.isFinished();
    }

    public int getSecondsRemaining() {
        return cooldown.getWholeSecondsRemaining();
    }

    public String getName() {
        return name;
    }

    public boolean tryUse(GameWorld world) {
        if (!isReady()) return false;

        activate(world);
        cooldown.reset();
        return true;
    }

    protected abstract void activate(GameWorld world);
}
