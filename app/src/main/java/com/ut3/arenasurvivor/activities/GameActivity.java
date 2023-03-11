package com.ut3.arenasurvivor.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.Controller;
import com.ut3.arenasurvivor.GameView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainMenuActivity.SHARED_PREF, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        //No Title
        GameView gameView = new GameView(this, sharedPreferences);
        setContentView(gameView);

        gameView.setOnTouchListener(new Controller());
    }

}