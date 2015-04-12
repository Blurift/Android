package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.game.managers.FilterManager;

/**
 * Created by Sean on 1/04/2015.
 *
 * Allows moving the player through a touch pad, going to options menu and going to casting menu.
 *
 * TODO Needs to give the PLAYER CLASS knob coordinates
 *
 */
public class HUD implements IUIScreen{

    private Stage stage;
    private UIManager ui;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    Texture castTexture;
    private float minButtonHeight;
    private float maxButtonHeight;
    private ImageButton castSpellBtn;
    private SpriteBatch sb;
    private Sprite shadow;
    private Texture shadowTexture;

    public HUD(final UIManager ui, Stage stage, Skin skin){
        this.stage = stage;
        this.ui = ui;
        sb = new SpriteBatch();
        castTexture = new Texture("UI/castButton.png");
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", castTexture);
        castBtnSkin.add("down", castTexture);
        castBtnSkin.add("over", castTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        castSpellBtn = new ImageButton(buttonStyle);
        castSpellBtn.setScale(5, 5);
        castSpellBtn.setPosition(Gdx.graphics.getWidth()-(45+castSpellBtn.getWidth()), 40);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ui.activateUIScreen(ui.getSpellCasting());
            }
        });
        shadowTexture = new Texture("UI/castButtonShadow.png");
        shadow = new Sprite(shadowTexture);
        shadow.setPosition(Gdx.graphics.getWidth()-(45+castSpellBtn.getWidth()), 40);

        //Touchpad
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("UI/touchpad/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("UI/touchpad/touchKnob.png"));
        touchpadStyle = new Touchpad.TouchpadStyle();
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchpad = new Touchpad(20, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);
        //touchpad.setSize(5f,5f);
        touchpad.setPosition(5f,5f);

        minButtonHeight = castSpellBtn.getY();
        maxButtonHeight = castSpellBtn.getY()+10;


        //Set the input as this stage
        Gdx.input.setInputProcessor(stage);
    }

    public float getKnobPercentY(){
        return touchpad.getKnobPercentY();
    }

    public float getKnobPercentX(){
        return touchpad.getKnobPercentX();
    }


    //Fills UIMananger stage with hud compnents
    public void fill(){
        stage.addActor(castSpellBtn);
        stage.addActor(touchpad);

    }

    //CLears UIManager stage of hud components
    public void clear(){
        touchpad.remove();
        castSpellBtn.remove();

    }


    private boolean isRising = true;
    private float moveSpeed = 10;

    public void render(){
        sb.setProjectionMatrix(stage.getViewport().getCamera().combined);
        sb.begin();
            shadow.draw(sb);
        sb.end();
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

    };

    private float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }

    public void dispose(){
        touchpadSkin.dispose();
        castTexture.dispose();
        shadowTexture.dispose();
        sb.dispose();
    }
}