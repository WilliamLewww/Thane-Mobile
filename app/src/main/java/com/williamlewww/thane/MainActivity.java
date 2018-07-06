package com.williamlewww.thane;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.williamlewww.thane.TEngine.TSurface;

public class MainActivity extends AppCompatActivity {

    public static int action_state;

    TSurface tSurface;

    float initialY, initialX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tSurface = new TSurface(this);
        setContentView(tSurface);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() < tSurface.getHeight() / 2) { action_state = -1; }
            if (event.getY() > tSurface.getHeight() / 2) { action_state = 1; }

            initialX = event.getX();
            initialY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (action_state < 0) {
                if (initialY - event.getY() > 50) {
                    if (event.getX() - initialX > 50) {
                        action_state = -3;
                    }else {
                        if (initialX - event.getX() > 50) {
                            action_state = -2;
                        }
                        else {
                            action_state = -4;
                        }
                    }
                }
            }

            if (action_state > 0) {
                if (event.getY() - initialY > 50) {
                    if (event.getX() - initialX > 50) {
                        action_state = 3;
                    }else {
                        if (initialX - event.getX() > 50) {
                            action_state = 2;
                        }
                        else {
                            action_state = 4;
                        }
                    }
                }
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) { action_state = 0; }

        return super.onTouchEvent(event);
    }
}
