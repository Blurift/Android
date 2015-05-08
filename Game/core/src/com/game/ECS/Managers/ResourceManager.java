package com.game.ECS.Managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Created by Sean on 6/05/2015.
 */
public class ResourceManager {
    private static AssetManager manager;


    public static void load()
    {
        manager = new AssetManager();

        /*
            Textures
        */

        //UI
        manager.load("UI/castButton.png", Texture.class);
        manager.load("UI/castButtonShadow.png", Texture.class);
        manager.load("UI/cancelButton.png", Texture.class);

        manager.load("UI/touchpad/touchBackground.png", Texture.class);
        manager.load("UI/touchpad/touchKnob.png", Texture.class);

        //Character Sheets
        manager.load("character/DruidSheet.png", Texture.class);

        //Projectiles
        manager.load("projectiles/iceball.png", Texture.class);

        //Missing Texture
        manager.load("Blank.png", Texture.class);

        /*
            Particle Effects
        */
        ParticleEffectLoader.ParticleEffectParameter param = new ParticleEffectLoader.ParticleEffectParameter();
        //param.imagesDir = new FileHandle("particles/");
        manager.load("particles/ice.p", ParticleEffect.class, param);
        manager.load("particles/iceExplosion.p", ParticleEffect.class, param);
    }

    public static boolean update(){
        return manager.update();
    }

    public static float getProgress(){
        return manager.getProgress();
    }

    public static boolean isLoaded()
    {
        if(manager != null) {
            if (manager.getProgress() >= 1)
                return true;
        }
        return false;
    }

    public static void dispose()
    {
        manager.dispose();
        manager = null;
    }

    public static Texture sheetDruid() {
        return manager.get("character/DruidSheet.png", Texture.class);
    }

    public static Texture uiCastSpellBtn() {
        return manager.get("UI/castButton.png", Texture.class);
    }

    public static Texture uiCastSpellShadow() {
        return manager.get("UI/castButtonShadow.png", Texture.class);
    }

    public static Texture uiCancelButton() {
        return manager.get("UI/cancelButton.png", Texture.class);
    }

    public static Texture uiKnobBG(){
        return manager.get("UI/touchpad/touchBackground.png", Texture.class);
    }

    public static Texture uiKnob(){
        return manager.get("UI/touchpad/touchKnob.png", Texture.class);
    }


    public static Texture projIce() {
        return manager.get("projectiles/iceball.png", Texture.class);
    }

    public static Texture blank(){
        return manager.get("Blank.png", Texture.class);
    }

}