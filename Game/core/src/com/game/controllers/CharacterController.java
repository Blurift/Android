package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Keirron on 22/03/2015.
 */
public class CharacterController {
    private int movementY = 0;
    private int movementX = 0;

    private Vector2 pos;

    Texture texture;
    Sprite sprite;

    public CharacterController(String spriteName, Vector2 pos)
    {
        this.pos = pos;

        texture = new Texture(Gdx.files.internal(spriteName));
        sprite = new Sprite(texture);
        sprite.setPosition(pos.x,pos.y);

    }

    //Getters
    public Vector2 GetPosition() { return pos; }

    //Setters
    public void SetPosition(Vector2 pos) { this.pos = pos; }

}
