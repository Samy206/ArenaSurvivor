package com.ut3.arenasurvivor.entities.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ut3.arenasurvivor.entities.Collidable;
import com.ut3.arenasurvivor.entities.Movable;

import java.util.logging.Logger;

public class Projectile implements Collidable, Movable {

    public Rect hitBox;
    private Logger LOGGER;
    private String componentName;

    private int SIZE = 10 ;

    private int x;

    private int y;

    private int playerX;

    private int playerY;
    private long lastDrawNanoTime = -1;

    private double SPEED = 2.5;

    public Projectile(String name, int x, int y, int playerX, int playerY) {
        componentName = name;
        hitBox = new Rect(x, y, x + SIZE, y + SIZE);
        this.x = x;
        this.y = y;
        this.playerX = playerX;
        this.playerY = playerY;
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
    public void move() {
        long now = System.nanoTime();

        //Never once did draw

        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
        }
        int deltaTime = (int) ((now - lastDrawNanoTime) / 10000000);

        int dirX = -x + playerX;
        int dirY = -y + playerY;
        double vectorNormal = Math.sqrt(dirX*dirX + dirY*dirY);

        int movementX= (int) ((dirX / vectorNormal) *deltaTime * SPEED);
        int movementY = (int) ((dirY / vectorNormal) *deltaTime * SPEED);

        hitBox.offset(movementX, movementY);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(hitBox, paint);
        this.lastDrawNanoTime = System.nanoTime();
    }
}
