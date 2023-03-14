package com.ut3.arenasurvivor;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ut3.arenasurvivor.game.logic.main.GameView;

public class Controller implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Write your code to perform an action on contineus touch move
            case MotionEvent.ACTION_MOVE:
                // Write your code to perform an action on down
                if(x <= view.getWidth()/2){
                    ((GameView) view).getPlayer().move(-1);
                }else{
                    ((GameView) view).getPlayer().move(1);
                }
                break;
            case MotionEvent.ACTION_UP:
                // Write your code to perform an action on touch up
                ((GameView) view).getPlayer().setCanMove(false);
                break;
            default:
                return true;
        }


        return true;
    }
}
