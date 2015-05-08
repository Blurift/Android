package com.game.ECS.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.Assets;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Sean on 28/04/2015.
 */
public class SpellAimingScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;
    private InputMultiplexer multiplexer;

    private ImageButton cancelSpellBtn;
    private Texture cancelTexture;

    public SpellAimingScreen(Main game, Stage stage, PlayerInputComponent playerInput) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //More input
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(createInputAdapter());
        //Screen components
        this.cancelSpellBtn = createCancelSpellBtn();

    }

    @Override
    public void show() {


        this.stage.addActor(this.cancelSpellBtn);
        Gdx.input.setInputProcessor(multiplexer);

        this.playerInput.currentState = PlayerInputComponent.States.AIMING;
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
        this.cancelSpellBtn.remove();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        cancelTexture.dispose();
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

    public InputAdapter createInputAdapter(){
        return new InputAdapter() {
            private float camModifierX;
            private float camModifierY;

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {

                playerInput.screenPos = null;
                game.setScreen(new GameScreen(game, stage, playerInput));
                playerInput.spellCast = new Vector2(screenX, screenY);
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(playerInput.screenPos == null)
                    playerInput.screenPos = new Vector2(screenX, screenY);
                else {
                    playerInput.screenPos.x = screenX;
                    playerInput.screenPos.y = screenY;
                }

                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                playerInput.screenPos = new Vector2(screenX, screenY);
                return true;
            }
        };
    }
}
