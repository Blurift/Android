package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpawningComponent;
import com.game.ECS.Managers.MapManager;
import com.game.ECS.Other.GameVars;

/**
 * Created by Sean on 25/04/2015.
 *
 * Handles the spawning of all entities
 *
 */
public class SpawnSystem extends EntitySystem {
    private ImmutableArray<Entity> playerEntities;

    private ComponentMapper<SpawningComponent> sm;
    private ComponentMapper<PlayerComponent> pm;
    private ComponentMapper<BodyComponent> bm;
    private MapManager mapManager;

    public SpawnSystem(int order, MapManager mapManager) {
        super(order);
        sm = ComponentMapper.getFor(SpawningComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        this.mapManager = mapManager;
    }

    public void addedToEngine(Engine engine) {
        playerEntities = engine.getEntitiesFor(Family.all(SpawningComponent.class,
                PlayerComponent.class, BodyComponent.class).get());
    }


    public void update(float deltaTime) {
        for (int i = 0; i < playerEntities.size(); ++i) {
            Vector2 spawn = mapManager.getRandomPlayerSpawn();
            Entity entity = playerEntities.get(i);
            PositionComponent position = new PositionComponent(
                    spawn.x, spawn.y);
            Body body = bm.get(playerEntities.get(i)).body;
            //Convert to Box2d units
            spawn.x /= GameVars.PTM;
            spawn.y /= GameVars.PTM;
            body.setTransform(spawn, body.getAngle());
            entity.remove(SpawningComponent.class);
            entity.add(position);
        }
    }
}