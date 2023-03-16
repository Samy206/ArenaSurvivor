package com.ut3.arenasurvivor.entities.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.Collidable;

public class Platform implements Collidable {

    private Rect hitBox;

    public Platform(Rect source) {
        hitBox = source;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rect hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return (dangerHitBox != null) && hitBox.intersect(dangerHitBox);
    }

    public void draw (Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FF6200EE"));
        canvas.drawRect(hitBox, paint);
    }

}
