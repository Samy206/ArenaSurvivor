package com.ut3.arenasurvivor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.ut3.arenasurvivor.activities.MainMenuActivity;
import com.ut3.arenasurvivor.entities.character.impl.Player;
import com.ut3.arenasurvivor.entities.impl.Platform;
import com.ut3.arenasurvivor.entities.impl.Projectile;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Long startTime;
    private SharedPreferences sharedPreferences;
    private Player player;
    private Projectile projectile;
    private List<Platform> platformList;

    public GameView(Context context, SharedPreferences sharedPreferences) {
        super(context);
        platformList = new ArrayList<Platform>();
        this.startTime = System.nanoTime();
        this.sharedPreferences = sharedPreferences;
        Drawable background = getResources().getDrawable(R.mipmap.ic_launcher_background);
        //setBackground(background);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this, sharedPreferences);
        projectile = new Projectile("ProjectileA",
                new Rect(100, 100, 200, 200));
        platformList.add(new Platform(new Rect(150, 300, 450, 320)));
        platformList.add(new Platform(new Rect(450, 400, 600, 420)));
        platformList.add(new Platform(new Rect(650, 300, 800, 320)));
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
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawRect(0, canvas.getHeight()-100, canvas.getWidth(), canvas.getHeight(), paint);
            player.draw(canvas);
            projectile.draw(canvas);

            for (Platform platform : platformList) {
                platform.draw(canvas);
                if(platform.detectCollision(projectile.getHitBox())) {
                    platformList.remove(platform);
                }
            }


        }
    }
}
