package com.game.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;

/**
 * Created by Sean on 13/06/2015.
 */
public class CamPanSystem extends EntitySystem {

    private Engine engine;
    private ImmutableArray<Entity> entities;

    public CamPanSystem(){

    }

    public void addedToEngine(Engine engine){

    }

    public void update(float deltaTime) {

    }
}
