package com.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Sean on 7/04/2015.
 */
public class Animator {
    public Animator(){

    }

    public Animation animate(Texture sheet, int startX, int startY, int width, int height, int length, float speed){
        TextureRegion[] frames = new TextureRegion[length];
        for(int i=0;i < frames.length; i++)
            frames[i] = new TextureRegion(sheet, startX + (width*i), startY, width, height);

        Animation animated = new Animation(speed, frames);
        return animated;
    }
}
