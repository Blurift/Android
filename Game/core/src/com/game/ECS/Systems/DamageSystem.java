package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.game.ECS.Components.DamageComponent;
import com.game.ECS.Components.HealthComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpawningComponent;
import com.game.ECS.Components.SpriteComponent;

/**
 * Created by Sean on 1/05/2015.
 *
 * Gets the damage component, and deals that damage to an entity with damage component
 * Also flashes sprite red if they have one
 *
 */
public class DamageSystem extends IteratingSystem{

    Engine engine;

    private ComponentMapper<SpriteComponent> sm;
    private ComponentMapper<DamageComponent> dm;
    private ComponentMapper<HealthComponent> hm;
    private static final int FLASH_AMOUNT = 2; //amount of times sprite flashes red
    private static final float RED_TIME = 0.05f; //How long sprite is red for
    private static final float CLEAR_TIME = 2; //amount of time in betwen red flashes
    private static final Color DMG_COLOR = Color.RED; //Color of damage overlay

    public DamageSystem() {
        super(Family.all(DamageComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine){
        super.addedToEngine(engine);
        this.engine = engine;
        sm = ComponentMapper.getFor(SpriteComponent.class);
        dm = ComponentMapper.getFor(DamageComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        //Todo fix deltaTime
        SpriteComponent sc = sm.get(entity);
        DamageComponent dc = dm.get(entity);
        HealthComponent hc = hm.get(entity);

        if(hc != null){
            hc.currentHealth-= dc.damage;
            if(hc.currentHealth <= 0) {
                //Todo for killing things
                if(entity.getComponent(PlayerComponent.class) != null){
                    entity.remove(PositionComponent.class);
                    entity.add(new SpawningComponent(3));
                }else
                    engine.removeEntity(entity);
            }
            dc.damage = 0;
        }


        if(sc != null) {
            //Start of damage animation
            if (sc.sprite.getColor().toFloatBits() != DMG_COLOR.toFloatBits() && dc.flashes == 0) {
               // dc.originalColor = sc.sprite.getColor();
                dc.originalColor = Color.WHITE;
                sc.sprite.setColor(DMG_COLOR);
            }

            if (dc.animTimer >= RED_TIME){
                if(dc.originalColor != null) {
                    if (sc.sprite.getColor().toFloatBits() == dc.originalColor.toFloatBits())
                        sc.sprite.setColor(DMG_COLOR);
                    else if (sc.sprite.getColor().toFloatBits() == DMG_COLOR.toFloatBits()) {
                        sc.sprite.setColor(dc.originalColor.toFloatBits());
                        dc.flashes++;
                    }
                }else{
                    sc.sprite.setColor(Color.WHITE);
                    entity.remove(DamageComponent.class);
                }
                dc.animTimer = 0;


                if(dc.flashes == FLASH_AMOUNT){
                    sc.sprite.setColor(dc.originalColor.toFloatBits());
                    entity.remove(DamageComponent.class);
                }
            }else{
                dc.animTimer += 1 * Gdx.graphics.getDeltaTime();
            }

        }
    }
}
