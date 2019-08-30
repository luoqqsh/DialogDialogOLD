package x.com.hl_player.View;


import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by guoshuyu on 2018/1/29.
 */

public interface HHGLRenderView extends HHRenderView{
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
    Bitmap initCover();

    /**
     * 获取当前画面的高质量bitmap，没有返回空
     */
    Bitmap initCoverHigh();

    void onRenderResume();

    void onRenderPause();

    void releaseRenderAll();

    void setRenderMode(int mode);

    void setRenderTransform(Matrix transform);

//    void setGLRenderer(GSYVideoGLViewBaseRender renderer);

    void setGLMVPMatrix(float[] MVPMatrix);

//    void setGLEffectFilter(GSYVideoGLView.ShaderInterface effectFilter);

}
