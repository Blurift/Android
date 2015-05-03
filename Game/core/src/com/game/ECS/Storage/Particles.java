package com.game.ECS.Storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

/**
 * Created by Sean on 28/04/2015.
 */
public class Particles {

    public static ParticleEffectPool.PooledEffect iceProjectile(){
        ParticleEffect projectileEffect = new ParticleEffect();
        projectileEffect.load(Gdx.files.internal(Assets.iceProjEffect),
                Gdx.files.internal(Assets.iceProjParticle));
        projectileEffect.scaleEffect(0.01f);
        ParticleEffectPool projectileEffectPool = new ParticleEffectPool(projectileEffect, 1,2);

        return projectileEffectPool.obtain();
    }

    public static ParticleEffectPool.PooledEffect iceExplosion(){
        ParticleEffect projectileEffect = new ParticleEffect();
        projectileEffect.load(Gdx.files.internal("particles/iceExplosion.p"), Gdx.files.internal("particles/"));
        projectileEffect.scaleEffect(0.01f);
        ParticleEffectPool projectileEffectPool = new ParticleEffectPool(projectileEffect, 1,2);

        return projectileEffectPool.obtain();
    }
}
