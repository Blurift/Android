package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.SpellSystem.Projectile;
import com.game.controllers.AIController;
import com.game.managers.AIManager;
import com.game.managers.GameManager;

/**
 * Created by Sean on 22/04/2015.
 */
public class MyContactListener implements ContactListener {
    private GameManager gm;

    public MyContactListener(GameManager gm){
        this.gm = gm;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fa.getUserData() instanceof Projectile) {
            //TODO dispose lasts 1 frame ((Projectile) fa.getUserData()).dispose();
            gm.getProjectileManager().removeProjectile((Projectile)fa.getUserData());
        }
        if(fb.getUserData() instanceof Projectile) {
            //TODO ((Projectile) fb.getUserData()).dispose();
            //gm.getProjectileManager().removeProjectile((Projectile)fb.getUserData());
            ((Projectile)fb.getUserData()).kill();
            if(fa.getUserData() instanceof AIController){
                ((AIController)fa.getUserData()).hit();
            }
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
