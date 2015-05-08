package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 8/05/2015.
 *
 * The component is casting a spell.
 *
 *
 */
public class SpellComponent extends Component {
    public static enum Spell{
        ICE_BALL, GRAVITY_SHIFT
    }

    public Vector2 spellDir = null; //If this si null, the spell isn't a projectile

}
