package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Managers.MapManager;

import java.util.Comparator;


/**
 * Created by Sean on 25/04/2015.
 *
 * Handles the rendering of all images in this order:
 *
 * TiledMap
 * Sprites with no Y Depth that appear under
 * Sprites with Y Depth sorted correctly
 * Sprites with no Y Depth that appear over
 * World filters
 *
 */
//TODO class doesn't need to be iterated anymore
public class RenderSystem extends SortedIteratingSystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<SpriteComponent> sm;
    private ComponentMapper<DepthComponent> dm;

    private SpriteBatch sb;
    private MapManager mapManager;
    private Array<Entity> depthEntities;
    private OrthographicCamera cam;

    private Engine engine;

    public RenderSystem(int order, SpriteBatch sb, MapManager mapManager) {
        super(Family.all(PositionComponent.class, SpriteComponent.class, DepthComponent.class).get(),
                new Comparator<Entity>()
                {
                    @Override
                    public int compare(Entity e1, Entity e2) {
                        float e1y = e1.getComponent(SpriteComponent.class).sprite.getY();
                        float e2y = e2.getComponent(SpriteComponent.class).sprite.getY();
                        if (e1y == e2y)
                            return 0;
                        return e1y > e2y ? -1 : 1;
                    }
                }, order
        );
        sm = ComponentMapper.getFor(SpriteComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);
        dm = ComponentMapper.getFor(DepthComponent.class);

        this.sb = sb;
        this.mapManager = mapManager;
        this.depthEntities = new Array<Entity>();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        super.forceSort();
        MapLayers mapLayers = mapManager.getMapLayers();
        for(int i = 0; i < mapLayers.getCount();i++){
            MapLayer mapLayer = mapLayers.get(i);
            if(mapLayer.equals(mapManager.getObjectLayer())){
                cam.update();
                sb.setProjectionMatrix(cam.combined);
                sb.begin();
                for(Entity entity : depthEntities){

                    Vector2 pos = new Vector2();
                    pos.x = pm.get(entity).x;
                    pos.y = pm.get(entity).y;
                    Sprite sprite = sm.get(entity).sprite;
                    //Draw sprite centred at position
                    sprite.setPosition(pos.x - sprite.getWidth() * 0.5f, pos.y - sprite.getHeight() * 0.5f);
                    sprite.draw(sb);
                }

                sb.end();
            }else{
                mapManager.getTiledMapRenderer().setView(cam);
                mapManager.getTiledMapRenderer().render(new int[]{i});
            }
        }
        depthEntities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        depthEntities.add(entity);
    }

    //Setters
    public void setRenderCamera(OrthographicCamera camera){
        this.cam = camera;
    }
}
