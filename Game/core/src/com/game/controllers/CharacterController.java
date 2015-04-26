package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.game.Animator;
import com.game.ECS.Other.B2DVars;
import com.game.managers.GameManager;
import com.game.tools.IDepthObject;

/**
 * Created by Keirron on 22/03/2015.
 */
public class CharacterController implements IDepthObject {


    private float movementX = 0;
    private float movementY = 0;


    private float dirX = 0; //Direction from touchpad on x scale
    private float dirY = 0; //Direction from touchpad on y scale

    private float moveSpeed = 6f;

    private Vector2 pos;

    private Texture texture;
    private TextureRegion currentRegion; //used for later animation
    private Sprite charSprite;

    private GameManager gm;

    //Movement checks
    private boolean isMoving;
    private String isFacing = "Down";

    //Cam movements
    private float camModifierY = 0;
    private float camModifierX = 0;
    private Vector2 camBackSpeed = new Vector2(1f, 1f);

    //Animations
    private AnimState currentAnimation;
    private float aniTime;

    //Box2d body
    private Body body;

    public boolean isCamLocked = false;

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

        //Box2d body for collision
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //Set position of body, scale it to world units and move to centre of sprite
        bodyDef.position.set((pos.x + 1f)/gm.PIXELS_TO_METRES, (pos.y)/gm.PIXELS_TO_METRES);
        //We don't need rotation at the moment
        bodyDef.fixedRotation = true;

