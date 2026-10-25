package io.team0xB;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

// temporary shape rendering will replace with sprites later
public class Enemy {
    public void draw(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.CORAL);
        renderer.circle(100, 100, 50);
        renderer.end();
    }

    public void dispose(ShapeRenderer renderer){
        renderer.dispose();
    }
}
