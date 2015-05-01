package com.game.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.game.tools.IDepthObject;
import com.game.ECS.Tools.MapBodyBuilder;
import com.game.tools.SpriteDepthObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Keirron on 22/03/2015.
 */
public class MapManager {
    private OrthographicCamera camera;

    private GameManager gm;

    //Tiled map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    //Layers
    private MapLayer objectLayer; //Objects like player, monster, items

    //Collisions on map
    MapObjects collidables;

    //Objects List
    private Array<IDepthObject> objectList;

    private SpriteBatch sb = new SpriteBatch();

    public MapManager(GameManager gm, OrthographicCamera camera, String mapName)
    {
        this.camera = camera;
        this.gm = gm;
        tiledMap = new TmxMapLoader().load(mapName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f / 32f);

        objectList = new Array<IDepthObject>();

        objectLayer = tiledMap.getLayers().get("Objects");

        collidables = tiledMap.getLayers().get("Collision").getObjects();

        //Populate World with collisions
        MapBodyBuilder mapBodyBuilder = new MapBodyBuilder();
        mapBodyBuilder.buildShapes(tiledMap, "Collision", gm.getWorld());
        mapBodyBuilder.buildShapes(tiledMap, "Hitbox", gm.getWorld());
    }

    public void update()
    {
        tiledMapRenderer.setView(camera);
    }

    public void render()
    {

        renderTileMap();
    }

    //Finding the object layer and rendering sprites on it
    private void renderTileMap(){
        for(int i = 0; i < tiledMap.getLayers().getCount(); i++){
            tiledMapRenderer.render(new int[]{i});
            //int n = 0;
            if(tiledMap.getLayers().get(i).equals(objectLayer)){
                //Get all the objects currently on the map, convert them to sprites to add depth
                for (TextureMapObject textureObj :
                        tiledMap.getLayers().get(i).getObjects().getByType(TextureMapObject.class))
                {
                    Sprite objSprite = new Sprite(textureObj.getTextureRegion());
                    //Convert from pixel perfect
                    objSprite.setX(textureObj.getX()/32);
                    objSprite.setY(textureObj.getY()/32);
                    objSprite.setSize(objSprite.getWidth()/32, objSprite.getHeight()/32);
                    objectList.add(new SpriteDepthObject(objSprite));
                    //textureObj.setVisible(false);
                }
                sortObjects();
                sb.setProjectionMatrix(camera.combined);
                sb.begin();
                for(IDepthObject obj : objectList) {
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

    public void addObject(IDepthObject obj){
        objectList.add(obj);
    }

    public void clearObjects(){
        objectList.clear();
    }

    //Render objects by Lowest to Hightest Y
    public void sortObjects() {
        objectList.sort(new Comparator<IDepthObject>() {
            @Override
            public int compare(IDepthObject o1, IDepthObject o2) {
                if (o1.getY() == o2.getY())
                    return 0;
                return o1.getY() > o2.getY() ? -1 : 1;
            }
        });
    }

    /**
     * Getters
     */
    public TiledMap getTiledMap(){
        return tiledMap;
    }

    public MapObjects getCollidables(){ return collidables; }

    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    //Finds the player spawn on the map
    public Vector2 getPlayerSpawn(){
        MapObjects spawns = tiledMap.getLayers().get("Spawns").getObjects();
        for(MapObject spawn : spawns){
            if(spawn.getName().equals("PlayerSpawn")){
                Rectangle playerSpawn = ((RectangleMapObject) spawn).getRectangle();
                Vector2 spawnLoc = new Vector2();
                spawnLoc.x = ((playerSpawn.x+ playerSpawn.width * 0.5f) /gm.PIXELS_TO_METRES) -
                        gm.getPlayer().getSprite().getWidth() * 0.5f;
                spawnLoc.y = ((playerSpawn.y + playerSpawn.height * 0.5f) /gm.PIXELS_TO_METRES);

                return spawnLoc;
            }
        }

        return null;
    }

    public List<Vector2> getEnemySpawns(){
        List<Vector2> enemySpawns = new ArrayList<Vector2>();
        MapObjects spawns = tiledMap.getLayers().get("Spawns").getObjects();
        for(MapObject spawn : spawns){
            if(spawn.getName().equals("EnemySpawn")){
                Rectangle enemySpawn = ((RectangleMapObject) spawn).getRectangle();
                Vector2 spawnLoc = new Vector2();
                spawnLoc.x = ((enemySpawn.x+ enemySpawn.width * 0.5f) /gm.PIXELS_TO_METRES);
                spawnLoc.y = ((enemySpawn.y + enemySpawn.height * 0.5f) /gm.PIXELS_TO_METRES);

                enemySpawns.add(spawnLoc);
            }
        }

        return enemySpawns;
    }


    //Remove the objecting being rendered,
    public void removeObject(TextureMapObject obj){
        objectLayer.getObjects().remove(obj);
    }


}
