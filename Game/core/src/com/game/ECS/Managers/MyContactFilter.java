package com.game.ECS.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.game.ECS.Components.ProjectileComponent;
import com.game.ECS.Storage.B2DVars;

/**
 * Created by Sean on 28/04/2015.
 */
public class MyContactFilter implements ContactFilter {
    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {

        boolean test = shouldBothCollide(fixtureA, fixtureB);
        if(!test){
            test = shouldBothCollide(fixtureB, fixtureA);
        }

        return test;
    }


    private boolean shouldBothCollide(Fixture fixtureA, Fixture fixtureB){

        //Cbeck if projectile is colliding with owner, if so, don't collide
        if(fixtureA.getFilterData().categoryBits == B2DVars.BIT_PROJECTILE &&
                fixtureB.getFilterData().categoryBits == fixtureA.getFilterData().maskBits){
            if(fixtureA.getUserData() instanceof Entity &&
                    fixtureB.getUserData() instanceof Entity){
                Entity entityA = (Entity) fixtureA.getUserData();
                Entity entityB = (Entity) fixtureB.getUserData();
                Entity owner = entityA.getComponent(ProjectileComponent.class).owner;
                if(entityB.equals(owner)){
                    return false;
                }

            }

            return true;

        }

        if(fixtureA.getFilterData().categoryBits == B2DVars.BIT_HUSK &&
                fixtureB.getFilterData().categoryBits == B2DVars.BIT_COLLISION){
            return true;
        }

        return false;
    }

}
