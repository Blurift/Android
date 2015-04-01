package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
public class HUD{

    private Stage stage;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private TextButton castSpellBtn;

    public HUD(final UIManager ui, Stage stage, Skin skin){
        this.stage = stage;


        castSpellBtn = new TextButton("SpellCasting", skin);
        castSpellBtn.setPosition(Gdx.graphics.getWidth()-200, 100);
        castSpellBtn.setScale(5, 5);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();
                ui.activateSpellCasting();
            }
        });

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
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);
        touchpad.setPosition(100,100);



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

    public void dispose(){
        touchpadSkin.dispose();
    }
}