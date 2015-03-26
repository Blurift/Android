package com.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.game.managers.GameManager;
import com.game.screenManager.ScreenManager;

/**
 * Created by Keirron on 22/03/2015.
 */
public class Main implements ApplicationListener {
    private ScreenManager screenManager;

    @Override
    public void create()
    {
        screenManager = new ScreenManager();
        screenManager.addScreen(new GameManager());
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        screenManager.render();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        screenManager.dispose();
    }
}
