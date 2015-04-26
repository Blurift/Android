package com.game.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Sean on 14/04/2015.
 *
 * Usually a static object taken from the game map, used to organize depth
 *
 */
public class SpriteDepthObject implements IDepthObject{
    private Sprite sprite;

    public SpriteDepthObject(Sprite sprite){
        this.sprite = sprite;
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    //Getters
    public float getY(){
        return sprite.getY();
    }

}
