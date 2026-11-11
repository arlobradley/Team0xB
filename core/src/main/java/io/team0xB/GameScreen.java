package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameScreen extends ScreenAdapter {
    private final Main game;

    private static final float VIEW_WIDTH = 24f;
    private static final float VIEW_HEIGHT = 13.5f;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final ExtendViewport viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();

    public static final float GAME_DURATION_SECONDS = 300f;
    private final CountdownTimer timer = new CountdownTimer(GAME_DURATION_SECONDS);
    private final Hud hud;

    private final GameMap map = new GameMap();
    private final GameWorld world = new GameWorld(map);
    private final EventManager eventManager = new EventManager(world);

    private final Vector2 aim = new Vector2();

    public GameScreen(Main game) {
        this.game = game;
        this.hud = new Hud(game.getSkin());
    }

    @Override
    public void show() {
        // the menu's stage was the input processor; clear it so its buttons stop receiving input
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.switchScreen(new MenuScreen(game));
            return;
        }

        // After a stall, a huge delta would make everything jump; cap the step size
        delta = Math.min(delta, 1f / 30f);

        boolean frozen = eventManager.isGameFrozen();

        if (!frozen) {
            timer.update(delta);
            aim.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(aim);
            world.update(delta, aim);
        }

        hud.setTimeRemaining(timer.getWholeSecondsRemaining());
        hud.updateInventory(world.getPlayer().getInventory());
        if (timer.isFinished()) {
            // later: switch to the lose screen
        }

        eventManager.update(delta);

        CutsceneEvent cutscene = eventManager.getCutscene();

        if (cutscene != null) {
            hud.showTextbox(cutscene.getCurrentText());
        } else {
            hud.hideTextbox();
        }

        TimedEvent activeEvent = eventManager.getActiveEvent();

        if (activeEvent != null) {
            hud.showNotification(activeEvent.getMessage(), activeEvent.getSecondsRemaining());
        } else {
            hud.hideNotification();
        }

        //Clamping camera to map size
        float halfWidth = viewport.getWorldWidth() / 2f;
        float halfHeight = viewport.getWorldHeight() / 2f;
        Vector2 position = world.getPlayer().position;
        camera.position.set(
            MathUtils.clamp(position.x, halfWidth, map.getWidth() - halfWidth),
            MathUtils.clamp(position.y, halfHeight, map.getHeight() - halfHeight),
            0f);
        viewport.apply();

        Gdx.gl.glClearColor(1f, 0.4f, 0.7f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.render(camera);
        shapes.setProjectionMatrix(camera.combined);
        world.render(shapes);

        shapes.setProjectionMatrix(camera.combined);
        world.render(shapes);

        hud.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hud.resize(width, height);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        hud.dispose();
        map.dispose();
    }
}
