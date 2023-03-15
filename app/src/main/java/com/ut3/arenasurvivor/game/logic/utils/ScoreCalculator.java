package com.ut3.arenasurvivor.game.logic.utils;

import android.content.SharedPreferences;

import com.ut3.arenasurvivor.activities.MainMenuActivity;

public class ScoreCalculator {

    private SharedPreferences sharedPreferences;
    private final double timeOffset = 2 * Math.pow(10,9);

    public ScoreCalculator(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public long calculateScore(Long startTime){
        long timePlayed = System.nanoTime() - startTime;
        return (long) (timePlayed / timeOffset);
    }

    public void updateScore(Long startTime) {
        long score = calculateScore(startTime);
        long previousMaxScore = sharedPreferences.getLong(MainMenuActivity.SHARED_PREF, 0);

        if(score > previousMaxScore) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(MainMenuActivity.SHARED_PREF, score);
            editor.commit();
            editor.apply();
        }


    }

}
