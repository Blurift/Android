package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 27/04/2015.
 *
 * Contains where the moving object is facing
 *
 */
public class FacingComponent extends Component {
    public enum Facing{
        LEFT, RIGHT, UP, DOWN
    }

    public Facing facing = Facing.DOWN;

}
