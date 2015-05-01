package com.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.EntityManager;
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
        PlayerInputComponent inputComponent = new PlayerInputComponent();

        entityManager = new EntityManager(engine, sb, inputComponent);

        setScreen(new GameScreen(this, stage, inputComponent));
    }

    @Override
    public void resize(int width, int height) {

    }

    //Todo FPS move this to debug render system
    FPSLogger fps = new FPSLogger();
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Get everything from UI
        stage.act(Gdx.graphics.getDeltaTime());

        //Update then Render the game world, which always appears in the background
        entityManager.update();

        //Render the UI
        super.render();

        //Render the stage
        stage.draw();


        fps.log();
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

