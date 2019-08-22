package x.com.hl_player.View;

import android.content.Context;
import android.view.ViewGroup;

import x.com.hl_log.ViseLog;
import x.com.hl_player.TvListBean.TypeView;
import x.com.hl_player.TypeBean.ScaleType;

public class HHPlayerView {
    private HHRenderView hhRenderView;

    /**
     * 添加播放的view
     */
    public void addView(final Context context, final ViewGroup textureViewContainer, final int rotate, TypeView typeView,
                        final HHSurfaceStateListener surfaceStateListener
//                        final MeasureHelper.MeasureFormVideoParamsListener videoParamsListener,
//                        final GSYVideoGLView.ShaderInterface effect, final float[] transform,
//                        final GSYVideoGLViewBaseRender customRender, int mode
    ) {
        switch (typeView) {
            case HHSURFACE:
                hhRenderView = HHSurfaceView.addSurfaceView(context, textureViewContainer, rotate, surfaceStateListener);
                break;
            case HHTEXTURE:
                hhRenderView = HHTextureView.addTextureView(context, textureViewContainer, rotate, surfaceStateListener);
                break;
            case HHGLSURFACE:
//                hhRenderView = HHSurfaceView.addSurfaceView(context, textureViewContainer, rotate);
                break;
        }
    }

    public float getRotation() {
        return (hhRenderView != null) ? hhRenderView.getRotation() : 0;
    }

    public int getWidth() {
        return (hhRenderView != null) ? hhRenderView.getSizeW() : 0;
    }

    public int getHeight() {
        return (hhRenderView != null) ? hhRenderView.getSizeH() : 0;
    }

    public void setVideoSize(int width, int height) {
        ViseLog.d(width + "  " + height);

    }

    public void setVideoScaleType(ScaleType scaleType) {
        if (hhRenderView != null) {
            hhRenderView.setScaleType(scaleType);
            hhRenderView.changeTextureViewShowType();
        }
    }
}
