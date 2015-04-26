package com.game.ECS.Other;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.ECS.Components.StateComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean on 25/04/2015.
 *
 * Asset locations
 *
 */
public class Assets {

    //Tiled Map
    public static String tiledMap = "map/WorldMap.tmx";

    //UI
    public static String castSpellBtn = "UI/castButton.png";
    public static String castSpellShadow = "UI/castButtonShadow.png";

    //Character Sheets
    public static String druidSheet = "character/DruidSheet.png";

    //Misc
    public static String blank = "Blank.png";

    //Animations
    //TODO add reverse animate feature
    private static Animation animate(Texture sheet, int startX, int startY, int width, int height, int length, float speed){
        TextureRegion[] frames = new TextureRegion[length];
        for(int i=0;i < frames.length; i++)
            frames[i] = new TextureRegion(sheet, startX + (width*i), startY, width, height);

        Animation animated = new Animation(speed, frames);
        return animated;
    }
    public static class DirAnimation{
        public Map<StateComponent.State, Animation> left, right, up, down;

        public DirAnimation(Map<StateComponent.State, Animation> left,
                            Map<StateComponent.State, Animation> right,
                            Map<StateComponent.State, Animation> up,
                            Map<StateComponent.State, Animation> down){
            this.left = left;
            this.right = right;
            this.up = up;
            this.down = down;
        }
    }

    //Player Druid
    public static DirAnimation animPlayerDruid(){
        Map<StateComponent.State, Animation> left = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> right = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> up = new HashMap<StateComponent.State, Animation>();
        Map<StateComponent.State, Animation> down = new HashMap<StateComponent.State, Animation>();
        Texture sheet = new Texture(druidSheet);
        float s = 0.15f; //Anim speed

        //Still
        left.put(StateComponent.State.STILL, animate(sheet, 64, 192, 64, 64, 1, s));
        right.put(StateComponent.State.STILL, animate(sheet, 64, 128, 64, 64, 1, s));
        up.put(StateComponent.State.STILL, animate(sheet, 64, 64, 64, 64, 1, s));
        down.put(StateComponent.State.STILL, animate(sheet, 64, 0, 64, 64, 1, s));

        //Walking
        left.put(StateComponent.State.WALK, animate(sheet, 64, 192, 64, 64, 1, s));
        right.put(StateComponent.State.WALK, animate(sheet, 64, 128, 64, 64, 1, s));
        up.put(StateComponent.State.WALK, animate(sheet, 64, 64, 64, 64, 1, s));
        down.put(StateComponent.State.WALK, animate(sheet, 64, 0, 64, 64, 4, s));

        return new DirAnimation(left, right, up, down);
    };



}
