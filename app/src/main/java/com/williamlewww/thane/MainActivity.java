package com.williamlewww.thane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.williamlewww.thane.TEngine.TSurface;

public class MainActivity extends AppCompatActivity {

    public static int action_state;

    TSurface tSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tSurface = new TSurface(this);
        setContentView(tSurface);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() <= tSurface.getHeight() / 2) { action_state = 1; }
            if (event.getY() > tSurface.getHeight() / 2) { action_state = 2; }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) { action_state = -1; }

        return super.onTouchEvent(event);
    }
}
