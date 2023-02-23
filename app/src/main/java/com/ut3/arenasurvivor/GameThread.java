package com.ut3.arenasurvivor;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;

import java.util.logging.Logger;

public class GameThread extends Thread {

    SharedPreferences sharedPreferences;
    final SurfaceHolder surfaceHolder;
    GameView gameView;
    Boolean running;
    Logger LOGGER;
    public static Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView, SharedPreferences sharedPreferences) {
        super();
        this.sharedPreferences = sharedPreferences;
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        LOGGER = Logger.getLogger("GameThread");
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        long timePlayed;
        while (running) {
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    timePlayed = System.nanoTime() - startTime;
                    this.gameView.update(timePlayed);
                    this.gameView.draw(canvas);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            long now = System.nanoTime() ;
            // Interval to redraw game
            // (Change nanoseconds to milliseconds)
            long waitTime = (now - startTime)/1000000;
            if(waitTime < 10)  {
                waitTime= 10; // Millisecond.
            }
            try{
                this.sleep(waitTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            startTime = System.nanoTime();
        }
    }
}
