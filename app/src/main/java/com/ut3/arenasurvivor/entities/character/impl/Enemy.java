package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ut3.arenasurvivor.GameView;
import com.ut3.arenasurvivor.entities.character.Character;

public class Enemy extends Character {

    private GameView gameView;
    //Fire rate
    private int RELOAD_TIME = 100;
    private float reloadTimer;
    private long lastDrawNanoTime = -1;

    private long lastShotTime = -1;

    private int nbBullets;

    public Enemy(GameView gameView, Bitmap image, int x, int y) {
        super(image, 1, 1, x, y);
        this.gameView = gameView;
        this.reloadTimer = RELOAD_TIME;
        nbBullets = 5;
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = image;
        canvas.drawBitmap(bitmap, x, y, null);

        this.lastDrawNanoTime = System.nanoTime();
    }

    public void update() {
        Long now = System.nanoTime();
        int deltaTime = (int) ((now - lastShotTime) / 1000000);
        reloadTimer -= 0.5f * deltaTime;
        if (nbBullets > 0 && reloadTimer <= 0) {
            this.gameView.createProjectileAt(x + image.getWidth(), y + image.getHeight());
            nbBullets--;
            reloadTimer = RELOAD_TIME;
        } else if (nbBullets == 0) {
            this.gameView.destroyEnemy(this);
        }
        lastShotTime = System.nanoTime();
    }

    public void fire() {


    }

    public void spawn() {

    }

    public void despawn() {

    }


    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return false;
    }

}
