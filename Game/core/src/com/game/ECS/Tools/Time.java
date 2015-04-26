package com.game.ECS.Tools;

import com.badlogic.gdx.Gdx;

/**
 * Created by Sean on 25/04/2015.
 *
 * Handles the FPS of the game
 * //TODO Make this class handle Box2d physics time and syncing it to frame rate
 */
public class Time {
    public static double time = 1.0d; // Ratio of "idealized" framerate and actual framerate

    private static int defaultFPS = 60;

    public static void update(){
        int actualFPS = Gdx.graphics.getFramesPerSecond();
        actualFPS = (actualFPS == 0) ? 3000 : actualFPS;
        time = (double)defaultFPS / actualFPS;
    }

}
