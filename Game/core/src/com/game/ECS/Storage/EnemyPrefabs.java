package com.game.ECS.Storage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.AIComponent;
import com.game.ECS.Components.AnimationSetComponent;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpawningComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Components.StateComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Managers.WorldManager;

/**
 * Created by Sean on 3/05/2015.
 *
 * Prefabs of all the enemies spawnable
 *
 */
public class EnemyPrefabs {

    public static Entity createTestEnemy(WorldManager worldManager, Vector2 spawn){
        Entity enemy = new Entity();

        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.sprite.setSize(2,2);
        spriteComponent.sprite.setColor(Color.BLACK);

        PositionComponent position = new PositionComponent(
                spawn.x, spawn.y);

        BodyComponent bodyComponent = new BodyComponent(worldManager.createBody(
                WorldManager.BodyType.HUMANOID, enemy
        ));
        bodyComponent.offset.y = 0.5f;
        spawn.x /= GameVars.PTM;
        spawn.y /= GameVars.PTM;
        bodyComponent.body.setTransform(spawn, bodyComponent.body.getAngle());

        enemy.add(new VelocityComponent(0, 0))
                .add(bodyComponent)
                .add(new PlayerComponent(0))
                .add(new FacingComponent())
                .add(spriteComponent)
                .add(new AnimationSetComponent(Assets.animPlayerDruid()))
                .add(new StateComponent())
                .add(new DepthComponent(-16)).add(new AIComponent()).add(position);

        return enemy;
    }
}
