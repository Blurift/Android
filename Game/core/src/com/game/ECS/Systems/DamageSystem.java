package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.game.ECS.Components.DamageComponent;
import com.game.ECS.Components.SpriteComponent;

/**
 * Created by Sean on 1/05/2015.
 *
 * Gets the damage component, and deals that damage to an entity with damage component
 * Also flashes sprite red if they have one
 *
 */
public class DamageSystem extends IteratingSystem{

    private ComponentMapper<SpriteComponent> sm;
    private ComponentMapper<DamageComponent> dm;
    private static final int FLASH_AMOUNT = 2; //amount of times sprite flashes red
    private static final float RED_TIME = 0.5f; //How long sprite is red for
    private static final float CLEAR_TIME = 2; //amount of time in betwen red flashes
    private static final Color DMG_COLOR = Color.RED; //Color of damage overlay

    public DamageSystem() {
        super(Family.all(DamageComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine){

        sm = ComponentMapper.getFor(SpriteComponent.class);
        dm = ComponentMapper.getFor(DamageComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //Todo fix deltaTime
        SpriteComponent sc = sm.get(entity);
        DamageComponent dc = dm.get(entity);
        if(sc != null) {
            //Start of damange animation
            if (sc.sprite.getColor().equals(Color.WHITE) && dc.flashes == 0) {
                sc.sprite.setColor(DMG_COLOR);
            }

            if (dc.animTimer >= RED_TIME){
                if(sc.sprite.getColor().equals(Color.WHITE))
                    sc.sprite.setColor(DMG_COLOR);
                else if(sc.sprite.getColor().equals(DMG_COLOR)){
                    sc.sprite.setColor(Color.WHITE);
                    dc.flashes++;
                }
                sc.sprite.setColor(Color.WHITE);

                dc.animTimer = 0;
            }else{
                dc.animTimer += 1 * Gdx.graphics.getDeltaTime();
            }

        }
    }
}
