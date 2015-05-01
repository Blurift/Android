package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.game.ECS.Components.CameraComponent;
import com.game.ECS.Components.PositionComponent;

/**
 * Created by Sean on 26/04/2015.
 *
 * Handles moving the gameworld camera.
 *
 */
public class CameraSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<CameraComponent> cm;

    public CameraSystem(int order) {
        super(Family.all(PositionComponent.class, CameraComponent.class).get(), order);

        pm = ComponentMapper.getFor(PositionComponent.class);
        cm = ComponentMapper.getFor(CameraComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cam = cm.get(entity);
        PositionComponent pos = pm.get(entity);

        cam.camera.position.x = pos.x + cam.offset.x;
        cam.camera.position.y = pos.y + cam.offset.y;

        cam.camera.update(); //TODO maybe move to render
    }


}
