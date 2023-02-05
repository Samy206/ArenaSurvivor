package com.ut3.arenasurvivor;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import java.util.logging.Logger;

public class GameThread extends Thread {
    final SurfaceHolder surfaceHolder;
    GameView gameView;
    Boolean running;
    Logger LOGGER;

    private Handler actionHandler;
    public static Canvas canvas;

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            gameView.update();
            actionHandler.post(draw);
        }
    };

    private Runnable draw = new Runnable() {
        @Override
        public void run() {
            gameView.draw(canvas);
            actionHandler.postDelayed(update,500);
        }
    };

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
        actionHandler = new Handler(Looper.getMainLooper());
        while(running) {
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameView.draw(canvas);
                    actionHandler.postDelayed(update, 5000);
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
        }


    }
}
