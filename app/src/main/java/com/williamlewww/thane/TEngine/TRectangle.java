package com.williamlewww.thane.TEngine;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TRectangle {

    public PointF position;
    public int width, height;

    public float angle = 0;

    private boolean rotation;

    private final int mProgram;

    private int mMVPMatrixHandle;
    private final String vertexShaderCode = "uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;" + "void main() {" + "  gl_Position = uMVPMatrix * vPosition;" + "}";
    private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;" + "void main() {" + "  gl_FragColor = vColor;" + "}";

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    static final int COORDS_PER_VERTEX = 3;
    float rectangleCoords[] = {
            -1.0f,  1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
             1.0f, -1.0f, 0.0f,
             1.0f,  1.0f, 0.0f
    };

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    public PointF getTopLeft() {
        return new PointF((float)((-width / 2) * Math.cos((-angle * Math.PI) / 180) - (height / 2) * Math.sin((-angle * Math.PI) / 180) + position.x),
                (float)((width / 2) * Math.sin((-angle * Math.PI) / 180) - (height / 2) * Math.cos((-angle * Math.PI) / 180) + position.y));
    }

    public PointF getTopRight() {
        return new PointF((float)((width / 2) * Math.cos((-angle * Math.PI) / 180) - (height / 2) * Math.sin((-angle * Math.PI) / 180) + position.x),
                (float)((-width / 2) * Math.sin((-angle * Math.PI) / 180) - (height / 2) * Math.cos((-angle * Math.PI) / 180) + position.y));
    }

    public PointF getBottomLeft() {
        return new PointF((float)((-width / 2) * Math.cos((-angle * Math.PI) / 180) - (-height / 2) * Math.sin((-angle * Math.PI) / 180) + position.x),
                (float)((width / 2) * Math.sin((-angle * Math.PI) / 180) - (-height / 2) * Math.cos((-angle * Math.PI) / 180) + position.y));
    }

    public PointF getBottomRight() {
        return new PointF((float)((width / 2) * Math.cos((-angle * Math.PI) / 180) - (-height / 2) * Math.sin((-angle * Math.PI) / 180) + position.x),
                (float)((-width / 2) * Math.sin((-angle * Math.PI) / 180) - (-height / 2) * Math.cos((-angle * Math.PI) / 180) + position.y));
    }

    public TRectangle(PointF position, int width, int height, boolean rotation) {
        this.position = position;
        this.width = width;
        this.height = height;

        this.rotation = rotation;

        for (int x = 0; x < rectangleCoords.length; x += 3) {
            rectangleCoords[x] *= width / 2;
            rectangleCoords[x] -= width / 2;
        }
        for (int x = 1; x < rectangleCoords.length; x += 3) {
            rectangleCoords[x] *= height / 2;
            rectangleCoords[x] -= height / 2;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(rectangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(rectangleCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = TRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = TRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = rectangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    float[] matrix = new float[16];

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        matrix = mvpMatrix.clone();
        Matrix.translateM(matrix,0, -position.x, -position.y, 0);
        if (rotation) {
            Matrix.rotateM(matrix, 0, angle, 0, 0, 1);
            Matrix.translateM(matrix,0, width / 2, height / 2, 0);
        }

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}