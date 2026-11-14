package io.team0xB;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Locale;

public class Hud implements Disposable {
    private static final float TIMER_TEXT_WIDTH = 120f;
    private static final float TIMER_PAD_VERTICAL = 8f;
    private static final float TIMER_PAD_HORIZONTAL = 28f;
    private static final float TIMER_TOP_MARGIN = 16f;
    private static final float BACKGROUND_ALPHA = 0.5f;

    private static final float NOTIFICATION_TEXT_WIDTH = 600f;
    private static final float NOTIFICATION_PAD_VERTICAL = 8f;
    private static final float NOTIFICATION_PAD_HORIZONTAL = 24f;
    private static final float NOTIFICATION_TOP_GAP = 10f;

    private static final float SLOT_WIDTH = 170f;
    private static final float SLOT_HEIGHT = 100f;
    private static final float SLOT_GAP = 12f;
    private static final float SLOT_PADDING = 8f;
    private static final float SLOT_BOTTOM_MARGIN = 16f;

    private static final float TEXTBOX_HEIGHT = 260f;
    private static final float TEXTBOX_MARGIN = 30f;
    private static final float TEXTBOX_BORDER = 4f;
    private static final float TEXTBOX_PADDING = 28f;

    private static final float COUNTER_MARGIN = 16f;
    private static final float COUNTER_PADDING = 10f;
    private static final float COUNTER_TEXT_WIDTH = 280f;

    private final Stage stage = new Stage(new ExtendViewport(UiConstants.WIDTH, UiConstants.HEIGHT));
    private final Label timerLabel;
    private final Label notificationLabel;
    private final Table notificationBox;
    private final Label textboxLabel;
    private final Table textboxRoot;
    private final Table[] slotBoxes = new Table[Inventory.SLOT_COUNT];
    private final Label[] slotNameLabels = new Label[Inventory.SLOT_COUNT];
    private final Label[] slotCooldownLabels = new Label[Inventory.SLOT_COUNT];
    private final Drawable slotEmptyBackground;
    private final Drawable slotReadyBackground;
    private final Drawable slotCooldownBackground;
    private int shownSeconds = -1;
    private String shownNotification = "";

    private final Label eventsCounterLabel;
    private final Label itemsCounterLabel;
    private int shownEvents = -1;
    private int shownItemsUsed = -1;

