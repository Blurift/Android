package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.AIComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SoundSetComponent;
import com.game.ECS.Components.VelocityComponent;

import java.util.Random;

/**
 * Created by Sean on 3/05/2015.
 *
 * Used to Manage all AI as a whole, where they spawn, when, global goals
 *
 */
public class AISystem extends IteratingSystem{

    private PlayerInputComponent input;
    private Engine engine;

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<AIComponent> aim;

    public AISystem(PlayerInputComponent input) {
        super(Family.all(AIComponent.class, PositionComponent.class).get());
        this.input = input;
    }

    public void addedToEngine(Engine engine){
        super.addedToEngine(engine);
        this.engine = engine;

        pm = ComponentMapper.getFor(PositionComponent.class);
        aim = ComponentMapper.getFor(AIComponent.class);
    }
    Random rdmSound = new Random();
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //Todo Players should probably be found a different way

        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PositionComponent.class,
                PlayerComponent.class).get());

        if(players.size() != 0){
            Entity player = players.get(0);
            PositionComponent pos = pm.get(entity);
            AIComponent ai = aim.get(entity);
            Vector2 enemyPos = new Vector2(pos.x, pos.y);
            pos = pm.get(player);
            Vector2 playerPos = new Vector2(pos.x, pos.y);

            Vector2 dis = new Vector2(playerPos).sub(enemyPos); //distance
            Vector2 dir = new Vector2(dis).nor();
            VelocityComponent vel = entity.getComponent(VelocityComponent.class);

            float r = 3; //radius to attack
            float rr = 2; //run back radius after attacking

            if(ai.state == AIComponent.AIState.RETREATING){
                if(ai.retreatTime == 0){
                    ai.state = AIComponent.AIState.FOLLOWING;
                }else{
                    //TODO Delta here should be fixed
                    ai.retreatTime-= deltaTime;
                    if(ai.retreatTime < 0)
                        ai.retreatTime = 0;
                }

            }else if(Math.abs(dis.x) < r && Math.abs(dis.y) < r){
                if(ai.state != AIComponent.AIState.ATTACKING) {
                    SoundSetComponent sounds = entity.getComponent(SoundSetComponent.class);
                    if(sounds != null){
                        if(sounds.lunge != null){
                            if(sounds.currentSound != null)
                                sounds.currentSound.stop();
                            sounds.currentSound = sounds.lunge.get(rdmSound.nextInt(sounds.lunge.size));
                            sounds.currentSound.play(0.5f);

                        }
                    }
                    ai.state = AIComponent.AIState.ATTACKING;
                }
            }else{
                ai.state = AIComponent.AIState.FOLLOWING;
            }



            //Slow down
            float speed = 0;
            if(ai.state == AIComponent.AIState.FOLLOWING) {
                speed = 20f;
            }else if(ai.state == AIComponent.AIState.ATTACKING) {
                speed = 22f;
            }else if(ai.state == AIComponent.AIState.RETREATING) {
                dir.x-= dir.x*2;
                dir.y-= dir.y*2;
                speed = 21f;
            }

            dir.x *= speed;
            dir.y *= speed;
            if(vel != null){
                vel.velocity = dir;
            }
        }
    }
}
