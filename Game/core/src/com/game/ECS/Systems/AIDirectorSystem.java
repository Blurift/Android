package com.game.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Managers.MapManager;
import com.game.ECS.Managers.WorldManager;
import com.game.ECS.Storage.EnemyPrefabs;

/**
 * Created by Sean on 3/05/2015.
 */
public class AIDirectorSystem extends EntitySystem {

    private MapManager mapManager;
    private WorldManager worldManager;

    private Engine engine;

    public AIDirectorSystem(MapManager mapManager, WorldManager worldManager) {
        this.mapManager = mapManager;
        this.worldManager = worldManager;
    }

    public void addedToEngine(Engine engine){
        this.engine = engine;
    }


    //Todo remove
    private boolean added = false;
    private float spawnTime = 15f;

    public void update(float deltatime){
        if(spawnTime <= 0){
            Vector2 spawn = mapManager.getRandomEnemySpawn();
            Entity enemy = EnemyPrefabs.createTestEnemy(worldManager, spawn);

            engine.addEntity(enemy);
            added = true;
            spawnTime = 15;
        }else{
            //Todo fix delta
            spawnTime -= 1* Gdx.graphics.getDeltaTime();
        }
    }
}
