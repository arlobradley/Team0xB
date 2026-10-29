package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class CutsceneEvent extends Event {
    private final String[] pages;
    private int pageIndex = 0;

    public CutsceneEvent(String... pages) {
        this.pages = pages;
    }

    @Override
    public void start(GameWorld world) {}

    @Override
    public void end(GameWorld world) {}

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            pageIndex++;
        }
    }

    @Override
    public boolean isFinished() {
        return pageIndex >= pages.length;
    }

    public String getCurrentText() {
        return pages[Math.min(pageIndex, pages.length - 1)];
    }
}
