package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Sean on 26/04/2015.
 */
public class CameraComponent extends Component {
    public Entity target;
    public OrthographicCamera camera;
}
