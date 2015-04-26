package com.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.controllers.CharacterController;
import com.game.managers.FilterManager;

/**
 * Created by Sean on 2/04/2015.
 *
 * For the player to choose where the want the spell to be cast
 *
 * TODO remove player ink
 *
 */
public class SpellAiming implements IUIScreen{
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
                ui.activateUIScreen(ui.getHUD());
            }
        });
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            private float camModifierX;
            private float camModifierY;

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                //Get screen coordinates and convert to world coordinates
                Vector3 screenLoc = new Vector3(screenX, screenY, 0);
                Vector3 unproject = ui.getGameManager().getCamera().unproject(screenLoc);
                Vector2 to = new Vector2(unproject.x, unproject.y);
                Vector2 direction = (to.sub(ui.getGameManager().getPlayer().getCastPoint())).nor();

                //Player faces direction touching
                ui.getGameManager().getPlayer().setFacing(direction.x, direction.y);

                //Shoot at that direction
                ui.getGameManager().getProjectileManager().shootProjectile(
                        ui.getGameManager().getPlayer().getCastPoint(),
                        direction);
                ui.activateUIScreen(ui.getHUD());
                ui.getGameManager().getPlayer().isCamLocked = false;

                //Give cam position to player, so it slowly centres to the player again
                //ui.getGameManager().getPlayer().setCamModifierX(camModifierX);
                //ui.getGameManager().getPlayer().setCamModifierY(camModifierY);
                //TODO fix centre back to player after spell casts
                //ui.getGameManager().getPlayer().setCamBackSpeed(new Vector2(15f, 15f));
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                CharacterController player = ui.getGameManager().getPlayer();

                //Get screen coordinates and convert to world coordinates
                Vector3 screenLoc = new Vector3(screenX, screenY, 0);
                Vector3 unproject = ui.getGameManager().getCamera().unproject(screenLoc);
                Vector2 to = new Vector2(unproject.x, unproject.y);
                Vector2 distance = new Vector2(to).sub(ui.getGameManager().getPlayer().getCastPoint());
                Vector2 direction = new Vector2(distance).nor();

                //Player faces direction touching (rounded to make smoother transition)
                ui.getGameManager().getPlayer().setFacing(Math.round(direction.x), Math.round(direction.y));

                //Focus cam on mid point of distance
                Vector2 camFocus = new Vector2();
                Vector2 camCentre = new Vector2(to).sub(new Vector2(
                        player.getSprite().getX() + (player.getSprite().getWidth() / 2f),
                        player.getSprite().getY() + (player.getSprite().getHeight() / 2f)
                ));
                camFocus.x = player.getCastPoint().x + (distance.x * 0.5f);
                camFocus.y = player.getCastPoint().y + (distance.y * 0.5f);
                ui.getGameManager().getPlayer().isCamLocked = true;
                Camera camera = ui.getGameManager().getCamera();
                float defaultCamX = player.getSprite().getX() + (player.getSprite().getWidth() / 2);
                float defaultCamY = player.getSprite().getY() + (player.getSprite().getHeight() / 2);
                float camSpeed = 5f * Gdx.graphics.getDeltaTime();
                float maxX = camFocus.x;
                float maxY = camFocus.y;
                camModifierX = camCentre.x * 0.5f;
                camModifierY = camCentre.y * 0.5f;

                camera.position.set(defaultCamX + camCentre.x * 0.5f, defaultCamY + camCentre.y * 0.5f, 0);
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

    public void render() {

    }

}
