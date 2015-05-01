package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.CameraComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.ParticleEffectComponent;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.ProjectileComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Managers.WorldManager;
import com.game.ECS.Storage.ProjectilePrefabs;

import javafx.scene.Camera;

/**
 * Created by Sean on 27/04/2015.
 */
public class InputSystem extends EntitySystem{

    private WorldManager worldManager;

    private Engine engine;
    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> vm;
    private ComponentMapper<PlayerInputComponent> pim;
    private ComponentMapper<CameraComponent> cm;
    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<FacingComponent> fm;

    public InputSystem(WorldManager worldManager) {
        this.worldManager = worldManager;

        vm = ComponentMapper.getFor(VelocityComponent.class);
        pim = ComponentMapper.getFor(PlayerInputComponent.class);
        cm = ComponentMapper.getFor(CameraComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);
        fm = ComponentMapper.getFor(FacingComponent.class);
    }

    public void addedToEngine(Engine engine) {
        this.engine = engine;
        entities = engine.getEntitiesFor(Family.all(VelocityComponent.class,
                PlayerInputComponent.class,
                CameraComponent.class,
                PositionComponent.class,
                FacingComponent.class).get());
    }


    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            VelocityComponent vel = vm.get(entities.get(i));
            PlayerInputComponent pic =  pim.get(entities.get(i));
            CameraComponent cc =  cm.get(entities.get(i));
            PositionComponent pc =  pm.get(entities.get(i));
            FacingComponent fc =  fm.get(entities.get(i));
            //Player is free to move around
            if(pic.currentState.equals(PlayerInputComponent.States.FREE))
                vel.velocity = pic.touchpadDir;
            //Player is aiming a spell, camera chases inbetween player and where player is touching
            if(pic.currentState.equals(PlayerInputComponent.States.AIMING) && pic.screenPos != null){

                Vector2 charPos = new Vector2(pc.x, pc.y);
                Vector3 unprojected = cc.camera.unproject(new Vector3(
                        pic.screenPos.x, pic.screenPos.y, 0));
                Vector2 touchedPos = new Vector2(unprojected.x, unprojected.y);

                //Get inbetween point of player, and where they are touching
                Vector2 distance = new Vector2(touchedPos).sub(charPos);

                //Set facing where aiming
                Vector2 direction = new Vector2(distance).nor();
                fc.dir.x = Math.round(direction.x);
                fc.dir.y = Math.round(direction.y);

                //Focus cam on mid point of distance
                cc.offset.x = distance.x * 0.5f;
                cc.offset.y = distance.y * 0.5f;

            }
            else{
                cc.offset.x = 0;
                cc.offset.y = 0;
            }

            if(pic.spellCast != null){
                Vector2 charPos = new Vector2(pc.x, pc.y);
                Vector3 unprojected = cc.camera.unproject(new Vector3(
                        pic.spellCast.x, pic.spellCast.y, 0));
                Vector2 touchedPos = new Vector2(unprojected.x, unprojected.y);

                Vector2 dir = new Vector2(touchedPos).sub(charPos).nor();

                engine.addEntity(ProjectilePrefabs.createIce(new Vector2(pc.x, pc.y),
                        entities.get(i), dir, worldManager));

                pic.spellCast = null;
            }

        }
    }
}
