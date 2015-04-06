package com.game.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
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

    //Collisions on map
    MapObjects collidables;

    //Objects List
    private List<Sprite> objectList;

    private SpriteBatch sb = new SpriteBatch();

    public MapManager(GameManager gm, OrthographicCamera camera, String mapName)
    {
        this.camera = camera;

        tiledMap = new TmxMapLoader().load(mapName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f / 32f);

        objectList = new ArrayList<Sprite>();

        objectLayer = tiledMap.getLayers().get("objects");

        collidables = tiledMap.getLayers().get("Collision").getObjects();
    }

    public void render()
    {
        tiledMapRenderer.setView(camera); // TODO can probably move this to create method
        renderTileMap();
    }

    //Finding the object layer and rendering sprites on it
    private void renderTileMap(){ // TODO Render objects by Lowest to Hightest Y
        for(int i = 0; i < tiledMap.getLayers().getCount(); i++){
            tiledMapRenderer.render(new int[]{i});
            if(tiledMap.getLayers().get(i).equals(objectLayer)){
                sb.setProjectionMatrix(camera.combined);
                sb.begin();
                for(Sprite obj : objectList) {
                    obj.draw(sb);
                }
                sb.end();
            }
        }
    }

    public void dispose(){

        tiledMap.dispose();
        sb.dispose();
    }

    public void addObject(Sprite obj){
        objectList.add(obj);
    }

    public void clearObjects(){
        objectList.clear();
    }

    //getters
    public TiledMap getTiledMap(){
        return tiledMap;
    }
    public MapObjects getCollidables(){ return collidables; }


    //Remove the objecting being rendered,
    public void removeObject(TextureMapObject obj){
        objectLayer.getObjects().remove(obj);
    }


}
