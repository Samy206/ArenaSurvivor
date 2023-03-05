package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ut3.arenasurvivor.GameView;
import com.ut3.arenasurvivor.entities.character.Character;

public class Enemy extends Character {

    private GameView gameView;
    /*---Fire rate Attr---*/
    //Base value of reload Time
    private int RELOAD_TIME = 100;
    //Attr to store current timer of reload
    private float reloadTimer;
    //Firerate of enemy
    private float fireRate;

    private long lastShotTime = -1;

    private int nbBullets;

    public Enemy(GameView gameView, Bitmap image, int x, int y) {
        super(image, 1, 1, x, y);
        this.gameView = gameView;
        //Set firing attr
        this.reloadTimer = RELOAD_TIME;
        nbBullets = 5;
        fireRate = 0.02f;
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = image;
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update() {
        //Update the reloadTime
        Long now = System.nanoTime();
        int deltaTime = (int) ((now - lastShotTime) / 1000000);
        reloadTimer -= fireRate * deltaTime;
        //If fire is ready and they're still bullets
        if (nbBullets > 0 && reloadTimer <= 0) {
            //Fire
            this.gameView.createProjectileAt(x + image.getWidth(), y + image.getHeight());
            nbBullets--;
            //Reset the time
            reloadTimer = RELOAD_TIME;
        } else if (nbBullets == 0) {
            //Kill this entity
            this.gameView.destroyEnemy(this);
        }
        lastShotTime = System.nanoTime();
    }

    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return false;
    }

}
