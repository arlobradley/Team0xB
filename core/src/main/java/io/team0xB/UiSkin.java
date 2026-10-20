package io.team0xB;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public final class UiSkin {
    private UiSkin() {}

    public static Skin create() {
        Skin skin = new Skin();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2f);
        skin.add("default-font", font);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        Drawable normal = skin.newDrawable("white", new Color(0.10f, 0.20f, 0.50f, 1f));
        Drawable hovered = skin.newDrawable("white", new Color(0.20f, 0.35f, 0.75f, 1f));
        Drawable pressed = skin.newDrawable("white", new Color(0.05f, 0.10f, 0.30f, 1f));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = normal;
        buttonStyle.over = hovered;
        buttonStyle.down = pressed;
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        skin.add("default", buttonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", labelStyle);

        return skin;
    }
}
