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
 * TODO use a pool to handle all projectiles
 *
 */
public class ProjectileManager {
    private List<Projectile> projectiles;
    private GameManager gm;
    public ProjectileManager(GameManager gm){
        projectiles = new LinkedList<Projectile>();
        this.gm = gm;
    }

    public void shootProjectile(Vector2 from, Vector2 direction){
        Projectile projectile = new Projectile(gm, from.x, from.y, direction);
        projectiles.add(projectile);
    }

    public void update(){
        for(Projectile projectile : projectiles)
            projectile.update(Gdx.graphics.getDeltaTime());
    }


    public List<Projectile> getProjectiles(){
        return projectiles;
    }

    public void removeProjectile(Projectile proj){
        projectiles.remove(proj);
    }
}
