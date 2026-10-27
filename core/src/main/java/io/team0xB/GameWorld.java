package io.team0xB;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameWorld {
    private final Player player = new Player();

    public void update(float delta) {
        player.update(delta);
    }

    public void render(ShapeRenderer shapes) {
        // one begin-end for all entities
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        player.render(shapes);
        shapes.end();
    }

    public Player getPlayer() {
        return player;
    }
}
