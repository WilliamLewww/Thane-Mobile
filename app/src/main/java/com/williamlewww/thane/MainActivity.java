package com.williamlewww.thane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.williamlewww.thane.TEngine.TSurface;

public class MainActivity extends AppCompatActivity {

    TSurface tSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tSurface = new TSurface(this);
        setContentView(tSurface);
    }
}
