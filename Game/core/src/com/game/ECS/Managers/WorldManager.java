package com.game.ECS.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.ECS.Other.B2DVars;
import com.game.ECS.Other.GameVars;
import com.game.MyContactListener;

/**
 * Created by Sean on 25/04/2015.
 *
 * WorldManager, a singleton class used to handle the Box2D world.
 *
 */
public class WorldManager {

    public WorldManager (){
        world = new World(new Vector2(0,0), true);
    }

    public enum BodyType{
        HUMANOID
    }

    private World world;

    public Body createBody(BodyType type, Entity owner){
        switch(type){
            case HUMANOID:
                float posX = 0, posY = 0, offsetX = 1f, offsetY = 0f, circleRadius = 0.25f,
                        hitboxW = 0.25f, hitboxH = 0.50f, hitboxOSX = 0f, hitboxOSY = 0.5f;
                return createBody(posX+offsetX, posY+offsetY, circleRadius,
                        hitboxW, hitboxH, hitboxOSX, hitboxOSY, owner);
        }
        return null;
    }

    private Body createBody(float x, float y, float collisionRadius, float hitboxW,
                            float hitboxH, float offsetX, float offsetY, Entity owner){
        Body body;
        BodyDef bodyDef;
        CircleShape circle;
        PolygonShape polygon;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((x)/ GameVars.PTM, y/GameVars.PTM);
        bodyDef.fixedRotation = true;
        body = this.world.createBody(bodyDef);
        //Collision
        CircleShape shape = new CircleShape();
        shape.setRadius(collisionRadius/GameVars.PTM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_HUSK;
        fixtureDef.filter.maskBits = B2DVars.BIT_COLLISION | B2DVars.BIT_HUSK;
        body.createFixture(fixtureDef);
        shape.dispose();

        //Hitbox
        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(hitboxW/GameVars.PTM, hitboxH/GameVars.PTM,
                new Vector2(offsetX, offsetY/GameVars.PTM), 0);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = hitbox;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_HITBOX;
        fixtureDef.filter.maskBits = B2DVars.BIT_PROJECTILE;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(owner);
        hitbox.dispose();
        body.setLinearDamping(GameVars.DAMPING);
        return body;
    }

    /**
     * Getters
     */

    public World getWorld(){
        return world;
    }

}
