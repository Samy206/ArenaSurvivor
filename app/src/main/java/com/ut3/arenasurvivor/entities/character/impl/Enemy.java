package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.character.Character;

public class Enemy implements Character {

    private int hp;
    private int nbBullets;

    public void fire(){

    }

    public void spawn(){

    }

    public void despawn(){

    }


    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return false;
    }

    @Override
    public void move(int movementX, int movementY) {

    }
}
