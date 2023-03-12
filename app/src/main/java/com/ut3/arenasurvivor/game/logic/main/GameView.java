package com.ut3.arenasurvivor.game.logic.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.ut3.arenasurvivor.game.logic.utils.EnemySpawner;
import com.ut3.arenasurvivor.R;
import com.ut3.arenasurvivor.entities.character.impl.Enemy;

import com.ut3.arenasurvivor.entities.character.impl.Player;
import com.ut3.arenasurvivor.entities.impl.Projectile;
import com.ut3.arenasurvivor.game.logic.utils.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Long startTime;
    private SharedPreferences sharedPreferences;
    private Player player;
    private Map<Enemy, Integer> enemies;
    private List<Projectile> projectiles;

    private ScoreCalculator calculator;
    private EnemySpawner spawner;

    public GameView(Context context, SharedPreferences sharedPreferences) {
        super(context);
        getHolder().addCallback(this);
        //Variables init
        //enemies = Collections.synchronizedList(new ArrayList<>());
        enemies = new ConcurrentHashMap<>();
        projectiles = new ArrayList<>();
        thread = new GameThread(getHolder(), this, sharedPreferences);
        startTime = System.nanoTime();
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
            projectile.move(5, 10);
        }
        spawner.update();


        calculator.updateScore(startTime);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //Entities init
        Bitmap playerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        player = new Player(this, playerBitmap, 0, 0);
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
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawRect(0, canvas.getHeight()-100, canvas.getWidth(), canvas.getHeight(), paint);
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
        Projectile newProjectile = new Projectile("projectile" + projectiles.size(), new Rect(x, y, x + 10, y + 10));
        projectiles.add(newProjectile);
    }

    public void destroyEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public void putEnemies(Enemy enemy){
        enemies.put(enemy, enemies.size());
    }

    public int getPlayerX(){
        return this.player.getX();
    }

    public int getPlayerY(){
        return this.player.getY();
    }
}
