package com.ut3.arenasurvivor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ut3.arenasurvivor.game.logic.main.GameView;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.Controller;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainMenuActivity.SHARED_PREF, MODE_PRIVATE);
        super.onCreate(savedInstanceState);

        gameView = new GameView(this, sharedPreferences, this);
        setContentView(gameView);
    }

    public void returnToMenuActivity() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}