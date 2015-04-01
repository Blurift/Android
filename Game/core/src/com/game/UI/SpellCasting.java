package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Sean on 1/04/2015.
 */
public class SpellCasting{
    private Stage stage;

    private TextButton cancelBtn;
    private TextButton spellBookBtn;

    public SpellCasting(final UIManager ui, Stage stage, Skin skin){
        this.stage = stage;
        //Cancel Button
        cancelBtn = new TextButton("Cancel SpellCasting", skin);
        cancelBtn.setPosition(Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight()-100);
        cancelBtn.setScale(5, 5);
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();
                ui.activateHUD();
            }
        });

        //SpellBook Button
        spellBookBtn = new TextButton("See Spells", skin);
        spellBookBtn.setPosition(Gdx.graphics.getWidth()-200, 100);
        spellBookBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();
                ui.activateSpellBook();
            }
        });

    }

    //Fills UIManager stage with spellcasting screen components
    public void fill(){
        stage.addActor(cancelBtn);
        stage.addActor(spellBookBtn);
    }

    //Clears UIManager stage of spellcasting screen components
    public void clear(){
        cancelBtn.remove();
        spellBookBtn.remove();
    }

}
