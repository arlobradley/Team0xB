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

    private static final float VIEW_WIDTH = 16f;
    private static final float VIEW_HEIGHT = 9f;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final ExtendViewport viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();

    public static final float GAME_DURATION_SECONDS = 300f;
    private final CountdownTimer timer = new CountdownTimer(GAME_DURATION_SECONDS);
    private final Hud hud;

    private final GameWorld world = new GameWorld();

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

        timer.update(delta);
        // Raw mouse position in pixels, then converted to world units through the camera and viewport
        aim.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(aim);
        world.update(delta, aim);

        hud.setTimeRemaining(timer.getWholeSecondsRemaining());

        // centre the camera on the player, then apply the viewport (which also updates the camera)
        camera.position.set(world.getPlayer().position, 0f);
        viewport.apply();

        Gdx.gl.glClearColor(1f, 0.4f, 0.7f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tells the renderer to draw in world units, as seen through the camera
        shapes.setProjectionMatrix(camera.combined);

        drawGrid();
        world.render(shapes);
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
