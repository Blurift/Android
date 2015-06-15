package com.game.ECS.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Keirron on 16/06/2015.
 */
public class SpellBookScreen implements Screen {

    private ImageButton cancelSpellBtn;
    private Texture cancelTexture;

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    public SpellBookScreen(Main game, final Stage stage, PlayerInputComponent playerInput)
    {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();
    }

    @Override
    public void show() {
        playerInput.gameSpeed = 0.1f;
        this.stage.addActor(this.cancelSpellBtn);
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

    }

    @Override
    public void dispose() {

    }

    public ImageButton createCancelSpellBtn(){
        cancelTexture = ResourceManager.uiCancelButton();
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", cancelTexture);
        castBtnSkin.add("down", cancelTexture);
        castBtnSkin.add("over", cancelTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        cancelSpellBtn = new ImageButton(buttonStyle);
        cancelSpellBtn.setSize(cancelSpellBtn.getWidth()*scale, cancelSpellBtn.getHeight()*scale);
        cancelSpellBtn.setPosition(stage.getViewport().getCamera().viewportWidth - (45*scale + cancelSpellBtn.getWidth()),
                stage.getViewport().getCamera().viewportHeight - (45*scale + cancelSpellBtn.getHeight()));
        cancelSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, stage, playerInput));
            }
        });

        return cancelSpellBtn;
    }
}
