package com.game.ECS.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.AnimationSetComponent;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.CameraComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.SpawningComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Components.StateComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Other.Assets;
import com.game.ECS.Other.GameVars;
import com.game.ECS.Systems.AnimationSystem;
import com.game.ECS.Systems.CameraSystem;
import com.game.ECS.Systems.CharacterMovementSystem;
import com.game.ECS.Systems.FacingSystem;
import com.game.ECS.Systems.RenderSystem;
import com.game.ECS.Systems.SpawnSystem;
import com.game.ECS.Tools.Time;

import javafx.scene.Camera;

/**
 * Created by Sean on 25/04/2015.
 */
public class EntityManager {
    private Engine engine;
    private SpriteBatch sb;

    private WorldManager worldManager;
    private MapManager mapManager;

    private float gameSpeed = 1;
    private Entity player;

    public EntityManager(Engine e, SpriteBatch sb){
        engine = e;
        this.sb = sb;
        //Dependencies
        mapManager = new MapManager(engine);
        worldManager = new WorldManager();

        //SYSTEM
        //Movement System
        CharacterMovementSystem cms = new CharacterMovementSystem(0, worldManager.getWorld());
        engine.addSystem(cms);
        //Spawn System
        SpawnSystem ss = new SpawnSystem(1, mapManager);
        engine.addSystem(ss);
        //Facing System - Where the player is facing
        FacingSystem fs = new FacingSystem();
        engine.addSystem(fs);
        //Animation State System, setting the players current animation
        AnimationSystem as = new AnimationSystem();
        engine.addSystem(as);
        //Camera System
        CameraSystem cs = new CameraSystem(2);
        engine.addSystem(cs);
        //Render System (Last, after all updates)
        RenderSystem rs = new RenderSystem(3, sb, mapManager);
        engine.addSystem(rs);

        //ENTITIES
        //Player
        player = createPlayer();
        //GameWorld Camera
        createCamera(player);
        //Static Objects
        mapManager.extractObjects();
        //Spawns
        mapManager.extractSpawns();
    }

    public void update(){
        engine.update((float) Time.time * gameSpeed);
    }

    public Entity createPlayer(){
        Entity entity = new Entity();
        SpriteComponent spriteComponent = new SpriteComponent();
        //Todo don't input direct size values here
        spriteComponent.sprite.setSize(2,2);

        entity.add(new VelocityComponent(0, 0))
                .add(new BodyComponent(worldManager.createBody(
                        WorldManager.BodyType.HUMANOID, entity
                )))
                .add(new PlayerComponent(0))
                .add(new SpawningComponent(0,0,0))
                .add(new FacingComponent())
                .add(spriteComponent)
                .add(new AnimationSetComponent(Assets.animPlayerDruid()))
                .add(new StateComponent())
                .add(new DepthComponent(0));

        engine.addEntity(entity);
        return entity;
    }

    public void createCamera(Entity player){
        CameraComponent cameraEntity = new CameraComponent();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, GameVars.VIRTUAL_WIDTH, GameVars.VIRTUAL_HEIGHT);
        camera.update();
        cameraEntity.camera = camera;
        cameraEntity.target = player;

        Entity entity = new Entity().add(cameraEntity);

        engine.addEntity(entity);
        engine.getSystem(RenderSystem.class).setRenderCamera(camera);
    }

    /**
     * Getters
     */

    public Entity getPlayer(){
        return player;
    }
}
