package x.com.hl_player.View;


import android.view.View;

/**
 * Created by guoshuyu on 2018/1/29.
 */

public interface HHRenderView {

    //Surface变化监听，必须
    void setHHSurfaceStateListener(HHSurfaceStateListener surfaceListener);

    /**
     * 当前view宽高，必须
     */
    int getSizeW();

    int getSizeH();

    //方向
    float getRotation();

    /**
     * 实现该接口的view，必须
     */
    View getRenderView();

    /**
     * 渲染view通过MeasureFormVideoParamsListener获取视频的相关参数，必须
     */
//    void setVideoParamsListener(MeasureHelper.MeasureFormVideoParamsListener listener);

    /**
     * 截图
     */
//    void taskShotPic(GSYVideoShotListener gsyVideoShotListener, boolean shotHigh);

    /**
     * 保存当前帧
     */
//    void saveFrame(final File file, final boolean high, final GSYVideoShotSaveListener gsyVideoShotSaveListener);

    /**
     * 获取当前画面的bitmap，没有返回空
     */
//    Bitmap initCover();

    /**
     * 获取当前画面的高质量bitmap，没有返回空
     */
//    Bitmap initCoverHigh();

//    void onRenderResume();

//    void onRenderPause();

//    void releaseRenderAll();

//    void setRenderMode(int mode);

//    void setRenderTransform(Matrix transform);

//    void setGLRenderer(GSYVideoGLViewBaseRender renderer);

//    void setGLMVPMatrix(float[] MVPMatrix);

//    void setGLEffectFilter(GSYVideoGLView.ShaderInterface effectFilter);

}