    public Hud(Skin skin) {
        Drawable transparentBlack = skin.newDrawable("white", new Color(0f, 0f, 0f, BACKGROUND_ALPHA));

        timerLabel = new Label("05:00", skin);
        timerLabel.setAlignment(Align.center);

        Table timerBox = new Table();
        timerBox.setBackground(transparentBlack);
        timerBox.add(timerLabel)
            .width(TIMER_TEXT_WIDTH)
            .pad(TIMER_PAD_VERTICAL, TIMER_PAD_HORIZONTAL, TIMER_PAD_VERTICAL, TIMER_PAD_HORIZONTAL);

        notificationLabel = new Label("", skin);
        notificationLabel.setAlignment(Align.center);

        notificationBox = new Table();
        notificationBox.setBackground(transparentBlack);
        notificationBox.add(notificationLabel)
            .width(NOTIFICATION_TEXT_WIDTH)
            .pad(NOTIFICATION_PAD_VERTICAL, NOTIFICATION_PAD_HORIZONTAL, NOTIFICATION_PAD_VERTICAL, NOTIFICATION_PAD_HORIZONTAL);
        notificationBox.setVisible(false);

        Table topRoot = new Table();
        topRoot.setFillParent(true);
        topRoot.top();
        topRoot.add(timerBox).padTop(TIMER_TOP_MARGIN).row();
        topRoot.add(notificationBox).padTop(NOTIFICATION_TOP_GAP);
        stage.addActor(topRoot);

        eventsCounterLabel = new Label("Events: 0", skin);
        itemsCounterLabel = new Label("Items used: 0", skin);

        Table counterBox = new Table();
        counterBox.setBackground(transparentBlack);
        counterBox.pad(COUNTER_PADDING);
        counterBox.add(eventsCounterLabel).width(COUNTER_TEXT_WIDTH).left().row();
        counterBox.add(itemsCounterLabel).width(COUNTER_TEXT_WIDTH).left();

        Table counterRoot = new Table();
        counterRoot.setFillParent(true);
        counterRoot.top().left();
        counterRoot.add(counterBox).pad(COUNTER_MARGIN);
        stage.addActor(counterRoot);

        // Slot backgrounds differ in brightness, not only hue: empty is faint, ready is dark, cooling down is grey
        slotEmptyBackground = skin.newDrawable("white", new Color(0f, 0f, 0f, 0.25f));
        slotReadyBackground = skin.newDrawable("white", new Color(0f, 0f, 0f, 0.6f));
        slotCooldownBackground = skin.newDrawable("white", new Color(0.3f, 0.3f, 0.3f, 0.85f));

        Table slotRow = new Table();
        for (int i = 0; i < Inventory.SLOT_COUNT; i++) {
            Label keyLabel = new Label(String.valueOf(i + 1), skin);

            slotCooldownLabels[i] = new Label("", skin);
            slotNameLabels[i] = new Label("", skin);
            slotNameLabels[i].setAlignment(Align.center);
            slotNameLabels[i].setEllipsis(true);

            Table box = new Table();
            box.setBackground(slotEmptyBackground);
            box.pad(SLOT_PADDING);
            box.add(keyLabel).left().expandX();
            box.add(slotCooldownLabels[i]).right().row();
            box.add(slotNameLabels[i]).colspan(2).growX();
            slotBoxes[i] = box;

            slotRow.add(box).width(SLOT_WIDTH).height(SLOT_HEIGHT).padLeft(i == 0 ? 0f : SLOT_GAP);
        }

        Table inventoryRoot = new Table();
        inventoryRoot.setFillParent(true);
        inventoryRoot.bottom().padBottom(SLOT_BOTTOM_MARGIN);
        inventoryRoot.add(slotRow);
        stage.addActor(inventoryRoot);

        BitmapFont font = skin.getFont("default-font");
        Label.LabelStyle darkStyle = new Label.LabelStyle(font, Color.BLACK);

        textboxLabel = new Label("", darkStyle);
        textboxLabel.setWrap(true);
        textboxLabel.setAlignment(Align.topLeft);

        Label textboxHint = new Label("Press SPACE to continue", darkStyle);

        Table whiteBox = new Table();
        whiteBox.setBackground(skin.newDrawable("white", Color.WHITE));
        whiteBox.pad(TEXTBOX_PADDING);
        whiteBox.add(textboxLabel).grow().top().left().row();
        whiteBox.add(textboxHint).right();

        Table borderBox = new Table();
        borderBox.setBackground(skin.newDrawable("white", Color.BLACK));
        borderBox.pad(TEXTBOX_BORDER);
        borderBox.add(whiteBox).grow();

        textboxRoot = new Table();
        textboxRoot.setFillParent(true);
        textboxRoot.bottom();
        textboxRoot.add(borderBox).growX().height(TEXTBOX_HEIGHT).pad(TEXTBOX_MARGIN);
        textboxRoot.setVisible(false);
        stage.addActor(textboxRoot);
    }

    public void setTimeRemaining(int totalSeconds) {
        if (totalSeconds == shownSeconds) return;
        shownSeconds = totalSeconds;
        timerLabel.setText(String.format(Locale.ROOT, "%02d:%02d", totalSeconds / 60, totalSeconds % 60));
    }

    public void showNotification(String message, int secondsRemaining) {
        String text = message + " (" + secondsRemaining + "s)";

        if (!text.equals(shownNotification)) {
            shownNotification = text;
            notificationLabel.setText(text);
        }

        notificationBox.setVisible(true);
    }

    public void hideNotification() {
        notificationBox.setVisible(false);
    }

    // Reads the inventory each frame; the HUD never changes it
    public void updateInventory(Inventory inventory) {
        for (int i = 0; i < Inventory.SLOT_COUNT; i++) {
            Item item = inventory.get(i);

            if (item == null) {
                slotNameLabels[i].setText("");
                slotCooldownLabels[i].setText("");
                slotBoxes[i].setBackground(slotEmptyBackground);
            } else if (item.isReady()) {
                slotNameLabels[i].setText(item.getName());
                slotCooldownLabels[i].setText("");
                slotBoxes[i].setBackground(slotReadyBackground);
            } else {
                slotNameLabels[i].setText(item.getName());
                slotCooldownLabels[i].setText(item.getSecondsRemaining() + "s");
                slotBoxes[i].setBackground(slotCooldownBackground);
            }
        }
    }

    public void showTextbox(String text) {
        textboxLabel.setText(text);
        textboxRoot.setVisible(true);
    }

    public void hideTextbox() {
        textboxRoot.setVisible(false);
    }

    public void setCounters(int eventsTriggered, int itemsUsed) {
        if (eventsTriggered != shownEvents) {
            shownEvents = eventsTriggered;
            eventsCounterLabel.setText("Events: " + eventsTriggered);
        }
        if (itemsUsed != shownItemsUsed) {
            shownItemsUsed = itemsUsed;
            itemsCounterLabel.setText("Items used: " + itemsUsed);
        }
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
