package com.ut3.arenasurvivor.activities;


import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ut3.arenasurvivor.R;
import com.ut3.arenasurvivor.game.logic.main.GameView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.Controller;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;
    SensorManager sm = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initGameView();

        setContentView(createRootPanel());

        setupActionBar();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.activity_game_action_bar);

    }


    private void initGameView(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainMenuActivity.SHARED_PREF, MODE_PRIVATE);
        gameView = new GameView(this, sharedPreferences, this);
        gameView.setZOrderOnTop(true);
        gameView.getHolder().setFormat(PixelFormat.TRANSPARENT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION
        );
        sm.registerListener(this, sensor, SensorManager.
                SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        sm.unregisterListener(this, sm.getDefaultSensor(Sensor.
                TYPE_LINEAR_ACCELERATION));
        super.onStop();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensor = sensorEvent.sensor.getType();
        float[] values = sensorEvent.values;
        synchronized (this) {
            if (sensor == Sensor.TYPE_LINEAR_ACCELERATION) {

                if(values[2] > 10){
                    gameView.getPlayer().jump();
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private RelativeLayout createRootPanel(){
        // Setup your ImageView
        ImageView bgImagePanel = new ImageView(this);
        bgImagePanel.setBackgroundResource(R.drawable.background);

        // Use a RelativeLayout to overlap both SurfaceView and ImageView
        RelativeLayout.LayoutParams fillParentLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        RelativeLayout rootPanel = new RelativeLayout(this);

        rootPanel.setLayoutParams(fillParentLayout);
        rootPanel.addView(gameView, fillParentLayout);
        rootPanel.addView(bgImagePanel, fillParentLayout);

        return rootPanel;
    }

    public void returnToMenuActivity() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}