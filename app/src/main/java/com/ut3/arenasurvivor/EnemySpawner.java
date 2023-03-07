package com.ut3.arenasurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

import com.ut3.arenasurvivor.entities.character.impl.Enemy;
import com.ut3.arenasurvivor.entities.character.impl.Player;

public class EnemySpawner {

    private GameView gameView;

    private Bitmap enemySprite;

    private final int  LOWEST_SPAWN_HEIGHT = 600;

    private int WAIT_TIME = 500;
    private int waitTimer;

    private long lastUpdateCall = -1;

    public EnemySpawner(GameView gameView, Bitmap enemySprite) {
        this.gameView = gameView;
        this.enemySprite = enemySprite;
        waitTimer = WAIT_TIME;
    }

    public void update(){
        Long now = System.nanoTime();
        int deltaTime = (int) ((now - lastUpdateCall) / 1000000);
        waitTimer -= deltaTime;

        if(waitTimer <= 0){
            Enemy enemy = createEnemyWithRandomPos();
            gameView.putEnemies(enemy);
            waitTimer = WAIT_TIME;
        }

        lastUpdateCall = System.nanoTime();
    }

    private Enemy createEnemyWithRandomPos(){
        WindowManager wm = (WindowManager) gameView.getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics metrics = wm.getCurrentWindowMetrics();
        Rect rect = metrics.getBounds();

        int x = (int) (Math.random() * (rect.right - enemySprite.getWidth())) + enemySprite.getWidth();
        int y = (int) (Math.random() * (rect.bottom - LOWEST_SPAWN_HEIGHT)) + enemySprite.getHeight();

        return new Enemy(gameView, enemySprite, x, y);
    }
}
