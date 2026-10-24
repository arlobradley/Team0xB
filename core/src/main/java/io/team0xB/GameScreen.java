package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameScreen extends ScreenAdapter {
    private final Main game;

    private static final float VIEW_WIDTH = 16f;
    private static final float VIEW_HEIGHT = 9f;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final ExtendViewport viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();

    private final Player player = new Player();

    public static final float GAME_DURATION_SECONDS = 300f;
    private final CountdownTimer timer = new CountdownTimer(GAME_DURATION_SECONDS);
    private final Hud hud;

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

        timer.update(delta);
        player.update(delta);
        hud.setTimeRemaining(timer.getWholeSecondsRemaining());

        // centre the camera on the player, then apply the viewport (which also updates the camera)
        camera.position.set(player.position, 0f);
        viewport.apply();

        Gdx.gl.glClearColor(1f, 0.4f, 0.7f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tells the renderer to draw in world units, as seen through the camera
        shapes.setProjectionMatrix(camera.combined);

        drawGrid();
        drawPlayer();
        hud.render(delta);
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

    private void drawPlayer() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(0.05f, 0.10f, 0.35f, 1f);
        shapes.circle(player.position.x, player.position.y, Player.RADIUS, 24);

        shapes.setColor(1f, 1f, 1f, 1f);
        shapes.circle(
            player.position.x + player.facing.x * Player.RADIUS * 0.7f,
            player.position.y + player.facing.y * Player.RADIUS * 0.7f,
            0.12f,
            12
            );

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
        hud.dispose();
    }
}
