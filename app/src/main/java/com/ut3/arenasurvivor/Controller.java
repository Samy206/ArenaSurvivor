package com.ut3.arenasurvivor;

import android.view.MotionEvent;
import android.view.View;

import com.ut3.arenasurvivor.entities.character.impl.Player;

public class Controller implements View.OnTouchListener {

    // Variables for dash
    private float x1,x2;
    private final double MIN_DISTANCE = 150;
    private Long lastDashTimer;
    private final double dashDelay;

    // player movements application
    private final Player player;

    public Controller(Player player) {
        this.player = player;
        lastDashTimer = 0L;
        dashDelay = 1.5 * Math.pow(10,9);
    }

    /**
     * Uses screen controllers to move the player in a certain way
     * @param view
     * @param motionEvent
     * @return screen touch confirmation as boolean
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float difference;
        int direction;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = motionEvent.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                x2 = motionEvent.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    Long now  = System.nanoTime();
                    // Code optimisation to avoid if & else if.
                    // Direction will be -1 (right to left dash) or 1 (left to right dash)
                    difference = (x2 - x1);
                    direction = (int) (difference / Math.abs(difference));
                    if( (now - lastDashTimer) > dashDelay) {
                        player.dash(direction);
                        player.move(direction);
                        lastDashTimer = now;
                    }
                }
                else {
                    // Code optimisation to avoid if & else if.
                    // Direction will be -1 (move to the left) or 1 (move to the right)
                    difference = (x1 - player.getX());
                    direction = (int) (difference / Math.abs(difference));
                    player.move(direction);
                }
                break;

            default:
                return true;
        }

        return true;
    }
}
