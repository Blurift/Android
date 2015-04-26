package com.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.game.ECS.Other.B2DVars;
import com.game.managers.GameManager;
import com.game.tools.IDepthObject;

/**
 * Created by Keirron on 22/03/2015.
 */
public class AIController implements IDepthObject{

    private Vector2 pos;

    private Texture texture;
    private TextureRegion currentRegion; //used for later animation
    private Sprite charSprite;
    private GameManager gm;
    private float dmgTimer = 0;
    private float flash = 0;

    //Box2d body
    private Body body;

    //Hitbox
    //Collision
    //Sprite
    //x

    public AIController(GameManager gm, String spriteName, Vector2 startPos) {
        this.pos = startPos;
        this.gm = gm;

        texture = new Texture(Gdx.files.internal(spriteName));
        currentRegion = new TextureRegion(texture, 64, 0, 64, 64);
        charSprite = new Sprite(currentRegion);
        charSprite.setSize(2f, 2f);
        charSprite.setX(pos.x);
        charSprite.setY(pos.y);
        charSprite.setColor(Color.BLACK);

        //  BOX2D

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((pos.x + 1f)/gm.PIXELS_TO_METRES, (pos.y)/gm.PIXELS_TO_METRES);
        bodyDef.fixedRotation = true;
        body = gm.getWorld().createBody(bodyDef);

        //Collision
        CircleShape shape = new CircleShape();
        shape.setRadius(0.25f/gm.PIXELS_TO_METRES);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_HUSK;
        fixtureDef.filter.maskBits = B2DVars.BIT_COLLISION | B2DVars.BIT_HUSK;
        body.createFixture(fixtureDef);
        shape.dispose();

        //Hitbox
        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(0.25f/gm.PIXELS_TO_METRES, 0.50f/gm.PIXELS_TO_METRES,
                new Vector2(0, 0.5f/gm.PIXELS_TO_METRES), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = hitbox;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_HITBOX;
        fixtureDef.filter.maskBits = B2DVars.BIT_PROJECTILE;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        hitbox.dispose();
        body.setLinearDamping(9f);
    }

    public void hit(){
        Gdx.app.log("a", "a");
        charSprite.setColor(Color.RED);
        dmgTimer = 0.05f;
        on = true;
        flash = 2;
    }

    public Vector2 getPosition() { return pos; }

    public Sprite getSprite(){ return charSprite; }

    public void dispose(){
        texture.dispose();
    }

    @Override
    public float getY() {
        return charSprite.getY();
    }

    private boolean on = false;
    public void update() {
        // Physics body, scale it back from world units, and keep it moved on sprite
        pos.set(body.getPosition().x * gm.PIXELS_TO_METRES - 1,
                body.getPosition().y * gm.PIXELS_TO_METRES - 0.25f);
        charSprite.setX(body.getPosition().x * gm.PIXELS_TO_METRES - 1);
        charSprite.setY(body.getPosition().y * gm.PIXELS_TO_METRES - 0.5f);
        if(flash != 0) {
            if (dmgTimer > 0) {
                dmgTimer -= 1 * Gdx.graphics.getDeltaTime();
            } else if (dmgTimer <= 0) {
                if(!on) {
                    on = true;
                    charSprite.setColor(Color.RED);
                }
                else if(on) {
                    on = false;
                    charSprite.setColor(Color.BLACK);
                    flash--;
                }
                dmgTimer = 0.05f;
                Gdx.app.log("Te", "Te");
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
            charSprite.draw(batch);
    }



}
