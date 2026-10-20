package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MenuScreen extends ScreenAdapter {
    // kept so button listeners can call game.switchScreen(...)
    private final Main game;
    // the container that holds, draws and gives input to all UI elements
    private final Stage stage;

    public MenuScreen(final Main game) {
        this.game = game;
        Skin skin = game.getSkin();

        // 1280x720 is a virtual UI size, not pixels; ExtendViewport scales it to any window
        stage = new Stage(new ExtendViewport(1280, 720));

        // STEP 1: text. A Label is just text drawn with the skin's "default" label style
        Label title = new Label("Goose Escape", skin);
        title.setFontScale(1.5f);

        // STEP 2: buttons. Each uses the skin's "default" button style
        TextButton playButton = new TextButton("Play", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        // STEP 3: A listener runs when the button is clicked
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchScreen(new ControlsScreen(game));
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // closes the window and ends the program
                Gdx.app.exit();
            }
        });

        // STEP 4: layout. A Table arranges actors in rows and columns.
        Table table = new Table();
        // make the table cover the whole stage so its contents are centred on screen
        table.setFillParent(true);

        table.add(title).padBottom(60f).row();
        table.add(playButton).width(320f).height(70f).padBottom(20f).row();
        table.add(quitButton).width(320f).height(70f);

        // STEP 5: nothing appears until the table is added to the stage
        stage.addActor(table);
    }

    @Override
    public void show() {
        // without this the stage never receives mouse clicks and buttons do nothing
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.10f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    // called when the window size changes, and once when the screen is first shown
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
