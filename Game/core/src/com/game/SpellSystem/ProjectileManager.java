package com.game.SpellSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

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

    public ProjectileManager(){
        projectiles = new LinkedList<Projectile>();
    }

    public void shootProjectile(Vector2 from, Vector2 to){
        Vector2 direction = to.sub(from);
        Projectile projectile = new Projectile(from.x, from.y, direction);
        projectiles.add(projectile);
        Gdx.app.log("MyTag", "my informative message");
    }

    public void update(){
        for(Projectile projectile : projectiles)
            projectile.update(Gdx.graphics.getDeltaTime());
    }


    public List<Projectile> getProjectiles(){
        return projectiles;
    }

}
