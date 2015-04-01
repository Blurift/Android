package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.managers.FilterManager;

/**
 * Created by Sean on 1/04/2015.
 */
public class SpellCasting{
    private Stage stage;
    private FilterManager filterManager;
    private UIManager ui;

    boolean isActive = false;

    private TextButton cancelBtn;
    private TextButton spellBookBtn;

    private InputMultiplexer multiplexer = new InputMultiplexer();

    private SpellDrawing spellDrawing;

    //Line drawing
    private Vector2 lineStart; //Start of a line in the middle of being drawn
    private Vector2 lineEnd; //If the player has started drawing, this is where their finger is


    public SpellCasting(final UIManager ui, Stage stage, Skin skin, FilterManager filterManager){
        this.stage = stage;
        this.filterManager = filterManager;
        this.ui = ui;
        //Cancel Button
        cancelBtn = new TextButton("Cancel SpellCasting", skin);
        cancelBtn.setPosition(Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight()-100);
        cancelBtn.setScale(5, 5);
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();
                spellDrawing.clearEdges();
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

        spellDrawing = new SpellDrawing();
        //Set up drawing line input
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                    lineStart = new Vector2(x, 1080 - y);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                spellDrawing.addEdge(lineStart, new Vector2(screenX, 1080 - screenY));
                    lineStart = null;
                    lineEnd = null;
                spellCheck();
                return true;
            }
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                    lineEnd = new Vector2(screenX, 1080 - screenY);
                return true;
            }
        });


    }

    private void spellCheck(){
        if(spellDrawing.getEdges().size() == 3){
            clear();
            spellDrawing.clearEdges();
            ui.activateHUD();
        }

    }

    //Fills UIManager stage with spellcasting screen components
    public void fill(){
        isActive = true;

        //Add buttons
        stage.addActor(cancelBtn);
        stage.addActor(spellBookBtn);

        //Set filter
        filterManager.setColour(new Color(0, 0, 0, .6f));

        //Change input
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void render(){
        if(isActive) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();

            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(255, 255, 255, 1));
            for (SpellDrawing.Edge edge : spellDrawing.getEdges()) {
                shapeRenderer.rectLine(edge.p1, edge.p2, 5f);
            }
            if (lineStart != null && lineEnd != null) {
                shapeRenderer.rectLine(lineStart, lineEnd, 5f);
//
            }
            shapeRenderer.end();
        }
    }

    //Clears UIManager stage of spellcasting screen components
    public void clear(){
        isActive = false;
        cancelBtn.remove();
        spellBookBtn.remove();
        filterManager.clear();
        Gdx.input.setInputProcessor(stage);
    }

}
