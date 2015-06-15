package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;
import com.game.SpellSystem.SpellDrawing;

import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by Sean on 13/05/2015.
 */
public class SpellCastingScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    LinkedList<TextButton> buttons;

    private InputMultiplexer multiplexer = new InputMultiplexer();

    private SpellDrawing spellDrawing;

    private SpellDrawing spellFire;
    private SpellDrawing spellWater;
    private SpellDrawing spellWind;

    Vector2[] points;

    float buffer = Gdx.graphics.getHeight()/4;



    //Line drawing
    private float LINE_SIZE = 10f; // TODO change on resolution

    int startPoint = -1;
    int endPoint = -1;
    Vector2 currentPoint = null;

    public SpellCastingScreen(Main game, final Stage stage, PlayerInputComponent playerInput) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        buttons = new LinkedList<TextButton>();

        points = new Vector2[9];

        Vector2 start = new Vector2(Gdx.graphics.getWidth()/2, buffer*2);


        points[0] = new Vector2(start).add(-buffer*0.7071f,buffer*0.7071f);
        points[1] = new Vector2(start).add(0,buffer);
        points[2] = new Vector2(start).add(buffer*0.7071f,buffer*0.7071f);
        points[3] = new Vector2(start).add(-buffer,0);
        points[4] = new Vector2(start);
        points[5] = new Vector2(start).add(buffer,0);
        points[6] = new Vector2(start).add(-buffer*0.7071f,-buffer*0.7071f);
        points[7] = new Vector2(start).add(0,-buffer);
        points[8] = new Vector2(start).add(buffer*0.7071f,-buffer*0.7071f);

        //Make Spells
        spellFire = new SpellDrawing();
        spellFire.addEdge(0,7);
        spellFire.addEdge(7,2);
        spellFire.addEdge(3,5);

        spellWater = new SpellDrawing();
        spellWater.addEdge(0,2);
        spellWater.addEdge(3,5);
        spellWater.addEdge(6,8);

        spellWind = new SpellDrawing();
        spellWind.addEdge(1,7);
        spellWind.addEdge(3,5);
        spellWind.addEdge(6,8);

        spellDrawing = new SpellDrawing();
        //Set up drawing line input
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                Vector2 clickCoordinates = new Vector2(screenX,Gdx.graphics.getHeight()-screenY);

                startPoint = FindClosestPoint(clickCoordinates);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                endPoint = FindClosestPoint(currentPoint);

                if(startPoint > -1 && endPoint > -1)
                    spellDrawing.addEdge(startPoint, endPoint);

                startPoint = -1;
                endPoint = -1;
                currentPoint = null;

                spellCheck();
                return true;
            }
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                currentPoint = new Vector2(screenX,Gdx.graphics.getHeight()-screenY);
                //endPoint = FindClosestPoint(clickCoordinates);
                return true;
            }
        });
    }

    @Override
    public void show() {
        /*Skin skin = new Skin(Gdx.files.internal("UI/uiskin.json"));

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
        }*/
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void spellCheck(){
        if(spellDrawing.getEdges().size() == 3){

            if(spellDrawing.Compare(spellFire))
            { game.setScreen(new SpellAimingScreen(game, stage, playerInput, SpellComponent.Spell.FROST)); }
            if(spellDrawing.Compare(spellWater))
            { game.setScreen(new SpellAimingScreen(game, stage, playerInput, SpellComponent.Spell.FROST)); }
            if(spellDrawing.Compare(spellWind))
            { game.setScreen(new SpellAimingScreen(game, stage, playerInput, SpellComponent.Spell.FROST)); }

            spellDrawing.clearEdges();
            //ui.activateUIScreen(ui.getSpellAiming());
        }

    }

    @Override
    public void render(float delta) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);







        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(Color.RED));
        shapeRenderer.circle(points[0].x,points[0].y, buffer* 0.2f);
        shapeRenderer.circle(points[1].x,points[1].y, buffer* 0.2f);
        shapeRenderer.circle(points[2].x,points[2].y, buffer* 0.2f);
        shapeRenderer.circle(points[3].x,points[3].y, buffer* 0.2f);
        shapeRenderer.circle(points[4].x,points[4].y, buffer* 0.2f);
        shapeRenderer.circle(points[5].x,points[5].y, buffer* 0.2f);
        shapeRenderer.circle(points[6].x,points[6].y, buffer* 0.2f);
        shapeRenderer.circle(points[7].x,points[7].y, buffer* 0.2f);
        shapeRenderer.circle(points[8].x,points[8].y, buffer* 0.2f);
        //shapeRenderer.end();

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(255, 255, 255, 1));
        for (SpellDrawing.Edge edge : spellDrawing.getEdges()) {
            shapeRenderer.rectLine(points[edge.p1], points[edge.p2], LINE_SIZE);
        }
        if (startPoint >-1 && currentPoint != null) {
            shapeRenderer.rectLine(points[startPoint], currentPoint, LINE_SIZE);
        }
        shapeRenderer.end();

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
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {

    }


    private TextButton createButton(Skin skin, float x, float y, String text){
        TextButton btn = new TextButton(text, skin);
        btn.setPosition(x-btn.getWidth()/2, y-btn.getHeight()/2);
        return btn;
    }

    private int FindClosestPoint(Vector2 p)
    {
        int closest = 0;
        float distance = 999999;

        for(int i = 0; i < 9; i++)
        {
            float d = Vector2.dst(p.x,p.y,points[i].x,points[i].y);
            if(d < distance)
            {
                closest = i;
                distance = d;
            }
        }

        return closest;
    }
}
