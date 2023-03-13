package com.ut3.arenasurvivor.game.logic.utils;

import android.graphics.Rect;

import com.ut3.arenasurvivor.R;
import com.ut3.arenasurvivor.entities.impl.Platform;

public class PlatformsSpawner {

    private int windowWidth;
    private int windowHeight;

    private final int PLATFORM_MAX_WIDTH  = 150;
    private final int PLATFORM_MIN_WIDTH = 70;

    private double platformMaxHeight;
    private double platformMinHeight;

    private final int PLATFORM_HEIGHT = 30;



    public PlatformsSpawner(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;


        // Calculate the good height area to apply
        platformMaxHeight = height * 0.45;
        platformMinHeight = height * 0.55;


    }


    public Platform createPlatformWithRandomPos() {

        // Generate random platform width
        int width = (int) (Math.random() *
                (PLATFORM_MAX_WIDTH - PLATFORM_MIN_WIDTH) + PLATFORM_MIN_WIDTH
            );

        // Generate top left coordinates
        int x = (int) (Math.random() * (windowWidth - width));
        int y = (int) (Math.random() * (platformMaxHeight - platformMinHeight) + platformMinHeight);

        // Create rect
        Rect coordinates = new Rect(x, y, x + width, y + PLATFORM_HEIGHT);

        return new Platform(coordinates);
    }

}
