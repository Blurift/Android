package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.VelocityComponent;

/**
 * Created by Sean on 27/04/2015.
 */
public class FacingSystem extends IteratingSystem{
    private ComponentMapper<FacingComponent> fm;
    private ComponentMapper<VelocityComponent> vm;

    public FacingSystem() {
        super(Family.all(FacingComponent.class, VelocityComponent.class).get());

        fm = ComponentMapper.getFor(FacingComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector2 vel = vm.get(entity).velocity;
        FacingComponent.Facing facing = fm.get(entity).facing;
        if(vel.y <= 0.60f && vel.y > -0.60f && vel.x > 0){
            facing = FacingComponent.Facing.RIGHT;
        }else if(vel.y <= 0.60f && vel.y> -0.60f && vel.x < 0){
            facing = FacingComponent.Facing.LEFT;
        }else if(vel.x <= 0.80f && vel.x > -0.80f && vel.y > 0){
            facing = FacingComponent.Facing.UP;;
        }else if(vel.x <= 0.80f && vel.x > -0.80f && vel.y < 0){
            facing = FacingComponent.Facing.DOWN;
        }

        fm.get(entity).facing = facing;

    }
}
