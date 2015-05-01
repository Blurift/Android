package com.game.SpellSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Storage.B2DVars;
import com.game.managers.GameManager;
import com.game.tools.IDepthObject;

/**
 * Created by Sean on 2/04/2015.
 *
 * A projectile that is being shot.
 *
 * Will need to use poolable
 *
 */
public class Projectile implements IDepthObject{

        private boolean kill = false;
        private Vector2 position;
        private Texture texture;
        private Sprite projSprite;
        private Vector2 direction;
        private GameManager gm;
        private ParticleEffect projectileEffect;
        private ParticleEffectPool projectileEffectPool;
        private Array<ParticleEffectPool.PooledEffect> effects = new Array();
        private ParticleEffectPool.PooledEffect effect;
        private float projSpeed = 8;

        //Box2d body
        private Body body;

        /**
         * Bullet constructor. Just initialize variables.
         */
        public Projectile(GameManager gm, float posX, float posY, Vector2 direction) {
            this.gm = gm;
            this.position = new Vector2(posX, posY);
            this.direction = direction;


            //Sprite
            texture = new Texture(Gdx.files.internal("projectiles/iceball.png"));
            projSprite = new Sprite(texture);
            projSprite.setSize(2f, 2f);
            projSprite.setX(posX - projSprite.getWidth()*0.5f);
            projSprite.setY(posY - projSprite.getHeight()*0.5f);


            //Particles
            projectileEffect = new ParticleEffect();
            projectileEffect.load(Gdx.files.internal("particles/ice.p"), Gdx.files.internal("particles/"));
            projectileEffect.scaleEffect(0.01f);
            projectileEffectPool = new ParticleEffectPool(projectileEffect, 1, 2);

            effect = projectileEffectPool.obtain();

            //Get the angle of direction and flip, as the emitter is flipped
            float emitterAngle = direction.angle() -180f;
            rotateBy(emitterAngle);
            effects.add(effect);

            //Box2D
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set((position.x )/gm.PIXELS_TO_METRES, (position.y)/gm.PIXELS_TO_METRES);
            bodyDef.fixedRotation = true;
            body = gm.getWorld().createBody(bodyDef);
            CircleShape shape = new CircleShape();
            shape.setRadius(0.25f/gm.PIXELS_TO_METRES);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.filter.categoryBits = B2DVars.BIT_PROJECTILE;
            fixtureDef.filter.maskBits = B2DVars.BIT_HITBOX;
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(this);
            shape.dispose();
        }

    //Rotates the emitters of the particle effect
    public void rotateBy(float amountInDegrees) {
        Array<ParticleEmitter> emitters = effect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter.ScaledNumericValue val = emitters.get(i).getAngle();
            float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
            float h1 = amountInDegrees + amplitude;
            float h2 = amountInDegrees - amplitude;
            val.setHigh(h1, h2);
            val.setLow(amountInDegrees);
        }
    }


        /**
         * Method called each frame, which updates the bullet.
         */
        public void update (float delta) {

            // update bullet position
            body.setLinearVelocity(direction.x/gm.PIXELS_TO_METRES * projSpeed * gm.getGameSpeed(),
                    direction.y/gm.PIXELS_TO_METRES * projSpeed * gm.getGameSpeed());
            position.x = body.getPosition().x * gm.PIXELS_TO_METRES;
            position.y = body.getPosition().y * gm.PIXELS_TO_METRES;
            effect.setPosition(position.x, position.y);
            projSprite.setPosition(position.x - projSprite.getWidth()*0.5f,
                    position.y - projSprite.getWidth()*0.5f);
        }

        /**
         * Renders the object on the map
         */
        public void draw(SpriteBatch batch){
            for (int i = effects.size - 1; i >= 0; i--) {
                ParticleEffectPool.PooledEffect effect = effects.get(i);
                effect.draw(batch, Gdx.graphics.getDeltaTime());
                if (effect.isComplete()) {
                    effect.free();
                    effects.removeIndex(i);
                }
            }
            if(!kill)
                projSprite.draw(batch);
            else if (effect.isComplete()){
                  gm.getProjectileManager().removeProjectile(this);
                dispose();
            }
        }



        /**
         * Getters
         */
        public float getY(){
            return position.y;
        }
        public Sprite getSprite() {
            return projSprite;
        }

        //TODO add kill function with iceExplosion

        public void kill() {
            projectileEffect.dispose();
            //Particles
            projectileEffect = new ParticleEffect();
            projectileEffect.load(Gdx.files.internal("particles/iceExplosion.p"), Gdx.files.internal("particles/"));
            projectileEffect.scaleEffect(0.01f);
            projectileEffectPool = new ParticleEffectPool(projectileEffect, 1, 2);

            effect = projectileEffectPool.obtain();
            effects.clear();
            effects.add(effect);
            kill = true;
            projSpeed = 0;
            gm.addBodyToDestroy(body);
        }

        public void dispose() {
            projectileEffect.dispose();
            effect.dispose();
            texture.dispose();
        }

}
