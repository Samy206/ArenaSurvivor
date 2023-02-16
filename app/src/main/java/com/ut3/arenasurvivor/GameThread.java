package com.ut3.arenasurvivor;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.logging.Logger;

public class GameThread extends Thread {
    SurfaceHolder surfaceHolder;
    GameView gameView;
    Boolean running;
    Logger LOGGER;
    public static Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
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
        while (running) {
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
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
