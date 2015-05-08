package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;


/**
 * Created by Sean on 1/05/2015.
 */
public class DamageComponent extends Component {
    public float damage = 0;

    //Damage animation related
    public int flashes = 0; //How many flashes the animation has done
    public float animTimer = 0; //Time til next flash
    public Color originalColor; //Store original color of sprite

    public DamageComponent(){}
    public DamageComponent(float amt){ this.damage = amt; }
}
