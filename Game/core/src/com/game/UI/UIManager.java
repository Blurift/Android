package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Keirron on 22/03/2015.
 *
 * Manages what screen the UI is controlling controlling, and what the user is current doing on the
 * game screen.
 *
 * It sends co-ordinates of where the user is touching to the screens.
 *
 */
public class UIManager{

    private Stage stage;

    private Skin skin;

    //Screens
    private HUD hud;
    private SpellCasting spellCasting;
    private SpellBook spellBook;

    public UIManager() { // TODO Screen width/height inputs to size the interace correctly

        //Set up Stage
        stage = new Stage();

        //Set up camera
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        //Set UI Skins
        skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

        //Set the input as this stage
        Gdx.input.setInputProcessor(stage);

        //Create screens
        hud = new HUD(this, stage, skin);
        spellCasting = new SpellCasting(this, stage, skin);
        spellBook = new SpellBook(this, stage, skin);

        //Set default to HUD
        hud.fill();

        //Set default screen

    }

    //Actiate sets active screen to revelent screen
    public void activateSpellCasting(){
        spellCasting.fill();
    }

    public void activateHUD(){
        hud.fill();
    }

    public void activateSpellBook(){
        spellBook.fill();
    }

    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
        hud.dispose();
    }

}
