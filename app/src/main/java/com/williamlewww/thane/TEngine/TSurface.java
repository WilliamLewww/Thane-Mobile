package com.williamlewww.thane.TEngine;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class TSurface extends GLSurfaceView {

    private final TRenderer mRenderer;

    public TSurface(Context context){
        super(context);

        setEGLContextClientVersion(2);

        mRenderer = new TRenderer();
        setRenderer(mRenderer);
    }
}