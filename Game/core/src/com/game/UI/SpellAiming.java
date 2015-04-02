package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.managers.FilterManager;

/**
 * Created by Sean on 2/04/2015.
 *
 * For the player to choose where the want the spell to be cast
 *
 * TODO remove player ink
 *
 */
public class SpellAiming {
    private Stage stage;
    private UIManager ui;

    boolean isActive = false;

    private TextButton cancelBtn;

    private InputMultiplexer multiplexer = new InputMultiplexer();


    public SpellAiming(final UIManager ui, Stage stage, Skin skin) {
        this.stage = stage;
        this.ui = ui;

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
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                ui.getGameManager().getProjectileManager().shootProjectile(
                        ui.getGameManager().getPlayer().getPosition(),
                        new Vector2(screenX, 1080 - screenY));
                clear();
                ui.activateHUD();
                return true;
            }
        });

    }

    public void fill(){
        isActive = true;

        //Add buttons
        stage.addActor(cancelBtn);

        //Change input
        Gdx.input.setInputProcessor(multiplexer);
    }

    //Clears UIManager stage of spellcasting screen components
    public void clear(){
        isActive = false;
        cancelBtn.remove();
        Gdx.input.setInputProcessor(stage);
    }

}
