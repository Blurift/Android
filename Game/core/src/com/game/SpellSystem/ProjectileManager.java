package com.game.SpellSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.managers.GameManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sean on 2/04/2015.
 *
 * Handles projectiles within the game
 *
 */
public class ProjectileManager {
    private List<Projectile> projectiles;
    private GameManager gm;
    public ProjectileManager(GameManager gm){
        projectiles = new LinkedList<Projectile>();
        this.gm = gm;
    }

    public void shootProjectile(Vector2 from, Vector3 screenLoc){
        Vector3 unproject = gm.getCamera().unproject(screenLoc);
        Vector2 to = new Vector2(unproject.x, unproject.y);
        Vector2 direction = to.sub(from);
        Projectile projectile = new Projectile(from.x, from.y, direction.nor());
        projectiles.add(projectile);
    }

    public void update(){
        for(Projectile projectile : projectiles)
            projectile.update(Gdx.graphics.getDeltaTime());
    }


    public List<Projectile> getProjectiles(){
        return projectiles;
    }

}
