package com.ut3.arenasurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

import com.ut3.arenasurvivor.entities.character.impl.Enemy;
import com.ut3.arenasurvivor.entities.character.impl.Player;

import java.util.Random;

public class EnemySpawner {

    private GameView gameView;

    private int WAIT_TIME = 500;
    private int waitTimer;

    private long lastUpdateCall = -1;

    public EnemySpawner(GameView gameView) {
        this.gameView = gameView;
        waitTimer = WAIT_TIME*2;
    }

    public int[] getCoordinates(Rect window, Bitmap enemyBitmap) {

        // height is 800px, with comfort space for ennemies it goes down to 690px
        int bitmapComfortHeight = enemyBitmap.getHeight() * 2;
        int bitmapComfortWidth = enemyBitmap.getHeight() * 2;


        int highBoundHeight = window.bottom - bitmapComfortHeight - 500;
        int lowBoundHeight = window.bottom - (115) - 500 -bitmapComfortHeight;

        Random random = new Random();

        int x = random.nextInt(window.right - bitmapComfortWidth) + bitmapComfortWidth;
        int y = random.nextInt(highBoundHeight - lowBoundHeight) + lowBoundHeight;

        return (new int[] {x, y});

    }

    public void update(){
        Long now = System.nanoTime();
        int deltaTime = (int) ((now - lastUpdateCall) / 1000000);
        Log.d("TIMER", deltaTime + " " + waitTimer);
        waitTimer -= deltaTime;
        if(waitTimer <= 0){
            //TODO mieux faire les positions de spawns, possible qu'il spawn en dehors de l'écran + pas jolie
            Bitmap enemyBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.output_onlinepngtools);
            WindowManager wm = (WindowManager) gameView.getContext().getSystemService(Context.WINDOW_SERVICE);
            WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();

            Rect window = windowMetrics.getBounds();
            int[] coordinates = getCoordinates(window, enemyBitmap);

            Enemy enemy = new Enemy(gameView, enemyBitmap, coordinates[0], coordinates[1]);
            gameView.putEnemies(enemy);
            waitTimer = WAIT_TIME;
        }
        lastUpdateCall = System.nanoTime();
    }
}
