package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Rectangle;
import com.game.managers.GameManager;

/**
 * Created by Keirron on 22/03/2015.
 */
public class CharacterController {
    private float movementX = 0;
    private float movementY = 0;
    private float dirX = 0;
    private float dirY = 0;

    private float moveSpeed = 6f; //Working out how to scale this to meters instead of pixels

    private Vector2 pos;

    private Texture texture;
    private TextureRegion currentRegion; //used for later animation
    private Sprite charSprite;

    private Rectangle collision;
    private GameManager gm;

    public CharacterController(GameManager gm, String spriteName, Vector2 startPos)
    {

        this.pos = startPos;
        this.gm = gm;

        texture = new Texture(Gdx.files.internal(spriteName));
        currentRegion = new TextureRegion(texture);
        charSprite = new Sprite(currentRegion);
        charSprite.setSize(2f, 2f);
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);
        collision = new Rectangle(pos.x, pos.y, 2f, 1f);
    }

    //Getters
    public Vector2 getPosition() { return pos; }

    public Sprite getSprite(){ return charSprite; }

    //Setters
    public void setPosition(Vector2 pos) { this.pos = pos; }

    //Updates for the player at each frame
    public void update(float delta){
        //Check for collision
        boolean onlyX = false;
        boolean onlyY = false;
        String dir = "";
        Rectangle testCollide = new Rectangle(pos.x + movementX * delta, pos.y + movementY * delta, 2f, 1f);
        for (RectangleMapObject rectangleObject : gm.getMapManager().getCollidables().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            Rectangle virtualRec = new Rectangle(rectangle);
            virtualRec.setX(rectangle.getX()/32);
            virtualRec.setY(rectangle.getY()/32);
            virtualRec.setWidth(virtualRec.getWidth()/32);
            virtualRec.setHeight(virtualRec.getHeight()/32);

            //Shortcut variable (Object, prop)
            float osx = testCollide.getX();//object starting x
            float oex = osx + testCollide.getWidth();//object ending x
            float osy = testCollide.getY();//object starting y
            float oey = osy + testCollide.getHeight();;//object ending y
            float psx = virtualRec.getX();//same for prop
            float pex = psx + virtualRec.getWidth();
            float psy = virtualRec.getY();
            float pey = psy + virtualRec.getHeight();

            if (Intersector.overlaps(virtualRec, testCollide)) {
                float inX = 0;
                float inY = 0;
                //How much of X is in
                //is it fully in
                if (psx < osx && pex > oex) {
                    inX = testCollide.getWidth();
                }//Partially in leftside
                else if (osx < psx && oex > psx) {
                    inX = oex - psx;
                }//Partially in rightside
                else if (osx < pex && oex > pex) {
                    inX = pex - osx;
                }

                //How much of Y is in
                //is it fully in
                if (psy < osy && pey > oey) {
                    inY = testCollide.getHeight();
                }//Partially in leftside
                else if (osy < psy && oey > psy) {
                    inY = oey - psy;
                }//Partially in rightside
                else if (osy < pey && oey > pey) {
                    inY = pey - osy;
                }
                //Left or right
                if (inY > inX) {
                    onlyY = true;
                    if(collision.getX() < psx + virtualRec.getWidth()/2){
                        dir = "left";
                    }else{
                        dir = "right";
                    }
                }

                else if(inX > inY) {
                    onlyX = true;
                    if(collision.getY() < psy + virtualRec.getHeight()/2){
                        dir = "bottom";
                    }else{
                        dir = "top";
                    }
                }



                Gdx.app.log("direction", dir);
            }
        }
        //Update position
        if(!onlyY) {
            pos.x = pos.x + movementX * delta;
            if(dir.equals("top") && dirY > 0)
                pos.y = pos.y + movementY * delta;
            if(dir.equals("bottom") && dirY < 0)
                pos.y = pos.y + movementY * delta;
        }

        if(!onlyX) {
            pos.y = pos.y + movementY * delta;
            if(dir.equals("right") && dirX > 0)
                pos.x = pos.x + movementX * delta;
            if(dir.equals("left") && dirX < 0)
                pos.x = pos.x + movementX * delta;
        }
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);
        collision.setX(pos.x);
        collision.setY(pos.y);
    }

    //Updates the velocity from touchpad widget
    public void updateVelocity(float movementX, float movementY){
        this.dirX = movementX;
        this.dirY = movementY;
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
