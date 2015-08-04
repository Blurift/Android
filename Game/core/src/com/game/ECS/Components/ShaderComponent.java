package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sean on 3/08/2015.
 *
 * Shaders for sprites. Used in Render system.
 *
 */
public class ShaderComponent extends Component {
    public ShaderProgram shaderProgram;
    public Array<String> uniforms = new Array<String>();
}
