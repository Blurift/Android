package com.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.game.MyContactListener;
import com.game.SpellSystem.Projectile;
import com.game.SpellSystem.ProjectileManager;
import com.game.UI.UIManager;
import com.game.controllers.AIController;
import com.game.controllers.CharacterController;
import com.game.screenManager.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keirron on 22/03/2015.
 */
public class GameManager extends Screen {
    private OrthographicCamera camera;
    private float VIRTUAL_HEIGHT = 8; //11
    private float VIRTUAL_WIDTH;
    private CharacterController mainPlayer;
    private MapManager mapManager;
    private UIManager uiManager;
    private FilterManager filterManager;
    private ProjectileManager projectileManager;
    private AIManager aiManager;

    //Box2d
    World world;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    public static final float PIXELS_TO_METRES = 32f;
    public static final float DEFAULT_GAMESPEED = 1f;
    private float gameSpeed = DEFAULT_GAMESPEED;

    //List of bodies to destory after world step
    private List<Body> bodiesToDestroy;

    public GameManager()
    {

        //Get virtual dimensions of game
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        VIRTUAL_WIDTH = VIRTUAL_HEIGHT * width / height;

        //Set up game camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        camera.update();

        //Box2d set up
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new MyContactListener(this));
        debugRenderer = new Box2DDebugRenderer();

        bodiesToDestroy = new ArrayList<Body>();

        //Create Main Player
        mainPlayer = new CharacterController(this, "character/druid_sheet.png" , new Vector2(5, 5));

        //Intantiate Managers
        mapManager = new MapManager(this, camera, "map/WorldMap.tmx");
        filterManager = new FilterManager();
        uiManager = new UIManager(this);
        projectileManager = new ProjectileManager(this);
        aiManager = new AIManager(this);

        //Set player at start spawn
        mainPlayer.setPosition(mapManager.getPlayerSpawn());
    }

        private double accumulator;
        private double currentTime;
        private float step = 1.0f / 60.0f;
    public void update(){
        float delta = Gdx.graphics.getDeltaTime() * gameSpeed;
        //Organize objects to render
        mapManager.clearObjects();
        mapManager.addObject(mainPlayer);
        for(Projectile projectile : projectileManager.getProjectiles()){
            mapManager.addObject(projectile);
        }
        for(AIController ai : aiManager.getEnemies()){
            ai.update();
            mapManager.addObject(ai);
        }
        projectileManager.update();

        //Update player velocity with UI inputs
        mainPlayer.updateVelocity(uiManager.getHUD().getKnobPercentX(), uiManager.getHUD().getKnobPercentY());

        //Update player
        mainPlayer.update(delta);

        //Update physics
        world.step(1f/60f, 6, 2);


        //Destroy physics objects queued for destruction
        for(Body body : bodiesToDestroy){
            world.destroyBody(body);
        }
        bodiesToDestroy.clear();

        //Update camera
        camera.update();

        //Update Map (Needs camera updated first
        mapManager.update();

        //Setup Box2D physics
        debugMatrix = mapManager.getSpriteBatch().getProjectionMatrix().cpy().scale(PIXELS_TO_METRES,
                PIXELS_TO_METRES, 0);
    }

    @Override
    public void render()
    {

        Gdx.gl.glClearColor(140,231,140,1);
        Gdx.gl.glClear((GL20.GL_COLOR_BUFFER_BIT));
        //Render Map
        mapManager.render();

        //Render Filters
        filterManager.render();

        //Render UI
        uiManager.render();

        //Render Collisions Box2D objects
       debugRenderer.render(world, debugMatrix);

    }

    @Override
    public void resize()
    {
        //TODO fix this
        camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        uiManager.getCamera().setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    /**
     * Getters
     */
    public ProjectileManager getProjectileManager(){
        return projectileManager;
    }

    public FilterManager getFilterManager(){
        return filterManager;
    }

    public MapManager getMapManager() { return mapManager; }

    public CharacterController getPlayer(){
        return mainPlayer;
    }

    public OrthographicCamera getCamera(){
        return camera;
    }

    public float getVIRTUAL_HEIGHT(){
        return VIRTUAL_HEIGHT;
    }

    public float getVIRTUAL_WIDTH(){
        return VIRTUAL_WIDTH;
    }

    public float getGameSpeed(){
        return gameSpeed;
    }

    public World getWorld(){
        return world;
    }

    /**
     * Setters
     */

    public void addBodyToDestroy(Body body){
        bodiesToDestroy.add(body);
    }

    @Override
    public void dispose()
    {
        mainPlayer.dispose();
        uiManager.dispose();
        mapManager.dispose();
    }
}
