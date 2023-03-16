package com.ut3.arenasurvivor.entities.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.Collidable;
import com.ut3.arenasurvivor.entities.Movable;


public class Projectile implements Collidable, Movable {

    public Rect hitBox;

    private final int SIZE = 10 ;

    private final int x;

    private final int y;

    private final int playerX;

    private final int playerY;
    private long lastDrawNanoTime = -1;

    private final double SPEED = 4.0;

    public Projectile(int x, int y, int playerX, int playerY) {
        hitBox = new Rect(x, y, x + SIZE, y + SIZE);
        this.x = x;
        this.y = y;
        this.playerX = playerX;
        this.playerY = playerY;
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
