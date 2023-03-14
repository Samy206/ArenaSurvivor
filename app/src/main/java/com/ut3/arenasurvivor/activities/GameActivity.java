package com.ut3.arenasurvivor.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ut3.arenasurvivor.R;
import com.ut3.arenasurvivor.game.logic.main.GameView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ut3.arenasurvivor.Controller;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initGameView();


        setContentView(createRootPanel());

    }



    private void initGameView(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainMenuActivity.SHARED_PREF, MODE_PRIVATE);
        gameView = new GameView(this, sharedPreferences, this);
        gameView.setZOrderOnTop(true);
        gameView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        gameView.setOnTouchListener(new Controller());

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