        // Create a body in the world using our definition
        body = gm.getWorld().createBody(bodyDef);
        // Now define the dimensions of the physics shape
        CircleShape shape = new CircleShape();
        shape.setRadius(0.25f/gm.PIXELS_TO_METRES);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_HUSK;
        fixtureDef.filter.maskBits = B2DVars.BIT_COLLISION | B2DVars.BIT_HUSK;
        Fixture fixture = body.createFixture(fixtureDef);
        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }

    public void draw(SpriteBatch batch){
        charSprite.draw(batch);
    }

    /**
     * Getters
     */

    //Cast Point is where projectiles spawn from
    public Vector2 getCastPoint(){
        if(isFacing.equals("Left")){
            return new Vector2(pos.x, pos.y + charSprite.getHeight()*0.5f);
        }else if(isFacing.equals("Right")){
            return new Vector2(pos.x + charSprite.getWidth(), pos.y + charSprite.getHeight()*0.5f);
        }else{
            return new Vector2(pos.x + charSprite.getWidth()*0.5f, pos.y + charSprite.getHeight()*0.5f);
        }

    }

    public Vector2 getPosition() {
        return pos;
    }

    public Sprite getSprite(){
        return charSprite;
    }

    public float getY(){
        return charSprite.getY();
    }

    /**
     *  Setters
     */
    public void setPosition(Vector2 pos) {
        this.pos = pos;
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);
        body.setTransform((pos.x + 1f)/gm.PIXELS_TO_METRES, (pos.y)/gm.PIXELS_TO_METRES,
                body.getAngle());
    }

    public void setCamModifierX(float x){
        camModifierX = x;
    }

    public void setCamModifierY(float y){
        camModifierY = y;
    }
    public void setCamBackSpeed(Vector2 speed){
        camBackSpeed = speed;
    }

    //Directly mention where the player is facing
    public void setFacing(String face){
        isFacing = face;
    }

    //Calculate and set where the place is facing based on a x,y dir
    public void setFacing(float dirX, float dirY){
        if(dirY <= 0.60f && dirY > -0.60f && dirX > 0){
            isFacing = "Right";
        }else if(dirY <= 0.60f && dirY > -0.60f && dirX < 0){
            isFacing = "Left";
        }else if(dirX <= 0.80f && dirX > -0.80f && dirY > 0){
            isFacing = "Up";
        }else if(dirX <= 0.80f && dirX > -0.80f && dirY < 0){
            isFacing = "Down";
        }
    }

    //Updates for the player at each frame
    public void update(float delta){

        // Now update the spritee position accordingly to it's now updated
        // Physics body, scale it back from world units, and keep it moved on sprite
        pos.set(body.getPosition().x * gm.PIXELS_TO_METRES - 1,
                body.getPosition().y * gm.PIXELS_TO_METRES - 0.25f);
        charSprite.setX(body.getPosition().x * gm.PIXELS_TO_METRES - 1);
        charSprite.setY(body.getPosition().y * gm.PIXELS_TO_METRES - 0.5f);

        //Update animation to render
        aniTime += delta;
        AnimState currentAnimation = getAnimation();
        if(this.currentAnimation != currentAnimation){
            aniTime = 0f;
            this.currentAnimation = currentAnimation;
        }
        if (currentAnimation != null)
            charSprite.setRegion(getAnimation(currentAnimation).getKeyFrame(aniTime, true));


        //Player Camera Movement
        float defaultCamX = charSprite.getX() + (charSprite.getWidth() / 2);
        float defaultCamY = charSprite.getY() + (charSprite.getHeight() / 2);

        float camSpeed = camBackSpeed.x;

        Camera camera = gm.getCamera();
        float maxX = dirX*0.5f;
        float maxY = dirY*0.5f;
        if(camModifierX < maxX && camModifierY < maxY)
            camSpeed = 1f;

        if(dirX > 0){
            if(camModifierX < maxX){
                camModifierX += Gdx.graphics.getDeltaTime() * camSpeed;
            }else{
                camModifierX = maxX;
            }
        }else if(dirX < 0){
            if(camModifierX > maxX){
                camModifierX -= Gdx.graphics.getDeltaTime() * camSpeed;
            }else{
                camModifierX = maxX;
            }
        }

        if(dirY > 0){
            if(camModifierY < maxY){
                camModifierY += Gdx.graphics.getDeltaTime() * camSpeed;
            }else{
                camModifierY = maxY;
            }
        }else if(dirY < 0){
            if(camModifierY > maxY){
                camModifierY -= Gdx.graphics.getDeltaTime() * camSpeed;
            }else{
                camModifierY = maxY;
            }
        }

        if(dirX == 0){
            if(camModifierX > 0){
                camModifierX -= Gdx.graphics.getDeltaTime() * camBackSpeed.x;
                if(camModifierX < 0)
                    camModifierX = 0;
            }
            if(camModifierX < 0){
                camModifierX += Gdx.graphics.getDeltaTime() * camBackSpeed.x;
                if(camModifierX > 0)
                    camModifierX = 0;
            }
        }else{
            if(camModifierX < maxX)
                camBackSpeed.x = 1f;
            if(camModifierY < maxY)
                camBackSpeed.y = 1f;//Make sure going back at normal speed
        }
        if(dirY == 0){
            if(camModifierY > 0){
                camModifierY -= Gdx.graphics.getDeltaTime() * camBackSpeed.y;
                if(camModifierY < 0)
                    camModifierY = 0;
            }
            if(camModifierY < 0){
                camModifierY += Gdx.graphics.getDeltaTime() * camBackSpeed.y;
                if(camModifierY > 0)
                    camModifierY = 0;
            }
        }else{
            if(camModifierX < maxX)
                camBackSpeed.x = 2f;
            if(camModifierY < maxY)
                camBackSpeed.y = 2f;//Make sure going back at normal speed
        }

        if(!isCamLocked)
            camera.position.set(defaultCamX + (camModifierX), defaultCamY + (camModifierY), 0);
    }

    //Updates the velocity from touchpad widget
    public void updateVelocity(float movementX, float movementY){
        //Removes acceleration effect from touchpad, we want that from physics
        Vector2 normalizedMovement = new Vector2(movementX, movementY).nor();
        this.dirX = movementX;
        this.dirY = movementY;
        setFacing(dirX, dirY);
        this.movementX = normalizedMovement.x * moveSpeed;
        this.movementY = normalizedMovement.y * moveSpeed;
        //TODO change to add acceleration and decel
        /*
        body.setLinearVelocity(normalizedMovement.x/gm.PIXELS_TO_METRES * moveSpeed * gm.getGameSpeed(),
                normalizedMovement.y/gm.PIXELS_TO_METRES * moveSpeed * gm.getGameSpeed());
                */

        Vector2 velocity = body.getLinearVelocity();
        float desiredXVel = 0;
        float desiredYVel = 0;
        float maxV = 0.15f;
        body.setLinearDamping(9f);
        if(dirX < 0){
            desiredXVel = dirX;
        }
        if(dirX > 0){
            desiredXVel = dirX;
        }
        if(dirY < 0){
            desiredYVel = dirY;
        }
        if(dirY > 0){
            desiredYVel = dirY;
        }

        float velXChange = desiredXVel - velocity.x;
        float velYChange = desiredYVel - velocity.y;
        float impulseX = body.getMass() * velXChange; //disregard time factor
        float impulseY = body.getMass() * velYChange; //disregard time factor
        impulseX /= gm.PIXELS_TO_METRES;
        impulseY /= gm.PIXELS_TO_METRES;
        impulseX *= gm.getGameSpeed();
        impulseY *= gm.getGameSpeed();
        body.applyLinearImpulse(new Vector2((impulseX), impulseY), body.getWorldCenter(), true);

    }

    /**
     * Animation functions
    */
    public String isFacing(){
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
            if(isFacing.equals("Left")) //stillLeft
                currentAnimation = AnimState.STILL_LEFT;
            if(isFacing.equals("Right")) //stillRight
                currentAnimation = AnimState.STILL_RIGHT;
            if(isFacing.equals("Up")) //stillUp
                currentAnimation = AnimState.STILL_UP;
            if(isFacing.equals("Down")) //stillDown
                currentAnimation = AnimState.STILL_DOWN;
        }else if(isMoving()) {
            if(isFacing.equals("Left")) //walkLeft
                currentAnimation = AnimState.WALK_LEFT;
            if(isFacing.equals("Right")) //walkRight
                currentAnimation = AnimState.WALK_RIGHT;
            if(isFacing.equals("Up")) //walkUp
                currentAnimation = AnimState.WALK_UP;
            if(isFacing.equals("Down")) //walkDown
                currentAnimation = AnimState.WALK_DOWN;
        }
        return currentAnimation;
    }

    private Animation getAnimation(AnimState state){
        Animator animator = new Animator();

        switch(state) {
            case STILL_LEFT:
                return animator.animate(texture, 64, 192, 64, 64, 1, 0.2f);
            case STILL_RIGHT:
                return animator.animate(texture, 64, 128, 64, 64, 1, 0.2f);
            case STILL_UP:
                return animator.animate(texture, 64, 64, 64, 64, 1, 0.2f);
            case STILL_DOWN:
                return animator.animate(texture, 64, 0, 64, 64, 1, 0.2f);
            case WALK_LEFT:
                return animator.animate(texture, 64, 192, 64, 64, 1, 0.2f);
            case WALK_RIGHT:
                return animator.animate(texture, 64, 128, 64, 64, 1, 0.2f);
            case WALK_UP:
                return animator.animate(texture, 64, 64, 64, 64, 1, 0.2f);
            case WALK_DOWN:
                return animator.animate(texture, 64, 0, 64, 64, 4, 0.15f);
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
