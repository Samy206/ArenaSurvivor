package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.Collidable;
import com.ut3.arenasurvivor.entities.Movable;
import com.ut3.arenasurvivor.entities.character.Character;

public class Player implements Character {

    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return false;
    }

    @Override
    public void move(int movementX, int movementY) {

    }
    private Bitmap sprite;

    public Player() {
        Rect rect = new Rect();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
    }

}
