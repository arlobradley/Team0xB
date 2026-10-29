package io.team0xB;

import com.badlogic.gdx.graphics.Color;
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

    private static final float NOTIFICATION_TEXT_WIDTH = 600f;
    private static final float NOTIFICATION_PAD_VERTICAL = 8f;
    private static final float NOTIFICATION_PAD_HORIZONTAL = 24f;
    private static final float NOTIFICATION_TOP_GAP = 10f;

    private final Stage stage = new Stage(new ExtendViewport(UiConstants.WIDTH, UiConstants.HEIGHT));
    private final Label timerLabel;
    private final Label notificationLabel;
    private final Table notificationBox;
    private int shownSeconds = -1;
    private String shownNotification = "";

    public Hud(Skin skin) {
        Drawable transparentBlack = skin.newDrawable("white", new Color(0f, 0f, 0f, 0.5f));

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

        Table root = new Table();
        root.setFillParent(true);
        root.top();
        root.add(timerBox).padTop(TIMER_TOP_MARGIN).row();
        root.add(notificationBox).padTop(NOTIFICATION_TOP_GAP);

        stage.addActor(root);
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
