package com.ut3.arenasurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.ut3.arenasurvivor.entities.character.impl.Player;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;

    private Player player;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    public void update() {
        this.player.update();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Bitmap playerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        player = new Player(this, playerBitmap, 0, 0);
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            player.draw(canvas);
        }
    }
}
