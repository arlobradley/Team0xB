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

    private static final float TEXTBOX_HEIGHT = 260f;
    private static final float TEXTBOX_MARGIN = 30f;
    private static final float TEXTBOX_BORDER = 4f;
    private static final float TEXTBOX_PADDING = 28f;

    private final Stage stage = new Stage(new ExtendViewport(UiConstants.WIDTH, UiConstants.HEIGHT));
    private final Label timerLabel;
    private final Label notificationLabel;
    private final Table notificationBox;
    private final Label textboxLabel;
    private final Table textboxRoot;
    private int shownSeconds = -1;
    private String shownNotification = "";

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

    public void showTextbox(String text) {
        textboxLabel.setText(text);
        textboxRoot.setVisible(true);
    }

    public void hideTextbox() {
        textboxRoot.setVisible(false);
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
