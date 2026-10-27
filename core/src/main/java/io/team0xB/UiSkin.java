package io.team0xB;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public final class UiSkin {
    private UiSkin() {}

    public static Skin create() {
        Skin skin = new Skin();

        float pixelScale = Math.min(
            Gdx.graphics.getBackBufferWidth() / UiConstants.WIDTH,
            Gdx.graphics.getBackBufferHeight() / UiConstants.HEIGHT);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AtkinsonHyperlegible-Regular.ttf"));
        BitmapFont font = createFont(generator, UiConstants.DEFAULT_FONT_SIZE, pixelScale);
        BitmapFont titleFont = createFont(generator, UiConstants.TITLE_FONT_SIZE, pixelScale);

        generator.dispose();

        skin.add("default-font", font);
        skin.add("title-font", font);

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

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        skin.add("title", titleStyle);

        return skin;
    }

    private static BitmapFont createFont(FreeTypeFontGenerator generator, int virtualSize, float pixelScale) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // render at the real pixel size, so the text is not blurry
        parameter.size = Math.round(virtualSize * pixelScale);
        // account for possible resize
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;

        BitmapFont font = generator.generateFont(parameter);
        // shrink back so that the text is virtualSize units tall in the UI
        font.getData().setScale(1f / pixelScale);
        return font;
    }
}
