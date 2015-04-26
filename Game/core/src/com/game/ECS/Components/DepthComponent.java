package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 25/04/2015.
 *
 * If this component is rendered with depth at a certain Y scale
 *
 */
public class DepthComponent extends Component {
    public float y;

    public DepthComponent(float y){
        this.y = y;
    }
}
