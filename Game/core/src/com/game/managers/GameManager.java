package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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


    private SpriteBatch sb;


    public GameManager()
    {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/3,h/3); // TODO the /3 makes it bigger, there is a better way to do this...
        camera.update();

        mainPlayer = new CharacterController("testCharacter.png" , new Vector2(500, 500));
        mapManager = new MapManager(camera, "map/MyCrappyMap.tmx");
        uiManager = new UIManager();

        mapManager.addObject(mainPlayer.getSprite());
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear((GL20.GL_COLOR_BUFFER_BIT));

        float delta = Gdx.graphics.getDeltaTime();

        camera.update();
        mapManager.render();
        uiManager.render();

        mainPlayer.updateVelocity(uiManager.getKnobPercentX(), uiManager.getKnobPercentY());
        mainPlayer.update(delta);

        //Camera follow player
        float defaultCamX = mainPlayer.getSprite().getX() + (mainPlayer.getSprite().getWidth() / 2);
        float defaultCamY = mainPlayer.getSprite().getY() + (mainPlayer.getSprite().getHeight() / 2);
        camera.position.set(defaultCamX, defaultCamY, 0);
    }

    @Override
    public void dispose()
    {
        mainPlayer.dispose();
        uiManager.dispose();
        mapManager.dispose();
    }
}
