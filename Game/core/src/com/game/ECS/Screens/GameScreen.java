package com.game.ECS.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.Assets;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

/**
 * Created by Sean on 26/04/2015.
 *
 * Screen containing the HUD for the game.
 *
 */
public class GameScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Texture castTexture;
    private float minButtonHeight;
    private float maxButtonHeight;
    private ImageButton castSpellBtn;
    private Image castSpellBtnShadow;
    private Texture castSpellBtnShadowTexture;

    private float barSize;

    public GameScreen(Main game, Stage stage, PlayerInputComponent playerInput){
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //Screen components
        this.touchpad = createTouchpad();
        this.castSpellBtn = createCastSpellBtn();

        //Scale some things
        this.barSize = 300*scale;

        //Set the input as this stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.addActor(touchpad);
        stage.addActor(castSpellBtn);
        stage.addActor(castSpellBtnShadow);

        this.playerInput.currentState = PlayerInputComponent.States.FREE;
    }

    //For the animation of the cast spell button
    private boolean isRising = true;
    private float moveSpeed = 10;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void render(float delta) {
        //Feed touchpad input to player entity

        playerInput.touchpadDir.x = touchpad.getKnobPercentX();
        playerInput.touchpadDir.y = touchpad.getKnobPercentY();

        //Animate cast spell button
        //TODO need to lerp book floating up and down
        moveSpeed = 20f;
        if(isRising){
            castSpellBtn.setY(castSpellBtn.getY()+ moveSpeed*Gdx.graphics.getDeltaTime());
            if(castSpellBtn.getY() >= maxButtonHeight){
                isRising = false;
            }
        }else if(!isRising){
            castSpellBtn.setY(castSpellBtn.getY() - moveSpeed*Gdx.graphics.getDeltaTime());
            if(castSpellBtn.getY() <= minButtonHeight) {
                isRising = true;
            }
        }

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barOffset = 55*scale; //Offset from bottom
        Vector2 healthStart = new Vector2();
        Vector2 healthEnd = new Vector2();
        float barLength = 1;
        if(playerInput.playerHealth != null) {
            healthStart.x = stage.getViewport().getScreenWidth() * 0.5f - barSize * 0.5f;
            healthStart.y = barOffset;

            healthEnd.x = healthStart.x + barSize;
            healthEnd.y = barOffset;


            //Behind Health Bar
            shapeRenderer.setColor(Color.BLACK);
            //Draw fancier back bar

            barLength = 1*scale;
            for (int i = 15; i > 0; i = i - 3) {
                shapeRenderer.rectLine(new Vector2(healthStart.x - barLength, healthStart.y),
                        new Vector2(healthEnd.x + barLength, healthEnd.y), i*scale);
                barLength++;
            }
            //Actual Health Representation
            shapeRenderer.setColor(Color.RED);
            healthEnd.x = healthStart.x + barSize * (playerInput.playerHealth.currentHealth /
                    playerInput.playerHealth.maxHealth);
            shapeRenderer.rectLine(healthStart, healthEnd, 10*scale);
        }

        if(playerInput.playerInk != null) {
            //Draw InkBar
            barOffset = 35 * scale; //Offset from bottom


            //Actual Ink
            healthStart.x = stage.getViewport().getScreenWidth() * 0.5f - barSize * 0.5f;
            healthStart.y = barOffset;
            healthEnd.x = healthStart.x + barSize;
            healthEnd.y = barOffset;


            //Behind Ink Bar
            shapeRenderer.setColor(Color.BLACK);
            //Draw fancier back bar
            barLength = 1*scale;
            for (int i = 15; i > 0; i = i - 3) {
                shapeRenderer.rectLine(new Vector2(healthStart.x - barLength, healthStart.y),
                        new Vector2(healthEnd.x + barLength, healthEnd.y), i*scale);
                barLength++;
            }
            //Actual Health Representation
            shapeRenderer.setColor(Color.PURPLE);
            Vector2 healthMini = new Vector2(healthEnd);
            healthEnd.x = healthStart.x + barSize * (playerInput.playerInk.currentInk /
                    playerInput.playerInk.maxInk);
            healthMini.x = (healthStart.x + 5*scale) + (barSize - 10*scale) * (playerInput.playerInk.currentInk /
                    playerInput.playerInk.maxInk);
            shapeRenderer.rectLine(healthStart, healthEnd, 10*scale);

            shapeRenderer.setColor(Color.MAROON);
            shapeRenderer.rectLine(new Vector2(healthStart.x + 5*scale, healthStart.y - 1*scale),
                    new Vector2(healthMini.x, healthMini.y - 1*scale), 1*scale);
            //Border
        }


        shapeRenderer.end();

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
        touchpad.remove();
        castSpellBtn.remove();
        castSpellBtnShadow.remove();

        //No longer giving input
        playerInput.touchpadDir.x = 0;
        playerInput.touchpadDir.y = 0;
    }

    @Override
    public void dispose() {
        touchpadSkin.dispose();
        castTexture.dispose();
        castSpellBtnShadowTexture.dispose();
    }

    public Touchpad createTouchpad(){
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", ResourceManager.uiKnobBG());
        touchpadSkin.add("touchKnob", ResourceManager.uiKnob());
        touchpadStyle = new Touchpad.TouchpadStyle();
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchKnob.setMinWidth(touchKnob.getMinWidth()*scale);
        touchKnob.setMinHeight(touchKnob.getMinHeight()*scale);
        touchpad = new Touchpad(20, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);
        touchpad.setPosition(5f*scale,5f*scale);
        touchpad.setSize(touchpad.getWidth()*scale, touchpad.getHeight()*scale);

        return touchpad;
    }

    public ImageButton createCastSpellBtn(){
        castTexture = ResourceManager.uiCastSpellBtn();
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", castTexture);
        castBtnSkin.add("down", castTexture);
        castBtnSkin.add("over", castTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        castSpellBtn = new ImageButton(buttonStyle);
        castSpellBtn.setSize(castSpellBtn.getWidth()*scale, castSpellBtn.getHeight()*scale);
        castSpellBtn.setPosition(stage.getViewport().getCamera().viewportWidth - (45*scale + castSpellBtn.getWidth()), 40*scale);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Todo remove this when SpellPreparing screen is implemented
                if(playerInput.playerInk.currentInk > 0) {
                    playerInput.playerInk.currentInk--;
                    game.setScreen(new SpellSelectScreen(game, stage, playerInput));
                }else if(playerInput.playerHealth.currentHealth > 1){
                    playerInput.playerHealth.currentHealth--;
                    game.setScreen(new SpellSelectScreen(game, stage, playerInput));
                }

            }
        });

        castSpellBtnShadowTexture = ResourceManager.uiCastSpellShadow();
        castSpellBtnShadow = new Image(new TextureRegion(castSpellBtnShadowTexture));
        castSpellBtnShadow.setSize(castSpellBtnShadow.getWidth()*scale, castSpellBtnShadow.getHeight()*scale);
        castSpellBtnShadow.setPosition(stage.getViewport().getCamera().viewportWidth-(45*scale+castSpellBtn.getWidth()), 40*scale);
        //For animating Cast Spell button...
        minButtonHeight = castSpellBtn.getY();
        maxButtonHeight = castSpellBtn.getY()+10*scale;

        return castSpellBtn;
    }
}
