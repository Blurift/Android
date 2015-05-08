package com.game.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * Created by Sean on 8/05/2015.
 *
 * Grabs a spell component to cast it.
 *
 */
public class SpellSystem extends IteratingSystem{
    public SpellSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
