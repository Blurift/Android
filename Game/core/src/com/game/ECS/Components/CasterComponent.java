package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

/**
 * Created by Sean on 28/04/2015.
 *
 * Goes on entities that cast spells
 *
 */
public class CasterComponent extends Component {
    public Map<FacingComponent.Facing, Vector2> castPoint;//The offsets of the world position where spells are cast from
}
