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
public class SpellBook implements IUIScreen{
    private Stage stage;

    private TextButton cancelBtn;

    public SpellBook(final UIManager ui, Stage stage, Skin skin){
        this.stage = stage;

        //Cancel Button
        cancelBtn = new TextButton("Back", skin);
        cancelBtn.setPosition(Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight()-100);
        cancelBtn.setScale(5, 5);
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ui.activateUIScreen(ui.getSpellCasting());
            }
        });


    }

    public void fill(){
        stage.addActor(cancelBtn);
    }

    public void clear(){
        cancelBtn.remove();
    }

    public void render(){};

}
