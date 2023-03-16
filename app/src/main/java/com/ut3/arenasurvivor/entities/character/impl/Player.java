package com.ut3.arenasurvivor.entities.character.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ut3.arenasurvivor.game.logic.main.GameView;
import com.ut3.arenasurvivor.entities.character.Character;

public class Player extends Character {

    private static final int ROW_TOP_TO_BOTTOM = 0;
    private static final int ROW_RIGHT_TO_LEFT = 1;
    private static final int ROW_LEFT_TO_RIGHT = 2;
    private static final int ROW_BOTTOM_TO_TOP = 3;

    private int initialGroundYPosition;

    // The thickness of the gound object so the player wont drown
    private final int groundThickness = 100;

    //Row index of image are being used
    private int rowUsing = ROW_LEFT_TO_RIGHT;

    private int colUsing;

    private Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;
    private Rect hitBox;

    private int comfortWidth;

    public static final float VELOCITY = 0.4f;
    public static final float JUMP_VELOCITY = 7f;

    private boolean canMove = false;

    private int direction = 0;

    private long lastDrawNanoTime = -1;

    private GameView gameView;

    private boolean canJump = true;
    private boolean jumping;
    private float jumpingDirection = -1f;

    //jumping stops at y = jumpingTreshold
    private final int jumpingTreshold = 400;


    public Player(GameView gameView, Bitmap image, int x, int y) {
        super(image, 4, 3, x, y);
        this.gameView = gameView;
        this.initialGroundYPosition = y;

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
        comfortWidth = this.gameView.getWidth() - this.width;
        this.hitBox = new Rect(x, y, x + this.width, y + this.height);
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
        //Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds)
        //Current time in nanoseconds
        long now = System.nanoTime();

        //Never once did draw
        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
        }

        int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);

        //Distance moves
        float distance = VELOCITY * deltaTime;
        int offsetX = (int) (direction * distance);

        //Calculate the new position of the game character
        movePlayer(offsetX);
        this.hitBox.offset(offsetX, 0);


        if (this.direction > 0) {
            this.rowUsing = ROW_LEFT_TO_RIGHT;
        } else {
            this.rowUsing = ROW_RIGHT_TO_LEFT;
        }

        this.canJump = (this.y == this.initialGroundYPosition);

        if(jumping){
            if(this.y <= jumpingTreshold){
                jumpingDirection = jumpingDirection*-1f;
            }

            int offsetY = (int) (JUMP_VELOCITY * jumpingDirection);
            this.y += offsetY;
            this.hitBox.offset(0, offsetY);

            if(this.y == initialGroundYPosition){
                this.jumping = false;
                this.canJump = true;
                this.jumpingDirection = -1;
            }
        }
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap, x, y, null);

        //Last draw Time
        this.lastDrawNanoTime = System.nanoTime();
    }


    @Override
    public boolean detectCollision(Rect dangerHitBox) {
        return (dangerHitBox != null) && hitBox.intersect(dangerHitBox);
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public void move(int direction) {
        this.direction = direction;
    }


    public void movePlayer(int offsetX) {
        int testNegativeValue = this.hitBox.left + offsetX;
        int testGreaterValue = this.hitBox.right + offsetX;
        if (testNegativeValue < 0) {
            this.x = 0;
            this.hitBox.left = x;
            this.hitBox.right = x + this.width;
        } else if (testGreaterValue >= this.gameView.getWidth()) {
            this.x = comfortWidth;
            this.hitBox.left = x;
            this.hitBox.right = x + this.width;
        } else {
            this.x = testNegativeValue;
            this.hitBox.offset(offsetX, 0);
        }
    }

    public void jump() {
        if(this.canJump){
            this.jumping = true;
            this.canJump = false;
        }
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }


    /**
     * Dash into a direction given by parameter, -1 being tot the left and 1 to the right
     */
    public void dash(int direction) {
        //Calculate the new position of the game character
        int offsetX = (int) (direction * VELOCITY * 1000);
        movePlayer(offsetX);
    }
}
