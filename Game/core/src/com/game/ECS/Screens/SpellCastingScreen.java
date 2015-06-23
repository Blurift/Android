package com.game.ECS.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Storage.SpellPattern;
import com.game.ECS.Tools.ResolutionHandler;
import com.game.Main;
import com.game.ECS.Tools.SpellDrawing;

import java.util.LinkedList;


/**
 * Created by Keirron on 13/05/2015.
 *
 * Used for drawing spells patterns and casting matching spells.
 *
 */
public class SpellCastingScreen implements Screen {

    private Main game;
    private Stage stage;
    private PlayerInputComponent playerInput;

    private float scale;

    LinkedList<TextButton> buttons;

    private InputMultiplexer multiplexer = new InputMultiplexer();

    private SpellDrawing spellDrawing;

    private Array<SpellPattern> spells = new Array<SpellPattern>();

    private Texture castTexture;
    private ImageButton castSpellBtn;

    Vector2[] points;

    float buffer = Gdx.graphics.getHeight()/4;

    private ImageButton cancelSpellBtn;
    private Texture cancelTexture;

    //Line drawing
    private float LINE_SIZE = 10f; // TODO change on resolution

    int startPoint = -1;
    int endPoint = -1;
    Vector2 currentPoint = null;

    float maxX;
    float maxY;
    float minX;
    float minY;
    private float barSize;
    private Label spellListLbl;
    private Label adviceLbl;

    public SpellCastingScreen(Main game, final Stage stage, final PlayerInputComponent playerInput) {
        this.game = game;
        this.stage = stage;
        this.playerInput = playerInput;

        //Scale of UI
        this.scale = ResolutionHandler.getScale();

        buttons = new LinkedList<TextButton>();

        points = new Vector2[9];

        Vector2 start = new Vector2(Gdx.graphics.getWidth()/2, buffer*2);

        LINE_SIZE*=scale;

        points[0] = new Vector2(start).add(-buffer*0.7071f,buffer*0.7071f);
        points[1] = new Vector2(start).add(0,buffer);
        points[2] = new Vector2(start).add(buffer*0.7071f,buffer*0.7071f);
        points[3] = new Vector2(start).add(-buffer,0);
        points[4] = new Vector2(start);
        points[5] = new Vector2(start).add(buffer,0);
        points[6] = new Vector2(start).add(-buffer*0.7071f,-buffer*0.7071f);
        points[7] = new Vector2(start).add(0,-buffer);
        points[8] = new Vector2(start).add(buffer*0.7071f,-buffer*0.7071f);

        //Make frost spell
        SpellDrawing frostPattern = new SpellDrawing();
        frostPattern.addEdge(0,7);
        frostPattern.addEdge(7,2);
        frostPattern.addEdge(3,5);
        spells.add(new SpellPattern(SpellPattern.AimType.AIM, frostPattern,
                SpellComponent.Spell.FROST, "Frost Ball"));

        //Make gravity spell
        SpellDrawing gravityPattern = new SpellDrawing();
        gravityPattern.addEdge(0,2);
        gravityPattern.addEdge(3,5);
        gravityPattern.addEdge(6,8);
        spells.add(new SpellPattern(SpellPattern.AimType.ACCELEROMETER, gravityPattern,
                SpellComponent.Spell.GRAVITY_SHIFT, "Gravity Shift"));

        maxX = Gdx.graphics.getWidth()*0.75f;
        minX = Gdx.graphics.getWidth()*0.25f;
        maxY = Gdx.graphics.getHeight()*0.85f;
        minY = Gdx.graphics.getHeight()*0.15f;

        this.cancelSpellBtn = createCancelSpellBtn();
        this.castSpellBtn = createCastSpellBtn();

        spellDrawing = new SpellDrawing();
        //Set up drawing line input
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                setCurrentPoint(new Vector2(screenX,Gdx.graphics.getHeight()-screenY));
                adviceLbl.remove();
                spellListLbl.remove();
                startPoint = FindClosestPoint(currentPoint);


                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                endPoint = FindClosestPoint(currentPoint);
                if(startPoint!=endPoint) {
                    if (startPoint > -1 && endPoint > -1) {
                        spellDrawing.addEdge(startPoint, endPoint);
                        if (playerInput.playerInk.currentInk > 0)
                            playerInput.playerInk.currentInk -= 1;
                        else if (playerInput.playerHealth.currentHealth > 1)
                            playerInput.playerHealth.currentHealth -= 1;
                    }
                    spellCheck();
                }
                startPoint = -1;
                endPoint = -1;
                currentPoint = null;


                return true;
            }
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                setCurrentPoint(new Vector2(screenX,Gdx.graphics.getHeight()-screenY));

