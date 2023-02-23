package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ut3.arenasurvivor.GameView;
import com.ut3.arenasurvivor.entities.character.Character;

public class Player extends Character {

    private static final int ROW_TOP_TO_BOTTOM = 0;
    private static final int ROW_RIGHT_TO_LEFT = 1;
    private static final int ROW_LEFT_TO_RIGHT = 2;
    private static final int ROW_BOTTOM_TO_TOP = 3;

    // The thickness of the gound object so the player wont drown
    private final int groundThickness = 100;

    //Row index of image are being used
    private int rowUsing = ROW_LEFT_TO_RIGHT;

    private int colUsing;

    private Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;

    public static final float VELOCITY = 1f;

    public int movingVectorX = 10;
    public int movingVectorY = 5;

    private long lastDrawNanoTime = -1;

    private GameView gameView;

    public Player(GameView gameView, Bitmap image, int x, int y) {
        super(image, 4, 3, x, y);
        this.gameView = gameView;

        this.topToBottoms = new Bitmap[colCount]; //3
        this.rightToLefts = new Bitmap[colCount]; //3
        this.leftToRights = new Bitmap[colCount]; //3
        this.bottomToTops = new Bitmap[colCount]; //3

        for (int col = 0; col < this.colCount; col++) {
            this.topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            this.rightToLefts[col] = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.leftToRights[col] = this.createSubImageAt(ROW_LEFT_TO_RIGHT, col);
            this.bottomToTops[col] = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
        }
    }

    public Bitmap[] getMoveBitmaps() {
        switch (rowUsing) {
            case ROW_BOTTOM_TO_TOP:
                return this.bottomToTops;
            case ROW_LEFT_TO_RIGHT:
                return this.leftToRights;
            case ROW_RIGHT_TO_LEFT:
                return this.rightToLefts;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap() {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }

    public void update() {
        this.colUsing = (this.colUsing + 1) % this.colCount;









        //When the game's character touches the edge of the screen, then change direction
        if (this.x < 0) {
            this.x = 0;
            this.movingVectorX = -this.movingVectorX;
        } else if (this.x > this.gameView.getWidth() - width) {
            this.x = this.gameView.getWidth() - width;
            this.movingVectorX = -this.movingVectorX;
        }

        if (this.y < 0) {
            this.y = 0;
            this.movingVectorY = -this.movingVectorY;
        } else if (this.y > this.gameView.getHeight() - height) {
            this.y = this.gameView.getHeight() - height;
            this.movingVectorY = -this.movingVectorY;
        }

        if(this.y > gameView.getHeight()-100){
            this.y = gameView.getHeight()-groundThickness;
        }

        //rowUsing
        if (movingVectorX > 0) {
            if (movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            } else if (movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            } else {
                this.rowUsing = ROW_LEFT_TO_RIGHT;
            }
        } else {
            if (movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            } else if (movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            } else {
                this.rowUsing = ROW_RIGHT_TO_LEFT;
            }
        }
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap, x, y, null);
        //Last draw Time
        this.lastDrawNanoTime = System.nanoTime();
    }

    public void move(int direction){
        //Current time in nanoseconds
        long now = System.nanoTime();

        //Never once did draw
        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
        }

        //Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds)
        int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);

        //Distance moves
        float distance = VELOCITY * deltaTime;

        double movingVectorLength = Math.sqrt(movingVectorX ^ 2 + movingVectorY ^ 2);

        //Calculate the new position of the game character
        this.x = x + (int) ((distance * movingVectorX / movingVectorLength) * direction);
        //this.y = y + (int) (distance * movingVectorY / movingVectorLength);
    }

    public void setMovingVector(int movingVectorX, int movingVectorY) {
        this.movingVectorX = movingVectorX;
        this.movingVectorY = movingVectorY;
    }


    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return false;
    }
}
