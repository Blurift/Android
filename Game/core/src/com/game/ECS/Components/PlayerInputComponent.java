package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 27/04/2015.
 */
public class PlayerInputComponent extends Component {
    public enum States{
        FREE, DRAWING, AIMING
    }

    public Vector2 touchpadDir = new Vector2(0, 0);
    public Vector2 screenPos; //Touched screen position converted to world cood
    public States currentState = States.FREE;

    public Vector2 spellCast; //if null no spell is being cast, otherwise direction of spell
    //TODO rework spellcast
}
