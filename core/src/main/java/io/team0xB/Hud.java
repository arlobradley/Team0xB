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

    private final Stage stage = new Stage(new ExtendViewport(UiConstants.WIDTH, UiConstants.HEIGHT));
    private final Label timerLabel;
    private int shownSeconds = -1;

    public Hud(Skin skin) {
        timerLabel = new Label("05:00", skin);
        // Centre the text inside the fixed-width cell;
        timerLabel.setAlignment(Align.center);

        // White pixel from the Skin, tinted black at 50% opacity
        Drawable transparentBlack = skin.newDrawable("white", new Color(0f, 0f, 0f, 0.5f));

        // Small table whose background is drawn behind the timer, with padding around the text
        Table timerBox = new Table();
        timerBox.setBackground(transparentBlack);
        timerBox.add(timerLabel)
            .width(TIMER_TEXT_WIDTH)
            .pad(TIMER_PAD_VERTICAL, TIMER_PAD_HORIZONTAL, TIMER_PAD_VERTICAL, TIMER_PAD_HORIZONTAL);

        // Fullscreen table that pins the timer box to the top centre
        Table root = new Table();
        root.setFillParent(true);
        root.top();
        root.add(timerBox).padTop(TIMER_TOP_MARGIN);

        stage.addActor(root);
    }

    public void setTimeRemaining(int totalSeconds) {
        if (totalSeconds == shownSeconds) return;
        shownSeconds = totalSeconds;
        timerLabel.setText(String.format(Locale.ROOT, "%02d:%02d", totalSeconds / 60, totalSeconds % 60));
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
