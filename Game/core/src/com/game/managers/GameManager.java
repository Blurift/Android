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
    private OrthographicCamera uiCamera;
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
        camera.setToOrtho(false, w/3,h/3);
        camera.update();

        mainPlayer = new CharacterController("testCharacter.png" , new Vector2(50, 50));
        mapManager = new MapManager(camera, "map/MyCrappyMap.tmx");
        uiManager = new UIManager();

        mapManager.addObject(mainPlayer.getMapObject());
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear((GL20.GL_COLOR_BUFFER_BIT));

        float delta = Gdx.graphics.getDeltaTime();

        uiCamera.update();
        camera.update();

        mapManager.render();
        uiManager.render();

        mainPlayer.updateVelocity(uiManager.getKnobPercentX(), uiManager.getKnobPercentY());
        mainPlayer.update(delta);
    }

    @Override
    public void dispose()
    {
        mainPlayer.dispose();
        uiManager.dispose();
        mapManager.dispose();
    }
}
