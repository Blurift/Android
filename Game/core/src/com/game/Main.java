package com.game;

import com.badlogic.gdx.ApplicationListener;
import com.game.managers.GameManager;

/**
 * Created by Keirron on 22/03/2015.
 */
public class Main implements ApplicationListener {
    private GameManager gameManager;

    @Override
    public void create() {
        gameManager = new GameManager();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
