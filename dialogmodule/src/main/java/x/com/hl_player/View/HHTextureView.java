package x.com.hl_player.View;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import x.com.hl_player.tools.ViewUtil;

public class HHTextureView extends TextureView implements TextureView.SurfaceTextureListener, HHRenderView {
    private HHSurfaceStateListener surfaceListener;
    private Surface mSurface;
    private SurfaceTexture mSaveTexture;

    public HHTextureView(Context context) {
        super(context);
    }

    public HHTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HHTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static HHRenderView addTextureView(Context context, ViewGroup textureViewContainer, int rotate, HHSurfaceStateListener stateListener) {
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        HHTextureView showTextureView = new HHTextureView(context);
        showTextureView.setHHSurfaceStateListener(stateListener);
        showTextureView.setRotation(rotate);
        ViewUtil.addToParent(textureViewContainer, showTextureView);
        return showTextureView;
    }

    @Override
    public void setHHSurfaceStateListener(HHSurfaceStateListener surfaceListener) {
        setSurfaceTextureListener(this);
        this.surfaceListener = surfaceListener;
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
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSaveTexture == null) {
            mSaveTexture = surfaceTexture;
            mSurface = new Surface(surfaceTexture);
            if (surfaceListener != null) {
                surfaceListener.onSurfaceAvailable(mSurface);
            }
        } else {
            setSurfaceTexture(mSaveTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        if (surfaceListener != null) {
            surfaceListener.onSurfaceSizeChanged(mSurface, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (surfaceListener != null) {
            surfaceListener.onSurfaceDestroyed(mSurface);
        }
        return (mSaveTexture == null);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        if (surfaceListener != null) {
            surfaceListener.onSurfaceUpdated(mSurface);
        }
    }
}
