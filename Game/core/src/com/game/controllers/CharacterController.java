package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Keirron on 22/03/2015.
 */
public class CharacterController {
    private float movementX = 0;
    private float movementY = 0;

    private float moveSpeed = 6f; //Working out how to scale this to meters instead of pixels

    private Vector2 pos;

    private Texture texture;
    private TextureRegion currentRegion; //used for later animation
    private Sprite charSprite;

    private Rectangle collision;


    public CharacterController(String spriteName, Vector2 startPos)
    {

        this.pos = startPos;

        texture = new Texture(Gdx.files.internal(spriteName));
        currentRegion = new TextureRegion(texture);
        charSprite = new Sprite(currentRegion);
        charSprite.setSize(2f, 2f);
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);
        Rectangle collision = new Rectangle(pos.x, pos.y, 2f, 1f);
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
        collision.setX(pos.x);
        collision.setY(pos.y);
    }

    //Updates the velocity from touchpad widget
    public void updateVelocity(float movementX, float movementY){
        this.movementX = movementX * moveSpeed;
        this.movementY = movementY * moveSpeed;
    }

    public void dispose(){
        texture.dispose();
    }

    //Getters
    public Rectangle getCollision(){
        return collision;
    }

}
