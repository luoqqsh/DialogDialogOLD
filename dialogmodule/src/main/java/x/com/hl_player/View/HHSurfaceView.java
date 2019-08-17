package x.com.hl_player.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import x.com.hl_log.ViseLog;
import x.com.hl_player.tools.ViewUtil;

public class HHSurfaceView extends SurfaceView implements SurfaceHolder.Callback2, HHRenderView {
    private HHSurfaceStateListener surfaceListener;

    public HHSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public HHSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HHSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public static HHRenderView addSurfaceView(Context context, ViewGroup textureViewContainer, int rotate, HHSurfaceStateListener stateListener) {
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        HHSurfaceView showSurfaceView = new HHSurfaceView(context);
        showSurfaceView.setIGSYSurfaceListener(stateListener);
//        showSurfaceView.setVideoParamsListener(videoParamsListener);
        showSurfaceView.setRotation(rotate);
        ViewUtil.addToParent(textureViewContainer, showSurfaceView);
        return showSurfaceView;
    }

    private void init(Context context) {
        getHolder().addCallback(this);
    }


    @Override
    public HHSurfaceStateListener getIGSYSurfaceListener() {
        return surfaceListener;
    }

    @Override
    public void setIGSYSurfaceListener(HHSurfaceStateListener surfaceListener) {
        this.surfaceListener = surfaceListener;
    }

    @Override
    public int getSizeH() {
        return getSizeH();
    }

    @Override
    public int getSizeW() {
        return getSizeW();
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
    public void surfaceChanged(SurfaceHolder surfaceHolder, int width, int height, int i2) {
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
