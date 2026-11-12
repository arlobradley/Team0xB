package io.team0xB;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class TriggerZone extends Entity {
    private final String[] pages;

    public TriggerZone(float x, float y, float radius, String... pages) {
        super(x, y, radius);
        this.pages = pages;
    }

    public String[] getPages() {
        return pages;
    }

    public boolean contains(Vector2 point) {
        return position.dst2(point) <= getRadius() * getRadius();
    }

    @Override
    public void update(float delta) {}

    @Override()
    public void render(ShapeRenderer shapes) {
        float outer = getRadius();
        float inner = outer * 0.75f;

        shapes.setColor(0.05f, 0.10f, 0.35f, 1f);
        shapes.rect(position.x - outer, position.y - outer, outer * 2f, outer * 2f);

        shapes.setColor(0.10f, 0.75f, 0.75f, 1f);
        shapes.rect(position.x - inner, position.y - inner, inner * 2f, inner * 2f);
    }
}
