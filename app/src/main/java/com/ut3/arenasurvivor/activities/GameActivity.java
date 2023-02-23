package com.ut3.arenasurvivor.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.GameView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //No Title
        setContentView(new GameView(this));

    }
}