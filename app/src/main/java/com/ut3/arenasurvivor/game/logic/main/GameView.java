package com.ut3.arenasurvivor.game.logic.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.ut3.arenasurvivor.game.logic.main.GameThread;
import com.ut3.arenasurvivor.game.logic.utils.EnemySpawner;
import com.ut3.arenasurvivor.R;
import com.ut3.arenasurvivor.activities.GameActivity;
import com.ut3.arenasurvivor.entities.character.impl.Enemy;

import com.ut3.arenasurvivor.activities.MainMenuActivity;
import com.ut3.arenasurvivor.entities.character.impl.Player;
import com.ut3.arenasurvivor.entities.impl.Projectile;
import com.ut3.arenasurvivor.game.logic.utils.ScoreCalculator;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameActivity gameActivity;
    private GameThread thread;
    private Long startTime;
    private SharedPreferences sharedPreferences;
    private Player player;
    private Map<Enemy, Integer> enemies;
    private ArrayBlockingQueue<Projectile> projectiles;

    private ScoreCalculator calculator;
    private EnemySpawner spawner;

    private final double CRY_AMPLITUDE = 10000;

    public GameView(Context context, SharedPreferences sharedPreferences, GameActivity gameActivity) {
        super(context);
        getHolder().addCallback(this);
        //Variables init
        enemies = new ConcurrentHashMap<>();
        projectiles = new ArrayBlockingQueue<>(100);
        thread = new GameThread(getHolder(), this, sharedPreferences);
        startTime = System.nanoTime();
        this.gameActivity = gameActivity;
        Bitmap enemyBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.output_onlinepngtools);
        spawner = new EnemySpawner(this, enemyBitmap);
        calculator = new ScoreCalculator(sharedPreferences);
        setFocusable(true);

    }

    public void update() {

        this.player.update();
        for (Enemy enemy : enemies.keySet()) {
            enemy.update();
        }
        for (Projectile projectile : projectiles) {
            projectile.move();
        }
        spawner.update();


        calculator.updateScore(startTime);

        detectCollision();

        if(projectiles.size() > 50){
            projectiles.poll();
        }

        double amplitude = gameActivity.getAmplitude();
        if(amplitude > CRY_AMPLITUDE){
            this.endGame();
        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //Entities init
        Bitmap playerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        int playerHeight = (int) (this.getHeight() * 0.82);
        player = new Player(this, playerBitmap, 0, playerHeight);
        
        //Thread Start
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
    public synchronized void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            //Reset display for background
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            player.draw(canvas);
            for (Enemy enemy : enemies.keySet()) {
                enemy.draw(canvas);
            }
            for (Projectile projectile : projectiles) {
                projectile.draw(canvas);
            }
        }
    }

    public void createProjectileAt(int x, int y) {
        Projectile newProjectile = new Projectile("projectile" + projectiles.size(), x, y, getPlayerX(), getPlayerY());
        projectiles.add(newProjectile);
    }

    private int getPlayerY() {
        return player.getY();
    }

    private int getPlayerX() {
        return player.getX();
    }

    public void destroyEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public void putEnemies(Enemy enemy){
        enemies.put(enemy, enemies.size());
    }

    public void endGame() {
        this.projectiles.clear();
        this.enemies.clear();
    }

    private void detectCollision() {

        for(Projectile projectile : projectiles) {
            if(projectile.detectCollision(player.getHitBox())) {
                try {
                    endGame();
                    thread.setRunning(false);
                    this.gameActivity.returnToMenuActivity();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }


    }

    public Player getPlayer(){
        return this.player;
    }


}
