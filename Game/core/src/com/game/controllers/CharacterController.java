package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Rectangle;
import com.game.Animator;
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

    //Movement checks
    private boolean isMoving;
    private String isFacing = "Down";


    //Animations
    private AnimState currentAnimation;
    private float aniTime;

    public CharacterController(GameManager gm, String spriteSheet, Vector2 startPos)
    {

        this.pos = startPos;
        this.gm = gm;
        aniTime = 0f;
        texture = new Texture(Gdx.files.internal(spriteSheet));
        currentRegion = new TextureRegion(texture, 33, 1, 32, 32);
        charSprite = new Sprite(currentRegion);
        charSprite.setSize(2f, 2f);
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);
        collision = new Rectangle(pos.x, pos.y, 2f, 1f);


    }

    //Getters
    public Vector2 getPosition() {
        return pos;
    }

    public Sprite getSprite(){
        return charSprite;
    }

    public Rectangle getCollision(){
        return collision;
    }

    //Setters
    public void setPosition(Vector2 pos) { this.pos = pos; }

    //Updates for the player at each frame
    public void update(float delta){
        //TODO has to be an easier way to do this
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

        //Update animation
        aniTime += delta;
        AnimState currentAnimation = getAnimation();
        if(this.currentAnimation != currentAnimation){
            aniTime = 0f;
            this.currentAnimation = currentAnimation;
        }
        if (currentAnimation != null)
            charSprite.setRegion(getAnimation(currentAnimation).getKeyFrame(aniTime, true));

    }

    //Updates the velocity from touchpad widget
    public void updateVelocity(float movementX, float movementY){
        //TODO the vector2 'test' removes acceleration and keeps player the same speed. Keep this?
        Vector2 test = new Vector2(movementX, movementY).nor();
        this.dirX = movementX;
        this.dirY = movementY;

        this.movementX = test.x * moveSpeed;
        this.movementY = test.y * moveSpeed;
    }

    /**
     * Animation functions
    */
    public String isFacing(){

        if(dirY <= 0.60f && dirY > -0.60f && dirX > 0){
            isFacing = "Right";
        }else if(dirY <= 0.60f && dirY > -0.60f && dirX < 0){
            isFacing = "Left";
        }else if(dirX <= 0.80f && dirX > -0.80f && dirY > 0){
            isFacing = "Up";
        }else if(dirX <= 0.80f && dirX > -0.80f && dirY < 0){
            isFacing = "Down";
        }

        return isFacing;
    }

    public boolean isMoving(){
        if(movementX == 0 && movementY == 0)
            isMoving = false;
        else
            isMoving = true;
        return isMoving;
    }

    private AnimState getAnimation(){
        AnimState currentAnimation = null;
        if(!isMoving()) {
            if(isFacing().equals("Left")) //stillLeft
                currentAnimation = AnimState.STILL_LEFT;
            if(isFacing().equals("Right")) //stillRight
                currentAnimation = AnimState.STILL_RIGHT;
            if(isFacing().equals("Up")) //stillUp
                currentAnimation = AnimState.STILL_UP;
            if(isFacing().equals("Down")) //stillDown
                currentAnimation = AnimState.STILL_DOWN;
        }else if(isMoving()) {
            if(isFacing().equals("Left")) //walkLeft
                currentAnimation = AnimState.WALK_LEFT;
            if(isFacing().equals("Right")) //walkRight
                currentAnimation = AnimState.WALK_RIGHT;
            if(isFacing().equals("Up")) //walkUp
                currentAnimation = AnimState.WALK_UP;
            if(isFacing().equals("Down")) //walkDown
                currentAnimation = AnimState.WALK_DOWN;
        }
        return currentAnimation;
    }

    private Animation getAnimation(AnimState state){
        Animator animator = new Animator();
        Animation currentAnimation = null;

        switch(state) {
            case STILL_LEFT:
                return animator.animate(texture, 33, 33, 32, 32, 1, 0.2f);
            case STILL_RIGHT:
                return animator.animate(texture, 33, 65, 32, 32, 1, 0.2f);
            case STILL_UP:
                return animator.animate(texture, 33, 97, 32, 32, 1, 0.2f);
            case STILL_DOWN:
                return animator.animate(texture, 33, 1, 32, 32, 1, 0.2f);
            case WALK_LEFT:
                return animator.animate(texture, 1, 33, 32, 32, 3, 0.2f);
            case WALK_RIGHT:
                return animator.animate(texture, 1, 65, 32, 32, 3, 0.2f);
            case WALK_UP:
                return animator.animate(texture, 1, 97, 32, 32, 3, 0.2f);
            case WALK_DOWN:
                return animator.animate(texture, 1, 1, 32, 32, 3, 0.2f);
            default:
                return null;
       }
    }


    private enum AnimState {
        //TODO merge the aboe this somehow
        STILL_LEFT, STILL_RIGHT, STILL_UP, STILL_DOWN,
        WALK_LEFT, WALK_RIGHT, WALK_UP, WALK_DOWN;

    }

    //Dispose
    public void dispose(){
        texture.dispose();
    }

}
