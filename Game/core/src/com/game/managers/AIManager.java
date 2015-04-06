package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.controllers.AIController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keirron on 22/03/2015.
 */
public class AIManager {
    private MapManager map;
    List<Vector2> spawns;
    List<AIController> monsters;
    ArrayList<TiledMapTileLayer.Cell> spawnersInScene;

    public AIManager(GameManager gm){
        map = gm.getMapManager();
        spawns = new ArrayList<Vector2>();
        monsters = new ArrayList<AIController>();
        spawnersInScene = new ArrayList<TiledMapTileLayer.Cell>();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getTiledMap().getLayers().get(0);
        for(int x = 0; x < layer.getWidth();x++){
            for(int y = 0; y < layer.getHeight();y++){
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                Object property = cell.getTile().getProperties().get("Spawn");
                if(property != null){
                    spawnersInScene.add(cell);
                    spawns.add(new Vector2(x, y));
                }
            }
        }
        Gdx.app.log("g", "sdf");
        for(Vector2 spawner : spawns){
            Gdx.app.log("g", "sdf");
            AIController enemy = new AIController("testCharacter.png" , new Vector2(spawner.x, spawner.y));
            monsters.add(enemy);
        }
    }

    public List<AIController> getEnemies(){
        return monsters;
    }
}
