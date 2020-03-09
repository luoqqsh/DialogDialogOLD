package com.xing.hhplayer.common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.tools.MeasureHelper;

public class HHGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback2, HHRenderView {
    private HHSurfaceStateListener surfaceListener;
    private MeasureHelper measureHelper;

    public HHGLSurfaceView(Context context) {
        super(context);
        setId(R.id.video_normal_id);
    }

    public HHGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HHGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static HHRenderView addGLSurfaceView(Context context, ViewGroup textureViewContainer, int rotate, HHSurfaceStateListener stateListener, MeasureHelper.MeasureFormVideoParamsListener listener) {
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        HHGLSurfaceView showSurfaceView = new HHGLSurfaceView(context);
        showSurfaceView.setHHSurfaceStateListener(stateListener, listener);
        showSurfaceView.setRotation(rotate);
        HHPlayerView.addToParent(textureViewContainer, showSurfaceView);
        return showSurfaceView;
    }

    @Override
    public void setHHSurfaceStateListener(HHSurfaceStateListener surfaceListener, MeasureHelper.MeasureFormVideoParamsListener listener) {
        getHolder().addCallback(this);
        this.surfaceListener = surfaceListener;
        measureHelper = new MeasureHelper(this, listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureHelper.prepareMeasure(widthMeasureSpec, heightMeasureSpec, (int) getRotation());
        setMeasuredDimension(measureHelper.getMeasuredWidth(), measureHelper.getMeasuredHeight());
    }

    @Override
    public int getSizeH() {
        return getHeight();
    }

    @Override
    public int getSizeW() {
        return getWidth();
    }

    @Override
    public View getRenderView() {
        return this;
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (surfaceListener != null) {
            surfaceListener.onSurfaceAvailable(surfaceHolder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        if (surfaceListener != null) {
            surfaceListener.onSurfaceSizeChanged(surfaceHolder.getSurface(), width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (surfaceListener != null) {
            surfaceListener.onSurfaceDestroyed(surfaceHolder.getSurface());
        }
    }
}
