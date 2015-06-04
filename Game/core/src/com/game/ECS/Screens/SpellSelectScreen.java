package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;

import java.util.LinkedList;


/**
 * Created by Sean on 13/05/2015.
 */
public class SpellSelectScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    LinkedList<TextButton> buttons;

    public SpellSelectScreen(Main game, Stage stage, PlayerInputComponent playerInput) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        buttons = new LinkedList<TextButton>();
    }

        @Override
        public void show() {
            Skin skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

            TextButton frostBtn = createButton(skin, Gdx.graphics.getWidth()/2,
                    Gdx.graphics.getHeight()/2, "Frost Ball (Aimed)");
            frostBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new SpellAimingScreen(game, stage, playerInput, SpellComponent.Spell.FROST));
                }
            });
            buttons.add(frostBtn);

            TextButton gravBtn = createButton(skin, Gdx.graphics.getWidth()/2,
                    Gdx.graphics.getHeight()/2 - 50*scale, "Gravity Shift");
            gravBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playerInput.spellCast = SpellComponent.Spell.GRAVITY_SHIFT;
                    game.setScreen(new GameScreen(game, stage, playerInput));
                }
            });
            buttons.add(gravBtn);

            for(TextButton btn : buttons){
                stage.addActor(btn);
            }
        }

    @Override
    public void render(float delta) {

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
        for(TextButton btn : buttons){
            btn.remove();
        }
    }

    @Override
    public void dispose() {

    }


    private TextButton createButton(Skin skin, float x, float y, String text){
        TextButton btn = new TextButton(text, skin);
        btn.setPosition(x-btn.getWidth()/2, y-btn.getHeight()/2);
        return btn;
    }
}
