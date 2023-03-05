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

import com.ut3.arenasurvivor.activities.GameActivity;
import com.ut3.arenasurvivor.activities.MainMenuActivity;
import com.ut3.arenasurvivor.entities.character.impl.Enemy;
import com.ut3.arenasurvivor.entities.character.impl.Player;
import com.ut3.arenasurvivor.entities.impl.Platform;
import com.ut3.arenasurvivor.entities.impl.Projectile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameActivity gameActivity;
    private GameThread thread;
    private Long startTime;
    private SharedPreferences sharedPreferences;
    private Player player;
    private List<Platform> platformList;
    private Map<Enemy, Integer> enemies;
    private List<Projectile> projectiles;

    private EnemySpawner spawner;

    public GameView(Context context, SharedPreferences sharedPreferences, GameActivity gameActivity) {
        super(context);
        this.gameActivity = gameActivity;
        platformList = new ArrayList<Platform>();
        this.startTime = System.nanoTime();
        this.sharedPreferences = sharedPreferences;
        Drawable background = getResources().getDrawable(R.mipmap.ic_launcher_background);
        //setBackground(background);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this, sharedPreferences);
        platformList.add(new Platform(new Rect(150, 300, 450, 320)));
        platformList.add(new Platform(new Rect(450, 400, 600, 420)));
        platformList.add(new Platform(new Rect(650, 300, 800, 320)));
        //Variables init
        //enemies = Collections.synchronizedList(new ArrayList<>());
        enemies = new ConcurrentHashMap<>();
        projectiles = new ArrayList<>();
        thread = new GameThread(getHolder(), this, sharedPreferences);
        spawner = new EnemySpawner(this);
        setFocusable(true);
    }

    public void update(long timePlayed) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long score = (long) ((System.nanoTime() - this.startTime) / (2*Math.pow(10,9)));
        editor.putLong(MainMenuActivity.SHARED_PREF, score);
        editor.apply();
        editor.commit();
        this.player.update();
        for (Enemy enemy : enemies.keySet()) {
            enemy.update();
        }
        for (Projectile projectile : projectiles) {
            projectile.move(2, 1);
        }
        spawner.update();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //Entities init
        Bitmap playerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        Bitmap enemyBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.output_onlinepngtools);
        player = new Player(this, playerBitmap, 0, 700);
        player.setMovingVector(10, 0);
        thread = new GameThread(getHolder(), this, sharedPreferences);
        enemies.put(new Enemy(this, enemyBitmap, 150, 150), 0);
        enemies.put(new Enemy(this, enemyBitmap, 300, 150), 0);
        //Thread Start
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
    public synchronized void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {

            // Detect collision and delete safely elements
            List<Integer> platformsToDelete = new ArrayList<>();
            List<Integer> projetilesToDelete = new ArrayList<>();
            int platformIndex = 0;
            int projectileIndex = 0;

            // Draw elements
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawRect(0, canvas.getHeight() - 50, canvas.getWidth(), canvas.getHeight(), paint);
            player.draw(canvas);


            for (Enemy enemy : enemies.keySet()) {
                enemy.draw(canvas);
            }

            for (Platform platform : platformList) {
                platform.draw(canvas);
            }

            for (Projectile projectile : projectiles) {

                for (Platform platform : platformList) {
                    // if we detect collision with platform
                    if (platform.detectCollision(projectile.getHitBox())) {
                        platformsToDelete.add(platformIndex);
                        projetilesToDelete.add(projectileIndex);
                    }
                    else {
                        projectile.draw(canvas);
                        // If we detect collision with a player
                        if (projectile.detectCollision(player.getHitBox())) {
                            try {
                                Thread.sleep(2000);
                                gameActivity.endGame();
                                break;
                                //thread.setRunning(false);
                            }catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    platformIndex += 1;
                }

                projectileIndex += 1;
            }

            // Collided projectiles
            for (Integer index : projetilesToDelete) {
                projetilesToDelete.remove(index);
            }

            // Collided platforms
            for (Integer index : platformsToDelete) {
                platformsToDelete.remove(index);
            }
        }
    }

    public void createProjectileAt(int x, int y) {
            Projectile newProjectile = new Projectile("projectile" + projectiles.size(), new Rect(x, y, x + 10, y + 10));projectiles.add(newProjectile);
    }

    public void destroyEnemy(Enemy enemy) {
            enemies.remove(enemy);
    }

    public void putEnemies(Enemy enemy){
            enemies.put(enemy, enemies.size());
    }
}

