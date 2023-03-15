package com.ut3.arenasurvivor.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.R;

public class MainMenuActivity extends AppCompatActivity {
    public static final String SHARED_PREF = "SCORE";
    public Long scoreMax;
    private TextView scoreView;

    private ListView listControl;

    String controlList[] = {"Appuyez à gauche ou à droite du personnage pour changer de direction ", "Swipez (Rester appuyer et relâcher plus loin ) pour faire dasher le personnage !", "Survivez le plus longtemps possible :)"};

    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);

        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setting layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        loadScore();
        // Setting score value using shared preference
        scoreView = findViewById(R.id.Score);
        scoreView.setText("Meilleur score : " + scoreMax);

        listControl = (ListView) findViewById(R.id.listControl);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listcontrolview, R.id.textView, controlList);
        listControl.setAdapter(arrayAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        loadScore();
        // Setting score value using shared preference
        scoreView = findViewById(R.id.Score);
        scoreView.setText("Meilleur score : " + scoreMax);
    }

    public void loadScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        scoreMax = sharedPreferences.getLong(SHARED_PREF, 0);
    }

}