package io.team0xB;

public class GameStats {
    private int eventsTriggered = 0;
    private int itemsUsed = 0;

    public void recordEvent() {
        eventsTriggered++;
    }

    public void recordItemUse() {
        itemsUsed++;
    }

    public int getEventsTriggered() {
        return eventsTriggered;
    }

    public int getItemsUsed() {
        return itemsUsed;
    }
}
