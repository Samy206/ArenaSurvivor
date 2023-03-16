package com.ut3.arenasurvivor.game.logic.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ut3.arenasurvivor.Controller;
import com.ut3.arenasurvivor.entities.impl.Platform;
import com.ut3.arenasurvivor.game.logic.utils.EnemySpawner;
import com.ut3.arenasurvivor.R;
import com.ut3.arenasurvivor.activities.GameActivity;
import com.ut3.arenasurvivor.entities.character.impl.Enemy;

import com.ut3.arenasurvivor.entities.character.impl.Player;
import com.ut3.arenasurvivor.entities.impl.Projectile;
import com.ut3.arenasurvivor.game.logic.utils.PlatformsSpawner;
import com.ut3.arenasurvivor.game.logic.utils.ScoreCalculator;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameActivity gameActivity;
    private final SharedPreferences sharedPreferences;
    private final Map<Enemy, Integer> enemies;
    private final ArrayBlockingQueue<Projectile> projectiles;
    private final ArrayBlockingQueue<Platform> platforms;
    private GameThread thread;
    private final Long startTime;
    private TextView currentScore;


    //Entities
    private Player player;

    //Utilities
    private ScoreCalculator scoreCalculator;
    private EnemySpawner enemySpawner;
    private PlatformsSpawner platformsSpawner;

    //Constants
    private final double SCREAM_AMPLITUDE = 10000;
    private final int PROJECTILES_THRESHOLD = 50;
    private Boolean cryUsed = false;


    public GameView(Context context, SharedPreferences sharedPreferences, GameActivity gameActivity) {
        super(context);
        getHolder().addCallback(this);
        //Variables init
        enemies = new ConcurrentHashMap<>();
        projectiles = new ArrayBlockingQueue<>(100);
        platforms = new ArrayBlockingQueue<>(3);
        thread = new GameThread(getHolder(), this, sharedPreferences);
        startTime = System.nanoTime();

        this.sharedPreferences = sharedPreferences;

        this.gameActivity = gameActivity;

        thread = new GameThread(getHolder(), this, sharedPreferences);

        setFocusable(true);
    }


    public void createPlatforms() {
        for(int i = 0; i < 3; i++) {
            platforms.add(platformsSpawner.createPlatformWithRandomPos());
        }
    }

    public void update() {
        this.player.update();

        for (Enemy enemy : enemies.keySet()) {
            enemy.update();
        }
        for (Projectile projectile : projectiles) {
            projectile.move();
        }

        enemySpawner.update();

        //Score update
        gameActivity.runOnUiThread(() -> {
            String score = "Score actuel :" + scoreCalculator.calculateScore(startTime);
            currentScore.setText(score);
        });
        scoreCalculator.updateScore(startTime);

        //Collision
        detectCollision();

        //Delete the old projectiles
        if(projectiles.size() > PROJECTILES_THRESHOLD){
            projectiles.poll();
        }

     if(!cryUsed) {
        double amplitude = gameActivity.getAmplitude();
            if(amplitude > SCREAM_AMPLITUDE){
                this.clearGame();
                this.cryUsed = true;
            }
        }

    }

    private void initEntities(){
        //Init player
        Bitmap playerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.player);
        int playerHeight = (int) (this.getHeight() * 0.82);
        player = new Player(this, playerBitmap, 0, playerHeight);
        player.move(1);

        //Add player controller
        setOnTouchListener(new Controller(player));
    }

    private void initUtilities(){
        //Init platformSpawner
        platformsSpawner = new PlatformsSpawner(this.getWidth(), this.getHeight());
        createPlatforms();

        //Init currentScore textView
        currentScore = gameActivity.getSupportActionBar().getCustomView().findViewById(R.id.currentScore);

        //Init enemy spawner
        Bitmap enemyBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.enemy);
        enemySpawner = new EnemySpawner(this, enemyBitmap);

        //Init ScoreCalculator
        scoreCalculator = new ScoreCalculator(sharedPreferences);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        initEntities();

        initUtilities();

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
                Log.e("SurfaceDestroyed", e.getMessage());
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
            for (Platform platform : platforms) {
                platform.draw(canvas);
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

    public void clearGame() {
        this.projectiles.clear();
        this.enemies.clear();
    }

    private void detectCollision() {

        for(Projectile projectile : projectiles) {

            for(Platform platform : platforms) {
                if (projectile.detectCollision(platform.getHitBox())) {
                    projectiles.remove(projectile);
                }
            }

            if(projectile.detectCollision(player.getHitBox())) {
                try {
                    clearGame();
                    thread.setRunning(false);
                    this.gameActivity.returnToMenuActivity();
                }
                catch (Exception e) {
                    Log.e("ENDGAME", e.getMessage());
                }
                break;
            }
        }


    }

    public Player getPlayer(){
        return this.player;
    }


}
