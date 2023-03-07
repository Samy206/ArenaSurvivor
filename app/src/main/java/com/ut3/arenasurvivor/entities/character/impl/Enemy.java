package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.character.Character;

public class Enemy extends Character {

    private int hp;
    private int nbBullets;

    public Enemy(Bitmap image, int rowCount, int colCount, int x, int y) {
        super(image, rowCount, colCount, x, y);
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
