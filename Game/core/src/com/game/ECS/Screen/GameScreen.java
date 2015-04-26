package com.game.ECS.Screen;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Other.Assets;
import com.game.ECS.Other.ResolutionHandler;

/**
 * Created by Sean on 26/04/2015.
 *
 * Screen containing the HUD for the game.
 *
 */
public class GameScreen implements Screen {

    private Stage stage;
    private Entity player;

    float scale;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    Texture castTexture;
    private float minButtonHeight;
    private float maxButtonHeight;
    private ImageButton castSpellBtn;
    private Image castSpellBtnShadow;
    private Texture castSpellBtnShadowTexture;

    public GameScreen(Stage stage, Entity player){
        this.stage = stage;
        this.player = player;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        //Screen components
        this.touchpad = createTouchpad();
        this.castSpellBtn = createCastSpellBtn();

        //Set the input as this stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.addActor(touchpad);
        stage.addActor(castSpellBtn);
        stage.addActor(castSpellBtnShadow);
    }

    //For the animation of the cast spell button
    private boolean isRising = true;
    private float moveSpeed = 10;

    @Override
    public void render(float delta) {
        //Feed touchpad input to player entity
        VelocityComponent vel = player.getComponent(VelocityComponent.class);
        vel.velocity.x = touchpad.getKnobPercentX();
        vel.velocity.y = touchpad.getKnobPercentY();

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
    }

    @Override
    public void dispose() {
        touchpadSkin.dispose();
        castTexture.dispose();
        castSpellBtnShadowTexture.dispose();
    }

    public Touchpad createTouchpad(){
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("UI/touchpad/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("UI/touchpad/touchKnob.png"));
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
        castTexture = new Texture(Assets.castSpellBtn);
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
                //ui.activateUIScreen(ui.getSpellCasting());
            }
        });

        castSpellBtnShadowTexture = new Texture(Assets.castSpellShadow);
        castSpellBtnShadow = new Image(new TextureRegion(castSpellBtnShadowTexture));
        castSpellBtnShadow.setSize(castSpellBtnShadow.getWidth()*scale, castSpellBtnShadow.getHeight()*scale);
        castSpellBtnShadow.setPosition(stage.getViewport().getCamera().viewportWidth-(45*scale+castSpellBtn.getWidth()), 40*scale);
        //For animating Cast Spell button...
        minButtonHeight = castSpellBtn.getY();
        maxButtonHeight = castSpellBtn.getY()+10*scale;

        return castSpellBtn;
    }
}
