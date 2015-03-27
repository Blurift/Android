package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Keirron on 22/03/2015.
 */
public class UIManager{

    private Stage stage;

    private Skin skin;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private TextButton castSpellBtn;

    public UIManager(){ // TODO Screen width/height inputs to size the interace correctly

        //Set up Stage
        stage = new Stage();

        //Set up camera
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        //Set UI Skins
        skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

        //Cast Spell Button
        castSpellBtn = new TextButton("Cast Spell",skin);
        castSpellBtn.setPosition(Gdx.graphics.getWidth()-200, 100);
        castSpellBtn.setScale(5, 5);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO direct to spellcasting class

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

        //Add components to stage
        stage.addActor(castSpellBtn);
        stage.addActor(touchpad);

        //Set the input as this stage
        Gdx.input.setInputProcessor(stage);
    }

    public float getKnobPercentY(){
        return touchpad.getKnobPercentY();
    }

    public float getKnobPercentX(){
        return touchpad.getKnobPercentX();
    }

    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
        touchpadSkin.dispose();
    }

}
