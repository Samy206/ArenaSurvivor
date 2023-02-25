package com.ut3.arenasurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.ut3.arenasurvivor.entities.character.impl.Enemy;
import com.ut3.arenasurvivor.entities.character.impl.Player;

public class EnemySpawner {

    private GameView gameView;

    private int WAIT_TIME = 500;
    private int waitTimer;

    private long lastUpdateCall = -1;

    public EnemySpawner(GameView gameView) {
        this.gameView = gameView;
        waitTimer = WAIT_TIME;
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
            Display display = wm.getDefaultDisplay();
            int x = (int) (Math.random() * display.getWidth()) + enemyBitmap.getWidth();
            int y = (int) (Math.random() * display.getHeight()) + enemyBitmap.getHeight() + 20;
            Enemy enemy = new Enemy(gameView, enemyBitmap, x, y);
            gameView.putEnemies(enemy);
            waitTimer = WAIT_TIME;
        }
        lastUpdateCall = System.nanoTime();
    }
}
