package com.game.ECS.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.ParticleEffectComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Storage.B2DVars;
import com.game.ECS.Storage.Particles;


/**
 * Created by Sean on 1/05/2015.
 *
 * Handles the contact between Box2D objects
 *
 */
public class MyContactListener implements ContactListener {

    private Engine engine;
    public MyContactListener(Engine engine){
        this.engine = engine;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getFilterData().categoryBits == B2DVars.BIT_PROJECTILE) {
            Entity projectile = (Entity) fa.getUserData();
            Entity entity = new Entity();

            PositionComponent pos = new PositionComponent(entity.getComponent(PositionComponent.class).x,
                    entity.getComponent(PositionComponent.class).y);
            ParticleEffectComponent effect = new ParticleEffectComponent();
            effect.effect = Particles.iceExplosion();

            entity.add(pos).add(effect).add(new DepthComponent(0));
            engine.addEntity(entity);
            engine.removeEntity(projectile);
        }
        if(fb.getFilterData().categoryBits == B2DVars.BIT_PROJECTILE) {
            Entity projectile = (Entity) fb.getUserData();
            Entity entity = new Entity();

            PositionComponent pos = new PositionComponent(projectile.getComponent(PositionComponent.class).x,
                    projectile.getComponent(PositionComponent.class).y);
            ParticleEffectComponent effect = new ParticleEffectComponent();
            effect.effect = Particles.iceExplosion();

            //TODO make an OnHit variable for Projectile, which stores an entity: Clear this entity after use
            entity.add(pos).add(effect).add(new DepthComponent(0));
            engine.addEntity(entity);
            engine.removeEntity(projectile);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
