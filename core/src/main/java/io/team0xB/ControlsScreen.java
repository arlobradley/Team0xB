package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ControlsScreen extends ScreenAdapter {
    private final Main game;
    private final Stage stage;

    // guards against two inputs in the same frame creating two game screens
    private boolean started = false;

    private final IntSet ignoreKeys = IntSet.with(
        Input.Keys.F11,
        Input.Keys.SHIFT_LEFT,
        Input.Keys.SHIFT_RIGHT,
        Input.Keys.CONTROL_LEFT,
        Input.Keys.CONTROL_RIGHT,
        Input.Keys.ALT_LEFT,
        Input.Keys.ALT_RIGHT
    );

    public ControlsScreen(final Main game) {
        this.game = game;
        Skin skin = game.getSkin();

        stage = new Stage(new ExtendViewport(UiConstants.WIDTH, UiConstants.HEIGHT));
        Label title = new Label("Controls", skin, "title");

        Label controls = new Label(
            "Move: W A S D or Arrow keys\n"
                + "Attack: hold left mouse button (aims at the cursor)\n"
                + "Use items: keys 1 to 5\n"
                + "Back to menu: Esc",
            skin);
        controls.setAlignment(Align.center);

        Label prompt = new Label("Press any key or click to start", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.add(title).padBottom(40f).row();
        table.add(controls).padBottom(80f).row();
        table.add(prompt);
        stage.addActor(table);

        stage.addListener(new InputListener() {
            // runs when any keyboard key is pressed down
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (ignoreKeys.contains(keycode)) {
                    return false;
                }
                startGame();
                return true;
            }

            // runs when any mouse button is pressed down
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startGame();
                return true;
            }
        });
    }

    private void startGame() {
        if (started) return;
        started = true;
        game.switchScreen(new GameScreen(game));
    }

    @Override
    public void show() {
        // the stage must be the input processor or its listener never fires
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.10f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
