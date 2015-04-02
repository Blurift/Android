package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.game.SpellSystem.Projectile;
import com.game.SpellSystem.ProjectileManager;
import com.game.UI.UIManager;
import com.game.controllers.CharacterController;
import com.game.screenManager.Screen;

/**
 * Created by Keirron on 22/03/2015.
 */
public class GameManager extends Screen {
    private OrthographicCamera camera;

    private CharacterController mainPlayer;
    private MapManager mapManager;
    private UIManager uiManager;
    private FilterManager filterManager;
    private ProjectileManager projectileManager;

    private float gameSpeed = 1;


    public GameManager()
    {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/3,h/3); // TODO the /3 makes it bigger, there is a better way to do this...
        camera.update();

        //Create Main Player
        mainPlayer = new CharacterController("testCharacter.png" , new Vector2(500, 500));

        //Intantiate Managers

        mapManager = new MapManager(camera, "map/MyCrappyMap.tmx");
        filterManager = new FilterManager();
        uiManager = new UIManager(this);
        projectileManager = new ProjectileManager();

        //Add objects to sprite

    }

    @Override
    public void render()
    {

        float delta = Gdx.graphics.getDeltaTime() * gameSpeed;

        /**
         * Updates
          */
        mapManager.clearObjects();
        mapManager.addObject(mainPlayer.getSprite());
        for(Projectile projectile : projectileManager.getProjectiles()){
            mapManager.addObject(projectile.getSprite());
        }

        projectileManager.update();




        mainPlayer.updateVelocity(uiManager.getHUD().getKnobPercentX(), uiManager.getHUD().getKnobPercentY());
        mainPlayer.update(delta);

        //Camera follow player
        float defaultCamX = mainPlayer.getSprite().getX() + (mainPlayer.getSprite().getWidth() / 2);
        float defaultCamY = mainPlayer.getSprite().getY() + (mainPlayer.getSprite().getHeight() / 2);
        camera.position.set(defaultCamX, defaultCamY, 0);
        camera.update();

        /**
         * Render
         */
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear((GL20.GL_COLOR_BUFFER_BIT));
        //Render Map
        mapManager.render();

        //Render Filters
        filterManager.render();

        //Render UI
        uiManager.render();

    }


    /**
     * Getters
     */
    public ProjectileManager getProjectileManager(){
        return projectileManager;
    }

    public FilterManager getFilterManager(){
        return filterManager;
    }

    public CharacterController getPlayer(){
        return mainPlayer;
    }

    /**
     * Setters
     */

    @Override
    public void dispose()
    {
        mainPlayer.dispose();
        uiManager.dispose();
        mapManager.dispose();
    }
}
