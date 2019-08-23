package x.com.hl_player.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import x.com.hl_log.ViseLog;
import x.com.hl_player.TypeBean.ScaleType;
import x.com.hl_player.tools.MeasureHelper;
import x.com.hl_player.tools.ViewUtil;

public class HHSurfaceView extends SurfaceView implements SurfaceHolder.Callback2, HHRenderView, MeasureHelper.MeasureFormVideoParamsListener {
    private HHSurfaceStateListener surfaceListener;
    private MeasureHelper measureHelper;
    private ScaleType scaleType = ScaleType.DEFAULT;

    public HHSurfaceView(Context context) {
        super(context);
    }

    public HHSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HHSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static HHRenderView addSurfaceView(Context context, ViewGroup textureViewContainer, int rotate, HHSurfaceStateListener stateListener) {
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        HHSurfaceView showSurfaceView = new HHSurfaceView(context);
        showSurfaceView.setHHSurfaceStateListener(stateListener);
        showSurfaceView.setRotation(rotate);
        ViewUtil.addToParent(textureViewContainer, showSurfaceView);

        return showSurfaceView;
    }

    @Override
    public void setHHSurfaceStateListener(HHSurfaceStateListener surfaceListener) {
        getHolder().addCallback(this);
        this.surfaceListener = surfaceListener;
        measureHelper = new MeasureHelper(this, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViseLog.d(widthMeasureSpec + "  " + heightMeasureSpec);
        measureHelper.prepareMeasure(widthMeasureSpec, heightMeasureSpec, (int) getRotation());
        setMeasuredDimension(measureHelper.getMeasuredWidth(), measureHelper.getMeasuredHeight());
    }

    /**
     * 获取布局参数
     *
     * @return
     */
    protected int getTextureParams() {
        boolean typeChanged = (scaleType != ScaleType.DEFAULT);
        return (typeChanged) ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /**
     * 调整TextureView去适应比例变化
     */
    @Override
    public void changeTextureViewShowType() {
        int params = getTextureParams();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = params;
        layoutParams.height = params;
        ViseLog.d(layoutParams.width);
        ViseLog.d(layoutParams.height);
        setLayoutParams(layoutParams);
        requestLayout();
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

    @Override
    public int getCurrentVideoWidth() {
        return getWidth();
    }

    @Override
    public int getCurrentVideoHeight() {
        return getHeight();
    }

    @Override
    public int getVideoSarNum() {
        return getSizeW();
    }

    @Override
    public int getVideoSarDen() {
        return getSizeH();
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
    }
}
