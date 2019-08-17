package x.com.hl_player.View;

import android.content.Context;
import android.view.ViewGroup;

import x.com.hl_player.tools.PlayerViewType;

public class HHPlayerView {
    public HHRenderView hhRenderView;

    public float getRotation() {
        return hhRenderView.getRenderView().getRotation();
    }

    public void setRotation(float rotation) {
        if (hhRenderView != null)
            hhRenderView.getRenderView().setRotation(rotation);
    }

    public int getWidth() {
        return (hhRenderView != null) ? hhRenderView.getRenderView().getWidth() : 0;
    }

    public int getHeight() {
        return (hhRenderView != null) ? hhRenderView.getRenderView().getHeight() : 0;
    }

    /**
     * 添加播放的view
     */
    public void addView(final Context context, final ViewGroup textureViewContainer, final int rotate,
                        final HHSurfaceStateListener gsySurfaceListener
//                        final MeasureHelper.MeasureFormVideoParamsListener videoParamsListener,
//                        final GSYVideoGLView.ShaderInterface effect, final float[] transform,
//                        final GSYVideoGLViewBaseRender customRender, int mode
    ) {
        switch (PlayerViewType.getRenderType()) {
            case PlayerViewType.SUFRACE:
                hhRenderView = HHSurfaceView.addSurfaceView(context, textureViewContainer, rotate,gsySurfaceListener);
                break;
            case PlayerViewType.TEXTURE:
//                hhRenderView = HHSurfaceView.addSurfaceView(context, textureViewContainer, rotate);
                break;
            case PlayerViewType.GLSURFACE:
//                hhRenderView = HHSurfaceView.addSurfaceView(context, textureViewContainer, rotate);
                break;
        }
//        if (PlayerViewType.getRenderType() == PlayerViewType.SUFRACE) {
//            hhRenderView = HHSurfaceView.addSurfaceView(context, textureViewContainer, rotate, gsySurfaceListener, videoParamsListener);
//        } else if (PlayerViewType.getRenderType() == PlayerViewType.GLSURFACE) {
//            hhRenderView = GSYVideoGLView.addGLView(context, textureViewContainer, rotate, gsySurfaceListener, videoParamsListener, effect, transform, customRender, mode);
//        } else {
//            hhRenderView = GSYTextureView.addTextureView(context, textureViewContainer, rotate, gsySurfaceListener, videoParamsListener);
//        }
    }

    public HHRenderView getHhRenderView() {
        return hhRenderView;
    }
}
