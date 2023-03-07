package com.ut3.arenasurvivor.entities;
import android.graphics.Rect;

import java.util.logging.Logger;

public interface Collidable {

    public boolean detectCollision(Rect dangerHitBox);

}
