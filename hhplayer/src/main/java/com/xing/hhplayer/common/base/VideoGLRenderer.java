package com.xing.hhplayer.common.base;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import x.com.hl_base.interfaces.HOnListener;
import x.com.hl_gl.BaseGLRenderer;
import x.com.hl_gl.shader.VideoShader;
import x.com.hl_gl.util.OpenGLUtils;
import x.com.hl_gl.util.ShaderUtil;

public class VideoGLRenderer extends BaseGLRenderer {
    private int mProgram;
    private int maPositionHandle;
    private int maTextureHandle;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int mTextureID[] = new int[2];
    private FloatBuffer mTriangleVertices;
    protected float[] mMVPMatrix = new float[16];
    protected float[] mSTMatrix = new float[16];

    private HOnListener<Surface> surfaceCreateListener;
    private Surface surface;
    private boolean mUpdateSurface;
    private SurfaceTexture mSurface;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mTriangleVertices = ByteBuffer.allocateDirect(
                mTriangleVerticesData.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangleVertices.put(mTriangleVerticesData).position(0);
        Matrix.setIdentityM(mSTMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix, 0);
        mProgram = ShaderUtil.createGlProgramByString(VideoShader.mVideoVertexShader, VideoShader.mVideoShader);
        if (mProgram == 0) {
            return;
        }
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        OpenGLUtils.checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        OpenGLUtils.checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        OpenGLUtils.checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }

        muSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
        OpenGLUtils.checkGlError("glGetUniformLocation uSTMatrix");
        if (muSTMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uSTMatrix");
        }

        //生成纹理
        GLES20.glGenTextures(2, mTextureID, 0);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID[0]);
        OpenGLUtils.checkGlError("glBindTexture mTextureID");

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        mSurface = new SurfaceTexture(mTextureID[0]);
        mSurface.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mUpdateSurface = true;
            }
        });
        surface = new Surface(mSurface);
        if (surfaceCreateListener != null) {
            surfaceCreateListener.onListen(surface);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置大小，左下角x,y,宽，高
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (mUpdateSurface) {
                mSurface.updateTexImage();
                //旋转
                mSurface.getTransformMatrix(mSTMatrix);
                mUpdateSurface = false;
            }
        }
        OpenGLUtils.clearColor();

        GLES20.glUseProgram(mProgram);
        OpenGLUtils.checkGlError("glUseProgram");
        //bindDrawFrameTexture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID[0]);
        initPointerAndDraw();
        GLES20.glFinish();
    }

    public void setSurfaceCreateListener(HOnListener<Surface> surfaceCreateListener) {
        this.surfaceCreateListener = surfaceCreateListener;
    }

    public Surface getSurface() {
        return surface;
    }

    protected void initPointerAndDraw() {
        mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
        OpenGLUtils.checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        OpenGLUtils.checkGlError("glEnableVertexAttribArray maPositionHandle");

        mTriangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(maTextureHandle, 3, GLES20.GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
        OpenGLUtils.checkGlError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(maTextureHandle);
        OpenGLUtils.checkGlError("glEnableVertexAttribArray maTextureHandle");

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        OpenGLUtils.checkGlError("glDrawArrays");
    }
}