                //endPoint = FindClosestPoint(clickCoordinates);
                return true;
            }

            private void setCurrentPoint(Vector2 point){
                currentPoint = point;
                if(currentPoint.x > maxX)
                    currentPoint.x = maxX;
                if(currentPoint.x < minX)
                    currentPoint.x = minX;
                if(currentPoint.y > maxY)
                    currentPoint.y = maxY;
                if(currentPoint.y < minY)
                    currentPoint.y = minY;
            }
        });
        //Scale some things
        this.barSize = 300*scale;

        this.spellListLbl = createSpellListLabel();
        this.adviceLbl = createAdviceLabel();

        if (game.currentMusic != ResourceManager.CastSpellMusic()){
            if (game.currentMusic != null)
                game.currentMusic.stop();
            game.currentMusic = ResourceManager.CastSpellMusic();
            game.currentMusic.play();
            game.currentMusic.setLooping(true);
            game.currentMusic.setVolume(1f);
        }
    }

    @Override
    public void show() {

        playerInput.gameSpeed = 0.1f;
        this.stage.addActor(this.cancelSpellBtn);
        this.stage.addActor(this.castSpellBtn);
        stage.addActor(spellListLbl);
        stage.addActor(adviceLbl);
        Gdx.input.setInputProcessor(multiplexer);
    }

    //Sees if the player casted a spell pattern
    private void spellCheck(){
        if(spellDrawing.getEdges().size() == 3){
            for(SpellPattern spell : spells){
                if(spellDrawing.Compare(spell.getSpellDrawing())){
                    if(spell.getAimType() == SpellPattern.AimType.AIM){
                        game.setScreen(new SpellAimingScreen(game, stage, playerInput, spell.getSpellType()));
                    }else if(spell.getAimType() == SpellPattern.AimType.ACCELEROMETER) {
                        playerInput.spellCast = spell.getSpellType();
                        game.setScreen(new AccelerometerScreen(game, stage, playerInput, 7));
                    }
                    spellDrawing.clearEdges();
                    return;
                }
            }
            game.setScreen(new GameScreen(game, stage, playerInput));
            spellDrawing.clearEdges();
        }

    }
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void render(float delta) {

        shapeRenderer.setAutoShapeType(true);
;
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

        //Draw bars
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barOffset = 55*scale; //Offset from bottom
        Vector2 healthStart = new Vector2();
        Vector2 healthEnd = new Vector2();
        float barLength = 1;
        if(playerInput.playerInk.currentInk <= 0) {
            healthStart.x = stage.getViewport().getScreenWidth() * 0.5f - barSize * 0.5f;
            healthStart.y = barOffset;

            healthEnd.x = healthStart.x + barSize;
            healthEnd.y = barOffset;


            //Behind Health Bar
            shapeRenderer.setColor(Color.BLACK);
            //Draw fancier back bar

            barLength = 1*scale;
            for (int i = 15; i > 0; i = i - 3) {
                shapeRenderer.rectLine(new Vector2(healthStart.x - barLength, healthStart.y),
                        new Vector2(healthEnd.x + barLength, healthEnd.y), i*scale);
                barLength++;
            }
            //Actual Health Representation
            shapeRenderer.setColor(Color.RED);
            healthEnd.x = healthStart.x + barSize * (playerInput.playerHealth.currentHealth /
                    playerInput.playerHealth.maxHealth);
            shapeRenderer.rectLine(healthStart, healthEnd, 10*scale);
        }

        if(playerInput.playerInk.currentInk > 0) {
            //Draw InkBar
            barOffset = 35 * scale; //Offset from bottom


            //Actual Ink
            healthStart.x = stage.getViewport().getScreenWidth() * 0.5f - barSize * 0.5f;
            healthStart.y = barOffset;
            healthEnd.x = healthStart.x + barSize;
            healthEnd.y = barOffset;


            //Behind Ink Bar
            shapeRenderer.setColor(Color.BLACK);
            //Draw fancier back bar
            barLength = 1*scale;
            for (int i = 15; i > 0; i = i - 3) {
                shapeRenderer.rectLine(new Vector2(healthStart.x - barLength, healthStart.y),
                        new Vector2(healthEnd.x + barLength, healthEnd.y), i*scale);
                barLength++;
            }
            //Actual Health Representation
            shapeRenderer.setColor(Color.PURPLE);
            Vector2 healthMini = new Vector2(healthEnd);
            healthEnd.x = healthStart.x + barSize * (playerInput.playerInk.currentInk /
                    playerInput.playerInk.maxInk);
            healthMini.x = (healthStart.x + 5*scale) + (barSize - 10*scale) * (playerInput.playerInk.currentInk /
                    playerInput.playerInk.maxInk);
            shapeRenderer.rectLine(healthStart, healthEnd, 10*scale);

            shapeRenderer.setColor(Color.MAROON);
            shapeRenderer.rectLine(new Vector2(healthStart.x + 5*scale, healthStart.y - 1*scale),
                    new Vector2(healthMini.x, healthMini.y - 1*scale), 1*scale);
            //Border
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
        this.cancelSpellBtn.remove();
        this.castSpellBtn.remove();
        playerInput.gameSpeed = 1f;
        spellListLbl.remove();
        adviceLbl.remove();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        cancelTexture.dispose();
        castTexture.dispose();
    }


    private int FindClosestPoint(Vector2 p)
    {
        int closest = 0;
        float distance = 999999;

        for(int i = 0; i < 9; i++)
        {
            if(p !=null) {
                float d = Vector2.dst(p.x, p.y, points[i].x, points[i].y);
                if (d < distance) {
                    closest = i;
                    distance = d;
                }
            }
        }

        return closest;
    }

    public ImageButton createCancelSpellBtn(){
        cancelTexture = ResourceManager.uiCancelButton();
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", cancelTexture);
        castBtnSkin.add("down", cancelTexture);
        castBtnSkin.add("over", cancelTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        cancelSpellBtn = new ImageButton(buttonStyle);
        cancelSpellBtn.setSize(cancelSpellBtn.getWidth()*scale, cancelSpellBtn.getHeight()*scale);
        cancelSpellBtn.setPosition(stage.getViewport().getCamera().viewportWidth - (45*scale + cancelSpellBtn.getWidth()),
                stage.getViewport().getCamera().viewportHeight - (45*scale + cancelSpellBtn.getHeight()));
        cancelSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                game.setScreen(new GameScreen(game, stage, playerInput));
            }
        });

        return cancelSpellBtn;
    }

    public ImageButton createCastSpellBtn(){
        castTexture = ResourceManager.uiCastSpellBtn();
        Skin castBtnSkin = new Skin();
        castBtnSkin.add("up", castTexture);
        castBtnSkin.add("down", castTexture);
        castBtnSkin.add("over", castTexture);
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = castBtnSkin.getDrawable("up");
        buttonStyle.down = castBtnSkin.getDrawable("down");
        buttonStyle.over = castBtnSkin.getDrawable("over");
        castSpellBtn = new ImageButton(buttonStyle);
        castSpellBtn.setSize(castSpellBtn.getWidth()*scale, castSpellBtn.getHeight()*scale);
        castSpellBtn.setPosition(stage.getViewport().getCamera().viewportWidth - (45*scale + castSpellBtn.getWidth()), 40*scale);
        castSpellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.vibrate(75);
                game.setScreen(new SpellBookScreen(game, stage, playerInput, spells));
            }
        });

        return castSpellBtn;
    }

    private Label createSpellListLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("SpellList",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(4f*scale,4f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth - (120*scale + castSpellBtn.getWidth()),
                80*scale);
        return text;
    }

    private Label createAdviceLabel(){
        Label text;
        Label.LabelStyle textStyle;
        BitmapFont font = new BitmapFont();

        textStyle = new Label.LabelStyle();
        textStyle.font = font;

        text = new Label("Touch and drag to draw spell...",textStyle);
        text.setAlignment(Align.center);
        text.setFontScale(4f*scale,4f*scale);
        text.setPosition(stage.getViewport().getCamera().viewportWidth* 0.4f,
                stage.getViewport().getCamera().viewportHeight* 0.9f);
        return text;
    }
}
