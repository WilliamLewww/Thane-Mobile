package com.williamlewww.thane.TEngine;

import android.graphics.PointF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class TLine {
    private FloatBuffer VertexBuffer;

    private final String vertexShaderCode = "uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;" + "void main() {" + "  gl_Position = uMVPMatrix * vPosition;" + "}";
    private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;" + "void main() {" + "  gl_FragColor = vColor;" + "}";


    protected int GlProgram;
    protected int PositionHandle;
    protected int ColorHandle;
    protected int MVPMatrixHandle;

    static final int COORDS_PER_VERTEX = 3;
    int VertexCount;
    int VertexStride;
    public List<Float> lineCoords;

    float color[] = { 0.0f, 0.0f, 1.0f, 1.0f };

    public void setColor(float r, float g, float b) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }

    public PointF getPointReverse(int index) {
        return new PointF(-lineCoords.get(index * 3), -lineCoords.get((index * 3) + 1));
    }

    public PointF getPoint(int index) {
        return new PointF(lineCoords.get(index * 3), lineCoords.get((index * 3) + 1));
    }

    public TLine() {
        lineCoords = new ArrayList<>();
    }

    public void addPoint(float x, float y) {
        lineCoords.add(x);
        lineCoords.add(y);
        lineCoords.add(0.0f);
    }

    public void initialize() {
        VertexCount = lineCoords.size() / COORDS_PER_VERTEX;
        VertexStride = COORDS_PER_VERTEX * 4;

        ByteBuffer bb = ByteBuffer.allocateDirect(lineCoords.size() * 4);
        bb.order(ByteOrder.nativeOrder());

        VertexBuffer = bb.asFloatBuffer();

        int vertexShader = TRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = TRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        float[] coordsArray = new float[lineCoords.size()];
        for (int z = 0; z < lineCoords.size(); z++) {
            coordsArray[z] = lineCoords.get(z);
        }

        VertexBuffer.put(coordsArray);
        VertexBuffer.position(0);

        GlProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(GlProgram, vertexShader);
        GLES20.glAttachShader(GlProgram, fragmentShader);
        GLES20.glLinkProgram(GlProgram);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(GlProgram);

        PositionHandle = GLES20.glGetAttribLocation(GlProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(PositionHandle);
        GLES20.glVertexAttribPointer(PositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VertexStride, VertexBuffer);

        ColorHandle = GLES20.glGetUniformLocation(GlProgram, "vColor");

        GLES20.glUniform4fv(ColorHandle, 1, color, 0);

        MVPMatrixHandle = GLES20.glGetUniformLocation(GlProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, VertexCount);

        GLES20.glDisableVertexAttribArray(PositionHandle);
    }

    public void drawStrip(float[] mvpMatrix) {
        GLES20.glUseProgram(GlProgram);

        PositionHandle = GLES20.glGetAttribLocation(GlProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(PositionHandle);
        GLES20.glVertexAttribPointer(PositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VertexStride, VertexBuffer);

        ColorHandle = GLES20.glGetUniformLocation(GlProgram, "vColor");

        GLES20.glUniform4fv(ColorHandle, 1, color, 0);

        MVPMatrixHandle = GLES20.glGetUniformLocation(GlProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, VertexCount);

        GLES20.glDisableVertexAttribArray(PositionHandle);
    }
}