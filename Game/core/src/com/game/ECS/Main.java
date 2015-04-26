package com.game.ECS;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.ECS.Managers.EntityManager;
import com.game.ECS.Managers.WorldManager;
import com.game.ECS.Screen.GameScreen;

/**
 * Created by Sean on 25/04/2015.
 */
public class Main extends Game {

    private EntityManager entityManager;
    private Stage stage;

    @Override
    public void create() {
        Engine engine = new Engine();
        stage = new Stage();
        SpriteBatch sb = new SpriteBatch();
        entityManager = new EntityManager(engine, sb);

        //setScreen(new GameScreen(stage, player));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        //Get everything from UI
        stage.act(Gdx.graphics.getDeltaTime());

        //Update then Render the game world, which always appears in the background
        entityManager.update();

        //Render the UI
        super.render();

        //Render the stage
        stage.draw();
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
