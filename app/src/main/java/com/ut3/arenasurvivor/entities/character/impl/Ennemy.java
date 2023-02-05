package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.character.Character;

public class Ennemy implements Character {
    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return false;
    }

    @Override
    public void move(int movementX, int movementY) {

    }
}
