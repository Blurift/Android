package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Keirron on 22/03/2015.
 */
public class CharacterController {
    private float movementX = 0;
    private float movementY = 0;

    private float moveSpeed = 100f; //Working out how to scale this to meters instead of pixels

    // TODO http://blog.xoppa.com/pixels/ Think in meters, not scale... not sure how to implement for position
    private Vector2 pos;

    private Texture texture;
    private TextureRegion currentRegion; //used for later animation
    private Sprite charSprite;

    public CharacterController(String spriteName, Vector2 startPos)
    {

        this.pos = startPos;

        texture = new Texture(Gdx.files.internal(spriteName));
        currentRegion = new TextureRegion(texture);
        charSprite = new Sprite(currentRegion);
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);

    }

    //Getters
    public Vector2 getPosition() { return pos; }

    public Sprite getSprite(){ return charSprite; }

    //Setters
    public void setPosition(Vector2 pos) { this.pos = pos; }

    //Updates for the player at each frame
    public void update(float delta){
        //Update position
        pos.x = + pos.x + movementX * delta;
        pos.y = + pos.y + movementY * delta;
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);
    }

    //Updates the velocity from touchpad widget
    public void updateVelocity(float movementX, float movementY){
        this.movementX = movementX * moveSpeed;
        this.movementY = movementY * moveSpeed;
    }

    public void dispose(){
        texture.dispose();
    }
}
