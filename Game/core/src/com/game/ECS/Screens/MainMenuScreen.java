package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Systems.AIDirectorSystem;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Keirron on 13/06/2015.
 */
public class MainMenuScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Image gameLogo;
    private ImageButton newGameBtn;
    private ImageButton exitGameBtn;
    private ImageButton highscoresGameBtn;



    public MainMenuScreen(Main game, Stage stage, PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //Create objects
        this.gameLogo = createGameLogo();
        this.newGameBtn = createNewGameButton();
        this.exitGameBtn = createExitButton();
        this.highscoresGameBtn = createHighscoresButton();
    }

    @Override
    public void show() {
        stage.addActor(gameLogo);
        stage.addActor(newGameBtn);
        stage.addActor(exitGameBtn);
        stage.addActor(highscoresGameBtn);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        gameLogo.remove();
        newGameBtn.remove();
        highscoresGameBtn.remove();
        exitGameBtn.remove();
    }

    @Override
    public void dispose() {
        //Dispose shit
    }

    private Image createGameLogo(){
        Image GameLogo = new Image(new TextureRegion(ResourceManager.gameLogo()));
        GameLogo.setSize(GameLogo.getWidth()*scale*0.5f, GameLogo.getHeight()*scale*0.5f);
        GameLogo.setPosition((stage.getViewport().getCamera().viewportWidth*0.5f)-(GameLogo.getWidth()*0.5f),
                (stage.getViewport().getCamera().viewportHeight*0.85f)-(GameLogo.getHeight()*0.5f));
        return GameLogo;
    }

    public ImageButton createExitButton() {
        Texture exitTexture = ResourceManager.exitMenuButton();
        Skin exitBtnSkin = new Skin();
        exitBtnSkin.add("up", exitTexture);
        exitBtnSkin.add("down", exitTexture);
        exitBtnSkin.add("over", exitTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = exitBtnSkin.getDrawable("up");
        buttonStyle.down = exitBtnSkin.getDrawable("down");
        buttonStyle.over = exitBtnSkin.getDrawable("over");
        exitGameBtn = new ImageButton(buttonStyle);
        exitGameBtn.setSize(exitGameBtn.getWidth()*scale*0.5f, exitGameBtn.getHeight()*scale*0.5f);
        exitGameBtn.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (exitGameBtn.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.20f) - (exitGameBtn.getHeight() * 0.5f));
        exitGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                Gdx.app.exit();
            }
        });

        return exitGameBtn;
    }
    public ImageButton createHighscoresButton() {
        Texture highscoreTexture = ResourceManager.highscoresButton();
        Skin highscoreBtnSkin = new Skin();
        highscoreBtnSkin.add("up", highscoreTexture);
        highscoreBtnSkin.add("down", highscoreTexture);
        highscoreBtnSkin.add("over", highscoreTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = highscoreBtnSkin.getDrawable("up");
        buttonStyle.down = highscoreBtnSkin.getDrawable("down");
        buttonStyle.over = highscoreBtnSkin.getDrawable("over");
        highscoresGameBtn = new ImageButton(buttonStyle);
        highscoresGameBtn.setSize(highscoresGameBtn.getWidth()*scale*0.5f, highscoresGameBtn.getHeight()*scale*0.5f);
        highscoresGameBtn.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (highscoresGameBtn.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.40f) - (highscoresGameBtn.getHeight() * 0.5f));
        highscoresGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                game.setScreen(new HighscoreScreen(game, stage, playerInput));
            }
        });

        return highscoresGameBtn;
    }

    public ImageButton createNewGameButton() {
        Texture newGameTexture = ResourceManager.newGameMenuButton();
        Skin newGameBtnSkin = new Skin();
        newGameBtnSkin.add("up", newGameTexture);
        newGameBtnSkin.add("down", newGameTexture);
        newGameBtnSkin.add("over", newGameTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = newGameBtnSkin.getDrawable("up");
        buttonStyle.down = newGameBtnSkin.getDrawable("down");
        buttonStyle.over = newGameBtnSkin.getDrawable("over");
        playerInput.gameScore = 0;
        playerInput.lives = 3;
        newGameBtn = new ImageButton(buttonStyle);
        newGameBtn.setSize(newGameBtn.getWidth()*scale*0.5f, newGameBtn.getHeight()*scale*0.5f);
        newGameBtn.setPosition((stage.getViewport().getCamera().viewportWidth * 0.5f) - (newGameBtn.getWidth() * 0.5f),
                (stage.getViewport().getCamera().viewportHeight * 0.60f) - (newGameBtn.getHeight() * 0.5f));
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                //Stop music
                game.getEngine().addSystem(new AIDirectorSystem(
                        game.getEntityManager().getMapManager(),
                        game.getEntityManager().getWorldManager()));
                game.getEntityManager().createPlayer(playerInput);
                game.setScreen(new GameScreen(game, stage, playerInput));
            }
        });
        return newGameBtn;
    }
}
