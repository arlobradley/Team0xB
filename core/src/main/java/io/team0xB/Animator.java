package io.team0xB;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;


/**
 * Manages all animations running on the Game screen
 * Add new animations by:
 * - adding a sprite sheet to Assets and updating assets.txt
 * - creating an enum Label in AnimKey for the animation
 * - putting a texture into the textures Map using its path
 * - putting an animation into the animations Map
 */
public class Animator {
    public enum AnimKey{
        ENEMY,
        PLAYER;
    }

    // Storing spriteSheets and animations as Map for easy access and to iterate through during dispose()
    // Using enum maps because it was easy to make mistakes that the IDE did not pick up on
    // when accessing values using string keys.
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

    /**
     * @param texture A LibGDX Texture object with a spritesheet
     * @param rows the amount of rows in the spritesheet
     * @param cols the amount of columns in the sprite sheet
     * @param missingFromLastRow the amount of blank spaces in the last row of the sprite sheet. If all spaces are full
     *                           Use 0
     * @return A TextureRegion Array containing all the frames of the spritesheet animation in order.
     */
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

    /**
     * @param animation An enum AnimKey label that corresponds to an animation
     * @param offset The float offset of the animation relative to the global animation timer. Use with objects that
     *               have a lot of instances, like enemies or droppable items otherwise use 0.
     * @param animTime A float valute of animation time progression.
     *                 (How long has passed since all animations started playing)
     * @param delta A float value. The time since the last frame.
     * @return A TextureRegion. The current frame of the animation.
     */
    public TextureRegion getNextFrame(AnimKey animation, float offset, float animTime, float delta){
        return animations.get(animation).getKeyFrame(animTime + offset * delta, true);
    }


    /**
     * Disposes of all textures used by the program. Must be run before closing the game to avoid memory leaks.
     */
    public void dispose(){
        for (Texture t: textures.values()){
            t.dispose();
        }
    }
}


