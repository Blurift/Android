package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.ECS.Other.Assets;

/**
 * Created by Sean on 25/04/2015.
 *
 * A sprite that will get rendered with in the gameworld. It takes texture regions so it can
 * grab different animation frames. It's centred on the Position component
 *
 *
 */
public class SpriteComponent extends Component{
    public Sprite sprite = new Sprite(new Texture(Assets.blank));

    public SpriteComponent(){}
    public SpriteComponent(Sprite sprite){
        this.sprite = sprite;
    }
}
