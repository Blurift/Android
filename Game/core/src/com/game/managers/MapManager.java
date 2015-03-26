package com.game.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.List;

/**
 * Created by Keirron on 22/03/2015.
 */
public class MapManager {
    private OrthographicCamera camera;

    //Tiled map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    //Layers
    private MapLayer objectLayer; //Objects like player, monster, items
    private MapLayer collisionLayer; //Layer containing collisions boxes

    public MapManager(OrthographicCamera camera, String mapName)
    {
        this.camera = camera;

        tiledMap = new TmxMapLoader().load(mapName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);



        objectLayer = tiledMap.getLayers().get("objects");
        collisionLayer = tiledMap.getLayers().get("collisions");
    }

    public void render()
    {
        tiledMapRenderer.setView(camera); // TODO can probably move this to create method
        tiledMapRenderer.render();
    }

    public void dispose(){
        tiledMap.dispose();
    }

    public void addObject(TextureMapObject obj){
        objectLayer.getObjects().add(obj);
    }

    //Remove the objecting being rendered,
    public void removeObject(TextureMapObject obj){
        objectLayer.getObjects().remove(obj);
    }
}
