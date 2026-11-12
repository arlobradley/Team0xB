package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;


public class Animator {
    public enum AnimKey{
        ENEMY,
        PLAYER;
    }

    // Storing spriteSheets and animations as Map for easy access and to iterate through during dispose()
    private final EnumMap<AnimKey, Texture> textures = new EnumMap<>(AnimKey.class);
    private final EnumMap<AnimKey, Animation<TextureRegion>> animations = new EnumMap<>(AnimKey.class);

    public Animator() {
        //Textures
        textures.put(AnimKey.ENEMY, new Texture(Gdx.files.internal("sprite-sheets/goose-sheet.png")));

        //Animations

        // Enemy Animation
        float EnemyFrameDuration = 0.030f;
        int F_COLS_ENEMY = 5;
        int F_ROWS_ENEMY = 5;
        TextureRegion[] enemyFrames =  sheetToRegionArray(textures.get(AnimKey.ENEMY), F_ROWS_ENEMY, F_COLS_ENEMY, 1);
        animations.put(AnimKey.ENEMY, new Animation<>(EnemyFrameDuration, enemyFrames));
    }

    private TextureRegion[] sheetToRegionArray(Texture texture, int rows, int cols, int missingFromLastRow){
        TextureRegion[] outputFrames = new TextureRegion[rows * cols - missingFromLastRow];

        TextureRegion[][] splitSheet = TextureRegion.split(texture,
            texture.getWidth() / cols,
            texture.getHeight() / rows);

        int currentSprite = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == rows - 1 && j >= cols - missingFromLastRow){
                    continue; // Skipping empty cells of the sprite sheet
                }
                outputFrames[currentSprite] = splitSheet[i][j];
                currentSprite++;
            }
        }
        return outputFrames;
    }

    public TextureRegion getNextFrame(AnimKey animation, float offset,float animTime, float delta){
        TextureRegion nextFrame = animations.get(animation).getKeyFrame(animTime + offset * delta, true);
        return nextFrame;
    }


    public void dispose(){
        for (Texture t: textures.values()){
            t.dispose();
        }
    }
}


