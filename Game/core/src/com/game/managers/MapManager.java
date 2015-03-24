package com.game.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by Keirron on 22/03/2015.
 */
public class MapManager {

    private OrthographicCamera camera;

    //Tiled map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    public MapManager(OrthographicCamera camera, String mapName)
    {
        this.camera = camera;

        tiledMap = new TmxMapLoader().load(mapName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void Render()
    {

    }
}
