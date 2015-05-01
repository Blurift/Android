package com.game.ECS.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.game.*;
import com.game.ECS.Components.AnimationSetComponent;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.CameraComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.ProjectileComponent;
import com.game.ECS.Components.SpawningComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Components.StateComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Storage.Assets;
import com.game.ECS.Storage.GameVars;
import com.game.ECS.Systems.ProjectileSystem;
import com.game.ECS.Tools.MapBodyBuilder;
import com.game.ECS.Systems.AnimationSystem;
import com.game.ECS.Systems.CameraSystem;
import com.game.ECS.Systems.CharacterMovementSystem;
import com.game.ECS.Systems.FacingSystem;
import com.game.ECS.Systems.InputSystem;
import com.game.ECS.Systems.RenderSystem;
import com.game.ECS.Systems.SpawnSystem;
import com.game.ECS.Tools.Time;

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

    public EntityManager(Engine e, SpriteBatch sb, PlayerInputComponent inputComponent){
        engine = e;
        this.sb = sb;

        //Dependencies
        worldManager = new WorldManager();
        mapManager = new MapManager(engine);
        worldManager.getWorld().setContactFilter(new MyContactFilter());
        worldManager.getWorld().setContactListener(new MyContactListener(engine));

        //Extract collision from map
        MapBodyBuilder.buildShapes(mapManager.getMap(), "Hitbox", worldManager.getWorld());
        MapBodyBuilder.buildShapes(mapManager.getMap(), "Collision", worldManager.getWorld());

        //SYSTEM
        //Player Input System
        InputSystem is = new InputSystem(worldManager);
        engine.addSystem(is);
        //Movement System
        CharacterMovementSystem cms = new CharacterMovementSystem(0, worldManager.getWorld());
        engine.addSystem(cms);
        //Projectile System
        ProjectileSystem projs = new ProjectileSystem();
        engine.addSystem(projs);
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
        RenderSystem rs = new RenderSystem(3, sb, mapManager, worldManager.getWorld());
        engine.addSystem(rs);

        //ENTITIES
        //Player
        player = createPlayer(inputComponent);
        //GameWorld Camera
        //createCamera(player);
        //Static Objects
        mapManager.extractObjects();
        //Spawns
        mapManager.extractSpawns();

        //Listeners
        //Projectile
        Family family = Family.all(ProjectileComponent.class).get();
        engine.addEntityListener(family, new ProjectileListener());
    }

    public void update(){
        engine.update((float) Time.time * gameSpeed);
    }

    /**
     * Entity creation
     */


    public Entity createPlayer(PlayerInputComponent inputComponent){
        Entity entity = new Entity();

        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.sprite.setSize(2,2);

        BodyComponent bodyComponent = new BodyComponent(worldManager.createBody(
                WorldManager.BodyType.HUMANOID, entity
        ));
        bodyComponent.offset.y = 0.5f;

        //Set up camera
        CameraComponent cameraComponent = new CameraComponent();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, GameVars.VIRTUAL_WIDTH, GameVars.VIRTUAL_HEIGHT);
        camera.update();
        cameraComponent.camera = camera;
        engine.getSystem(RenderSystem.class).setRenderCamera(camera);

        entity.add(new VelocityComponent(0, 0))
                .add(bodyComponent)
                .add(new PlayerComponent(0))
                .add(new SpawningComponent(0,0,0))
                .add(new FacingComponent())
                .add(spriteComponent)
                .add(new AnimationSetComponent(Assets.animPlayerDruid()))
                .add(new StateComponent())
                .add(new DepthComponent(-16)).add(inputComponent)
                .add(cameraComponent);

        engine.addEntity(entity);
        return entity;
    }


    /**
     * Entity listeners
     */

    private class ProjectileListener implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {

        }

        @Override
        public void entityRemoved(Entity entity) {
            Body body = entity.getComponent(BodyComponent.class).body;
            body.getWorld().destroyBody(body);
        }
    }

    /**
     * Getters
     */

    public Entity getPlayer(){
        return player;
    }
}
