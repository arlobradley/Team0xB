package io.team0xB;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
    private Skin skin;

    @Override
    public void create() {
        skin = UiSkin.create();
        setScreen(new MenuScreen(this));
    }

    public Skin getSkin() {
        return skin;
    }

    public void switchScreen(Screen next) {
        final Screen old = getScreen();
        setScreen(next);
        if (old != null) {
            Gdx.app.postRunnable(old::dispose);
        }
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        super.render();
    }

    @Override
    public void dispose() {
        Screen current = getScreen();
        super.dispose();
        if (current != null) current.dispose();
        skin.dispose();
    }
}
