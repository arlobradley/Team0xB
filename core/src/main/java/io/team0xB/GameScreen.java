package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameScreen extends ScreenAdapter {
    private static final float VIEW_WIDTH = 24f;
    private static final float VIEW_HEIGHT = 13.5f;
    public static final float GAME_DURATION_SECONDS = 300f;
    private static final boolean SHOW_DEBUG_GRID = false;

    private final Main game;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final ExtendViewport viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final Animator animator = new Animator();

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

        // after a stall, a huge delta would make everything jump; cap the step size
        delta = Math.min(delta, 1f / 30f);

        // Freeze the game when the timer is finished, change to introduce proper game-over
        boolean gameOver = timer.isFinished();
        boolean frozen = eventManager.isGameFrozen() || gameOver;

        if (!frozen) {
            timer.update(delta);
            animator.updateAnimationTimer(delta);

            aim.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(aim);
            world.update(delta, aim, VIEW_WIDTH, VIEW_HEIGHT);
        }

        hud.setTimeRemaining(timer.getWholeSecondsRemaining());
        hud.updateInventory(world.getPlayer().getInventory());
        hud.setCounters(world.getStats().getEventsTriggered(), world.getStats().getItemsUsed());
        if (timer.isFinished()) {
            // later: switch to the lose screen
        }

        if (!gameOver) {
            eventManager.update(delta);
        }

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

        // follow the player, but never show anything outside the map
        float halfWidth = viewport.getWorldWidth() / 2f;
        float halfHeight = viewport.getWorldHeight() / 2f;
        Vector2 position = world.getPlayer().position;
        camera.position.set(
            clampAxis(position.x, halfWidth, map.getWidth()),
            clampAxis(position.y, halfHeight, map.getHeight()),
            0f);
        viewport.apply();

        Gdx.gl.glClearColor(1f, 0.4f, 0.7f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.render(camera);

        shapes.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        if (SHOW_DEBUG_GRID) {
            drawGrid();
        }
        world.render(shapes, spriteBatch, animator, delta);

        hud.render(delta);
    }

    // clamps the camera centre on one axis; if the map is smaller than the view, centre on the map instead
    private float clampAxis(float value, float halfView, float mapSize) {
        if (mapSize <= halfView * 2f) {
            return mapSize / 2f;
        }
        return MathUtils.clamp(value, halfView, mapSize - halfView);
    }

    private void drawGrid() {
        float halfWidth = viewport.getWorldWidth() / 2f;
        float halfHeight = viewport.getWorldHeight() / 2f;
        int minX = MathUtils.floor(camera.position.x - halfWidth);
        int maxX = MathUtils.ceil(camera.position.x + halfWidth);
        int minY = MathUtils.floor(camera.position.y - halfHeight);
        int maxY = MathUtils.ceil(camera.position.y + halfHeight);

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(0.85f, 0.30f, 0.55f, 1f);
        for (int x = minX; x <= maxX; x++) shapes.line(x, minY, x, maxY);
        for (int y = minY; y <= maxY; y++) shapes.line(minX, y, maxX, y);
        shapes.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hud.resize(width, height);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        spriteBatch.dispose();
        animator.dispose();
        hud.dispose();
        map.dispose();
    }
}
