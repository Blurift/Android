package com.game.ECS.Other;

import com.badlogic.gdx.Gdx;

/**
 * Created by Sean on 25/04/2015.
 *
 * The variables that stay the same, needed through out the game
 *
 */
public class GameVars {
    public static final float PTM = 32f; //Pixels to metres
    public static final float DAMPING = 9f; //The damping for all Box2D objects
    public static final float VIRTUAL_HEIGHT = 8;
    public static final float VIRTUAL_WIDTH = VIRTUAL_HEIGHT * Gdx.graphics.getWidth() /
            Gdx.graphics.getHeight();
}
