package com.xing.hhplayer.common.View;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import com.xing.hhplayer.common.base.VideoGLRenderer;
import com.xing.hhplayer.common.tools.MeasureHelper;

import x.com.hl_base.interfaces.HOnListener;

public class HHGLSurfaceView extends GLSurfaceView {
    private HHSurfaceStateListener surfaceListener;
    private MeasureHelper measureHelper;
    private VideoGLRenderer videoGLRenderer;

    public HHGLSurfaceView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
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

    @Override
    public void onResume() {
        super.onResume();
        
    }
}
