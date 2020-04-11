package com.xing.hhplayer.common.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import com.xing.hhplayer.common.base.VideoGLRenderer;
import com.xing.hhplayer.common.tools.MeasureHelper;

import x.com.hl_base.interfaces.HOnListener;
import x.com.hl_gl.shader.ShaderInterface;
import x.com.hl_log.ViseLog;

public class HHGLSurfaceView extends GLSurfaceView {
    private HHSurfaceStateListener surfaceListener;
    private MeasureHelper measureHelper;
    private VideoGLRenderer videoGLRenderer;

    public HHGLSurfaceView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(3);
        videoGLRenderer = new VideoGLRenderer();
        setRenderer(videoGLRenderer);
        //自动调用，每秒60次
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //按需调用
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setSurfaceCreateListener(HOnListener<Surface> surfaceCreateListener) {
        videoGLRenderer.setSurfaceCreateListener(surfaceCreateListener);
    }

    public Surface getVideoSurface() {
        if (videoGLRenderer != null) {
            return videoGLRenderer.getSurface();
        }
        return null;
    }

    public void takeBitmap(HOnListener<Bitmap> takeBitmapListener) {
        if (videoGLRenderer != null && takeBitmapListener != null) {
            videoGLRenderer.takeBitmap(takeBitmapListener);
        }
    }

    public void setEffect(ShaderInterface shaderEffect) {
        if (videoGLRenderer != null) {
            videoGLRenderer.setEffect(shaderEffect);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ViseLog.d("onresume");
        requestLayout();
    }

    @Override
    public void onPause() {
        super.onPause();
        ViseLog.d("onPause");
        requestLayout();
    }

}
