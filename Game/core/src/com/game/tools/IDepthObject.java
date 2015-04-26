package com.game.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Comparator;

/**
 * Created by Sean on 14/04/2015.
 *
 * A interface with the things that a Map needs to render an object
 * Orders sprites and particles on the Y scales, and then renders them so
 *
 */
public interface IDepthObject{

    public float getY();
    public void draw(SpriteBatch batch);
}
