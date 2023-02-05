package com.ut3.arenasurvivor;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.ut3.arenasurvivor.entities.impl.Projectile;

public class GameController {

    public static void main(String args[]) {
        Projectile projectile = new Projectile("ProjectileA",
                new Rect(10, 10, 10, 10));

        projectile.draw(new Canvas());
    }


}
