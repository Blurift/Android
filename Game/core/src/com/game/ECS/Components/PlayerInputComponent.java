package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 27/04/2015.
 *
 * The inbetween of the UI and the Player.
 *
 * Used for controlling aspects of the game via the UI, and receiving aspects of the game to give
 * to the UI,
 *
 */
public class PlayerInputComponent extends Component {
    public static enum States{
        FREE, DRAWING, AIMING
    }


    public static enum GameState{
        Menu, Playing
    }

    public Vector2 touchpadDir = new Vector2(0, 0);
    public Vector2 screenPos; //Touched screen position converted to world cood
    public States currentState = States.FREE;
    public HealthComponent playerHealth;
    public InkComponent playerInk;
    public GameState gameState;

    public SpellComponent.Spell spellCast; //if null no spell is being cast
    public Vector2 spellDir;
    //TODO rework spellcast
}
