package com.ut3.arenasurvivor.entities.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.Collidable;
import com.ut3.arenasurvivor.entities.Movable;

import java.util.logging.Logger;

public class Projectile implements Collidable, Movable {

    public Rect hitBox;
    private Logger LOGGER;
    private String componentName;

    public Projectile(String name, Rect source) {
        componentName = name;
        hitBox = source;
        LOGGER = Logger.getLogger(componentName);
    }

    public void setHitbox(Rect hitBox) {
        this.hitBox = hitBox;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return (dangerHitBox != null) && hitBox.intersect(dangerHitBox);
    }

    @Override
    public void move(int movementX, int movementY) {
        hitBox.offsetTo(movementX, movementY);
    }

    public void draw (Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(hitBox, paint);
    }
}
