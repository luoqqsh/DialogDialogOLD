package com.xing.hhplayer.common.base;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.Surface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import x.com.hl_gl.BaseGLRenderer;
import x.com.hl_gl.shader.VideoShader;
import x.com.hl_gl.util.OpenGLUtils;
import x.com.hl_gl.util.ShaderUtil;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;

public class VideoGLRenderer extends BaseGLRenderer {
    private int mProgram;
    private int maPositionHandle;
    private int maTextureHandle;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int mTextureID[] = new int[2];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
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

        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID[0]);
        OpenGLUtils.checkGlError("glBindTexture mTextureID");

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        SurfaceTexture mSurface = new SurfaceTexture(mTextureID[0]);
        mSurface.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {

            }
        });

        Surface surface = new Surface(mSurface);

        sendSurfaceForPlayer(surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置大小，左下角x,y,宽，高
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        OpenGLUtils.clearColor();
    }
}
