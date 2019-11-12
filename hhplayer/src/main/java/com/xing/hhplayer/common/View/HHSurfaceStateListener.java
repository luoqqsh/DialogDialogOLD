package com.xing.hhplayer.common.View;

import android.view.Surface;

/**
 * Surface 状态变化回调
 * Created by luoqqsh on 2019/8/18.
 */

public interface HHSurfaceStateListener {
    //用户可见，创建成功
    void onSurfaceAvailable(Surface surface);

    //画布改变
    void onSurfaceSizeChanged(Surface surface, int width, int height);

    //画布销毁
    boolean onSurfaceDestroyed(Surface surface);

    //画布更新
    void onSurfaceUpdated(Surface surface);
}
