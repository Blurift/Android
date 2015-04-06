package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Keirron on 22/03/2015.
 */
public class AIController {

    private Vector2 pos;

    private Texture texture;
    private TextureRegion currentRegion; //used for later animation
    private Sprite charSprite;

    public AIController(String spriteName, Vector2 startPos) {

        this.pos = startPos;

        texture = new Texture(Gdx.files.internal(spriteName));
        currentRegion = new TextureRegion(texture);
        charSprite = new Sprite(currentRegion);
        charSprite.setSize(2f, 2f);
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);

    }

    public Vector2 getPosition() { return pos; }

    public Sprite getSprite(){ return charSprite; }

    public void dispose(){
        texture.dispose();
    }

}
