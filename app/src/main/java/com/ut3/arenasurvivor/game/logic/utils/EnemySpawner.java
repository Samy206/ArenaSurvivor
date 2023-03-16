package com.ut3.arenasurvivor.game.logic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.WindowManager;
import android.view.WindowMetrics;

import com.ut3.arenasurvivor.entities.character.impl.Enemy;
import com.ut3.arenasurvivor.game.logic.main.GameView;

public class EnemySpawner {

    private final GameView gameView;

    private final Bitmap enemySprite;

    private int enemyWidth;
    private int enemyHeight;
    private int windowWidth;
    private int windowHeight;
    private int lowestSpawnHeight;

    private final int WAIT_TIME = 1000;
    private int waitTimer;

    private long lastUpdateCall = -1;

    public EnemySpawner(GameView gameView, Bitmap enemySprite) {
        this.gameView = gameView;
        this.enemySprite = enemySprite;
        waitTimer = WAIT_TIME;
        setUpResolutionValues();
    }

    /**
     * Setup class attributes to avoid repeated operations
     */
    public void setUpResolutionValues() {

        WindowManager wm = (WindowManager) gameView.getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics metrics = wm.getCurrentWindowMetrics();
        Rect rect = metrics.getBounds();

        windowHeight = rect.bottom;
        windowWidth = rect.right;
        enemyHeight = enemySprite.getHeight();
        enemyWidth = enemySprite.getWidth();
        lowestSpawnHeight = (int) (windowHeight * 0.8);

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
        int x = (int) (Math.random() * (windowWidth - enemyWidth)) + enemyWidth;
        int y = (int) (Math.random() * (windowHeight - lowestSpawnHeight)) + enemyHeight;
        return new Enemy(gameView, enemySprite, x, y);
    }
}
