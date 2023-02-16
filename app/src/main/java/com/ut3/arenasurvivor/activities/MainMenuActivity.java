package com.ut3.arenasurvivor.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.R;

public class MainMenuActivity extends AppCompatActivity {


    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView score = (TextView) findViewById(R.id.Score);
        score.setText("Le score est de 20 connard");
    }
}