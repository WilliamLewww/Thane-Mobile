package com.williamlewww.thane.TMain;

import android.graphics.PointF;

import com.williamlewww.thane.MainActivity;
import com.williamlewww.thane.TEngine.TRectangle;

public class Board {
    TRectangle rectangle;

    public Board() {
        rectangle = new TRectangle(new PointF(0,0),100,50, true);
    }

    public void update(float deltaTimeS) {
        if (MainActivity.action_state == 1) {
            rectangle.angle -= 1;
        }
        if (MainActivity.action_state == 2) {
            rectangle.angle += 1;
        }

        moveInDirection(getDirection());
    }

    private PointF getDirection() {
        PointF direction = new PointF((float)Math.cos((rectangle.angle * Math.PI) / 180), (float)Math.sin((rectangle.angle * Math.PI) / 180));
        return direction;
    }

    private void moveInDirection(PointF direction) {
        rectangle.position.x += direction.x * 2.5f;
        rectangle.position.y += direction.y * 2.5f;
    }

    public void draw(float[] mvpMatrix) {
        rectangle.draw(mvpMatrix);
    }
}
