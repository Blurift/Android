package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 27/04/2015.
 *
 * Contains where the moving object is facing
 *
 */
public class FacingComponent extends Component {
    public static enum Facing{
        LEFT, RIGHT, UP, DOWN
    }

    public Facing facing = Facing.DOWN;
    public Vector2 dir = new Vector2();
}
