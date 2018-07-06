package com.williamlewww.thane.TEngine;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Trace;

import com.williamlewww.thane.TMain.Board;
import com.williamlewww.thane.TMain.Track;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TRenderer implements GLSurfaceView.Renderer {
    Board board;
    Track track;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private long frameStart = 0, frameEnd = 0, deltaTime = 0;

    public static int screenWidth, screenHeight;

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        board = new Board();
        track = new Track();
        track.initialize();

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);

        screenWidth = i;
        screenHeight = i1;

        float ratio = (float) i / i1;
        Matrix.frustumM(mProjectionMatrix, 0, -i/2, i/2, -i1/2, i1/2, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        frameStart = System.nanoTime();
        update();
        render();
        frameEnd = System.nanoTime();
        deltaTime = frameEnd - frameStart;
    }

    private void update() {
        float deltaTimeS = (float)deltaTime / 100000000;
        board.update(deltaTimeS);
    }

    private void render() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.translateM(mViewMatrix, 0, board.rectangle.position.x, board.rectangle.position.y, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        board.draw(mMVPMatrix);
        track.draw(mMVPMatrix);
    }
}
