package x.com.hl_player.View;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import x.com.hl_player.TvListBean.TvView;
import x.com.hl_player.TypeBean.PlayerViewType;
import x.com.hl_player.tools.MeasureHelper;

public class HHPlayerView implements MeasureHelper.MeasureFormVideoParamsListener {
    private HHRenderView hhRenderView;
    private int videoWidth;
    private int videoHeight;

    /**
     * 添加播放的view
     */
    public void addView(final Context context, final ViewGroup textureViewContainer, final int rotate, TvView tvView,
                        final HHSurfaceStateListener surfaceStateListener
//                        final MeasureHelper.MeasureFormVideoParamsListener videoParamsListener,
//                        final GSYVideoGLView.ShaderInterface effect, final float[] transform,
//                        final GSYVideoGLViewBaseRender customRender, int mode
    ) {
        switch (tvView) {
            case HHSURFACE:
                hhRenderView = HHSurfaceView.addSurfaceView(context, textureViewContainer, rotate, surfaceStateListener, this);
                break;
            case HHTEXTURE:
                hhRenderView = HHTextureView.addTextureView(context, textureViewContainer, rotate, surfaceStateListener, this);
                break;
            case HHGLSURFACE:
                hhRenderView = HHGLSurfaceView.addGLSurfaceView(context, textureViewContainer, rotate, surfaceStateListener, this);
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
        videoWidth = width;
        videoHeight = height;
        changeTextureViewShowType();
    }

    public void setVideoScaleType(int scaleType) {
        if (hhRenderView != null) {
            PlayerViewType.setShowType(scaleType);
            changeTextureViewShowType();
        }
    }

    /**
     * 调整显示比例
     */
    private void changeTextureViewShowType() {
        if (hhRenderView.getRenderView() != null) {
            int params = getTextureParams();
            ViewGroup.LayoutParams layoutParams = hhRenderView.getRenderView().getLayoutParams();
            layoutParams.width = params;
            layoutParams.height = params;
            hhRenderView.getRenderView().setLayoutParams(layoutParams);
        }
    }

    private static int getTextureParams() {
        boolean typeChanged = (PlayerViewType.getShowType() != PlayerViewType.SCREEN_TYPE_DEFAULT);
        return (typeChanged) ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
    }

    static void addToParent(ViewGroup textureViewContainer, View render) {
        int params = getTextureParams();
        if (textureViewContainer instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(params, params);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            textureViewContainer.addView(render, layoutParams);
        } else if (textureViewContainer instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(params, params);
            layoutParams.gravity = Gravity.CENTER;
            textureViewContainer.addView(render, layoutParams);
        }
    }

    @Override
    public int getCurrentVideoWidth() {
        return videoWidth;
    }

    @Override
    public int getCurrentVideoHeight() {
        return videoHeight;
    }

    @Override
    public int getVideoSarNum() {
        return getWidth();
    }

    @Override
    public int getVideoSarDen() {
        return getHeight();
    }
}
