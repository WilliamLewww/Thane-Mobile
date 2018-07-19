package com.williamlewww.thane.TEngine;

import android.graphics.PointF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class TPoint {
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

    float color[] = { 0.94f, 0.85f, 0.85f, 1.0f };

    public TPoint() {
        lineCoords = new ArrayList<>();
    }

    public TPoint(PointF position, float[] color) {
        lineCoords = new ArrayList<>();
        lineCoords.add(-position.x);
        lineCoords.add(-position.y);
        lineCoords.add(0.0f);

        this.color[0] = color[0];
        this.color[1] = color[1];
        this.color[2] = color[2];
        this.color[3] = color[3];

        initialize();
    }

    public void addPoint(float x, float y) {
        lineCoords.add(-x);
        lineCoords.add(-y);
        lineCoords.add(0.0f);

        VertexCount = lineCoords.size() / COORDS_PER_VERTEX;
        VertexStride = COORDS_PER_VERTEX * 4;

        ByteBuffer bb = ByteBuffer.allocateDirect(lineCoords.size() * 4);
        bb.order(ByteOrder.nativeOrder());

        VertexBuffer = bb.asFloatBuffer();

        float[] coordsArray = new float[lineCoords.size()];
        for (int z = 0; z < lineCoords.size(); z++) {
            coordsArray[z] = lineCoords.get(z);
        }

        VertexBuffer.put(coordsArray);
        VertexBuffer.position(0);
    }

    public void addPoint(PointF point) {
        lineCoords.add(-point.x);
        lineCoords.add(-point.y);
        lineCoords.add(0.0f);

        VertexCount = lineCoords.size() / COORDS_PER_VERTEX;
        VertexStride = COORDS_PER_VERTEX * 4;

        ByteBuffer bb = ByteBuffer.allocateDirect(lineCoords.size() * 4);
        bb.order(ByteOrder.nativeOrder());

        VertexBuffer = bb.asFloatBuffer();

        float[] coordsArray = new float[lineCoords.size()];
        for (int z = 0; z < lineCoords.size(); z++) {
            coordsArray[z] = lineCoords.get(z);
        }

        VertexBuffer.put(coordsArray);
        VertexBuffer.position(0);
    }

    public void initialize() {
        VertexCount = lineCoords.size() / COORDS_PER_VERTEX;
        VertexStride = COORDS_PER_VERTEX * 4;

        ByteBuffer bb = ByteBuffer.allocateDirect(lineCoords.size() * 4);
        bb.order(ByteOrder.nativeOrder());

        VertexBuffer = bb.asFloatBuffer();

        int vertexShader = TRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = TRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

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

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, VertexCount);

        GLES20.glDisableVertexAttribArray(PositionHandle);
    }
}