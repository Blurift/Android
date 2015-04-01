package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Sean on 1/04/2015.
 *
 * A filter which renders between the game world and hud, for things like making the screen darker,
 * or adding a red fading border when damaged, etc
 *
 */
public class FilterManager {
    private ShapeRenderer shapeRenderer;

    private Color colour;

    private boolean isActive = false;

    private static final Color DEFAULT_COLOUR = new Color(0,0,0,1);

    public FilterManager(){
        shapeRenderer = new ShapeRenderer();
        colour = DEFAULT_COLOUR;
    }

    //Rendering black only for now
    public void render(){
        if(isActive) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.setColor(colour);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.setColor(DEFAULT_COLOUR);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void setColour(Color colour){
        isActive = true;
        this.colour = colour;
    }

    public void clear(){
        isActive = false;
        colour = DEFAULT_COLOUR;

    }

}
