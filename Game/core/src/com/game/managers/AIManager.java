package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.controllers.AIController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keirron on 22/03/2015.
 *
 * TODO use a pool to handle all monsters
 *
 */
public class AIManager {
    private MapManager map;
    List<Vector2> spawns;
    List<AIController> monsters;
    ArrayList<TiledMapTileLayer.Cell> spawnersInScene;

    public AIManager(GameManager gm){
        map = gm.getMapManager();
        spawns = gm.getMapManager().getEnemySpawns();
        monsters = new ArrayList<AIController>();
        spawnersInScene = new ArrayList<TiledMapTileLayer.Cell>();
        for(Vector2 spawner : spawns){
            AIController enemy = new AIController(gm, "character/druid_sheet.png" , new Vector2(spawner.x, spawner.y));
            monsters.add(enemy);
        }
    }

    public List<AIController> getEnemies(){
        return monsters;
    }
}
