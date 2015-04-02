package com.game.SpellSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 2/04/2015.
 *
 * A projectile that is being shot.
 *
 * Will need to use poolable
 *
 */
public class Projectile{

        private Vector2 position;
        private Texture texture;
        private Sprite projSprite;
        private Vector2 direction;

        private static final float PROJECTILE_SPEED = 1;

        /**
         * Bullet constructor. Just initialize variables.
         */
        public Projectile(float posX, float posY, Vector2 direction) {
            this.position = new Vector2(posX, posY);
            this.direction = direction;
            texture = new Texture(Gdx.files.internal("projectiles/fireball.png"));
            projSprite = new Sprite(texture);
            projSprite.setX(posX);
            projSprite.setY(posY);
        }

        /**
         * Method called each frame, which updates the bullet.
         */
        public void update (float delta) {

            // update bullet position
            position.add(direction.x*delta*PROJECTILE_SPEED, direction.y*delta*PROJECTILE_SPEED);
            projSprite.setPosition(position.x, position.y);
            Gdx.app.log("BulPos", position.toString());
        }

        public Sprite getSprite() {
            return projSprite;
        }

        public void dispose() {
                texture.dispose();
        }

}
