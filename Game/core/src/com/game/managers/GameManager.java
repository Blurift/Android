package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.game.controllers.CharacterController;

/**
 * Created by Keirron on 22/03/2015.
 */
public class GameManager {
    private OrthographicCamera uiCamera;
    private OrthographicCamera camera;

    private CharacterController mainPlayer;
    private MapManager mapManager;

    public GameManager()
    {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/3,h/3);
        camera.update();

        mainPlayer = new CharacterController();
        mapManager = new MapManager(camera, "");
    }

    public void Render()
    {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear((GL20.GL_COLOR_BUFFER_BIT));

        uiCamera.update();
        camera.update();


        mapManager.Render();
    }
}
