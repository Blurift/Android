package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.AIComponent;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpawningComponent;
import com.game.ECS.Components.VelocityComponent;

/**
 * Created by Sean on 3/05/2015.
 *
 * Used to Manage all AI as a whole, where they spawn, when, global goals
 *
 */
public class AISystem extends IteratingSystem{

    private Engine engine;

    private ComponentMapper<PositionComponent> pm;

    public AISystem() {
        super(Family.all(AIComponent.class, PositionComponent.class).get());
    }

    public void addedToEngine(Engine engine){
        super.addedToEngine(engine);
        this.engine = engine;

        pm = ComponentMapper.getFor(PositionComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //Todo Players should probably be found a different way

        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PositionComponent.class,
                PlayerComponent.class).get());

        if(players.size() != 0){
            Entity player = players.get(0);
            PositionComponent pos = pm.get(entity);
            Vector2 enemyPos = new Vector2(pos.x, pos.y);
            pos = pm.get(player);
            Vector2 playerPos = new Vector2(pos.x, pos.y);

            Vector2 dir = new Vector2(playerPos).sub(enemyPos).nor();
            VelocityComponent vel = entity.getComponent(VelocityComponent.class);
            //Slow down
            dir.x *= 0.5;
            dir.y *= 0.5;
            if(vel != null){
                vel.velocity = dir;
            }
        }

    }
}
