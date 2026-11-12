package io.team0xB;

public class Inventory {
    public static final int SLOT_COUNT = 5;
    private final Item[] slots = new Item[SLOT_COUNT];

    public boolean add(Item item) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] == null) {
                slots[i] = item;
                return true;
            }
        }

        return false;
    }

    // null = slot is empty
    public Item get(int slot) {
        return slots[slot];
    }

    public void remove(int slot) {
        slots[slot] = null;
    }

    public boolean use(int slot, GameWorld world) {
        Item item = slots[slot];
        return item != null && item.tryUse(world);
    }

    public void update(float delta) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] != null) {
                slots[i].update(delta);
            }
        }
    }
}
