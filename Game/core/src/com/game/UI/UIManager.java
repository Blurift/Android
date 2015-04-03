package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.managers.GameManager;

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
    private SpellAiming spellAiming;
    private GameManager gm;

    private IUIScreen currentUIScreen;

    //Margin around screen
    private float margin = 0.10f;

    public UIManager(GameManager gm) { // TODO Screen width/height inputs to size the interace correctly
        this.gm = gm;

        //Set up Stage
        stage = new Stage();

        //Set up camera
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getViewport().setWorldSize(gm.getVIRTUAL_WIDTH(), gm.getVIRTUAL_HEIGHT());
        //Set UI Skins
        skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

        //Set the input as this stage
        Gdx.input.setInputProcessor(stage);

        //Create screens
        hud = new HUD(this, stage, skin);
        spellCasting = new SpellCasting(this, stage, skin, gm.getFilterManager());
        spellBook = new SpellBook(this, stage, skin);
        spellAiming = new SpellAiming(this, stage, skin);

        //Set default screen to HUD
        currentUIScreen = hud;
        hud.fill();

        //Set default screen

    }

    //Getters

    public GameManager getGameManager(){
        return gm;
    }

    public HUD getHUD(){
        return hud;
    }

    public SpellCasting getSpellCasting(){
        return spellCasting;
    }

    public SpellBook getSpellBook(){
        return spellBook;
    }

    public SpellAiming getSpellAiming(){
        return spellAiming;
    }


    public void activateUIScreen(IUIScreen toActivate){
        currentUIScreen.clear();
        currentUIScreen = toActivate;
        currentUIScreen.fill();
    }

    public void render(){
        currentUIScreen.render();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
        hud.dispose();
    }

}
