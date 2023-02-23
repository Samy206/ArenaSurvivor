package com.ut3.arenasurvivor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.ut3.arenasurvivor.activities.MainMenuActivity;
import com.ut3.arenasurvivor.entities.character.impl.Player;
import com.ut3.arenasurvivor.entities.impl.Projectile;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Long startTime;
    private SharedPreferences sharedPreferences;
    private Player player;
    private Projectile projectile;


    public GameView(Context context, SharedPreferences sharedPreferences) {
        super(context);
        this.startTime = System.nanoTime();
        this.sharedPreferences = sharedPreferences;
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this, sharedPreferences);
        projectile = new Projectile("ProjectileA",
                new Rect(100, 100, 200, 200));
        setFocusable(true);
    }

    public void update(long timePlayed) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long score = (long) ((System.nanoTime() - this.startTime) / (2*Math.pow(10,9)));
        editor.putLong(MainMenuActivity.SHARED_PREF, score);
        editor.apply();
        editor.commit();
        projectile.move(5, 10);
        this.player.update();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Bitmap playerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        player = new Player(this, playerBitmap, 0, 0);
        thread = new GameThread(getHolder(), this, sharedPreferences);
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
            projectile.draw(canvas);
        }
    }
}
