package com.ut3.arenasurvivor.activities;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.Controller;
import com.ut3.arenasurvivor.GameView;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;
    SensorManager sm = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainMenuActivity.SHARED_PREF, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        //No Title
        gameView = new GameView(this, sharedPreferences);
        setContentView(gameView);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        gameView.setOnTouchListener(new Controller());


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
}