package x.com.hl_player;


import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import x.com.hl_base.HOnListener;
import x.com.hl_log.ViseLog;
import x.com.hl_player.TypeBean.PlayerPrePareStateType;
import x.com.hl_player.TypeBean.PlayerStateType;
import x.com.hl_player.base.HHPlayerCore;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer2 {

    private HHPlayerCore hhPlayerCore;//播放器内核
    private View mContainer;

    public HHPlayer2(HHPlayerCore hhPlayerCore) {
        this.hhPlayerCore = hhPlayerCore;
    }

    public HHPlayer2(HHPlayerCore hhPlayerCore, SurfaceView surface) {
        this.hhPlayerCore = hhPlayerCore;
//        hhPlayerCore.initView(surface);
    }

    public HHPlayer2(HHPlayerCore hhPlayerCore, TextureView texture) {
        this.hhPlayerCore = hhPlayerCore;
//        hhPlayerCore.initView(texture);
    }

    public HHPlayer2(HHPlayerCore hhPlayerCore, Surface surface) {
        this.hhPlayerCore = hhPlayerCore;
        hhPlayerCore.initView(surface);
    }

    public void prepare(String path) {
        hhPlayerCore.prepare(path);
    }

    public void setLooping(boolean looping) {
        hhPlayerCore.setLooping(looping);
    }

    public void setMute(boolean mute) {
        setVolume(mute ? 0 : 1);
    }

    public void setVolume(float volume) {
        if (volume >= 0 && volume <= 1) {
            hhPlayerCore.setVolume(volume);
        } else {
            hhPlayerCore.setVolume(1);
            ViseLog.e("volume超范围：" + volume);
        }
    }

    public float getVolume() {
        return hhPlayerCore.volume;
    }

    public void start() {
        if (hhPlayerCore.isPrepared && hhPlayerCore.isPlayControl()) {
            hhPlayerCore.start();
            hhPlayerCore.getCurrentStart();
            hhPlayerCore.mPlayerState = PlayerStateType.PLAYING;
            Log.e(hhPlayerCore.TAG, "doStart");
        }
    }

    public void pause() {
        if (hhPlayerCore.isPlayControl() && hhPlayerCore.isMediaPlaying()) {
            hhPlayerCore.pause();
            hhPlayerCore.mPlayerState = PlayerStateType.PAUSE;
            Log.e(hhPlayerCore.TAG, "doPause");
        }
    }

    public void reset() {
        hhPlayerCore.reset();
        hhPlayerCore.getCurrentStop();
        hhPlayerCore.mPlayerState = PlayerStateType.IDLE;
    }

    public void stop() {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.stop();
            hhPlayerCore.isPrepared = false;
            hhPlayerCore.mPlayerState = PlayerStateType.IDLE;
            hhPlayerCore.getCurrentStop();
            Log.d(hhPlayerCore.TAG, "doStop");
        }
    }

    //单位毫秒
    public void seekTo(long position) {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.playerPrePareState = PlayerPrePareStateType.PREPARING;
            hhPlayerCore.seekTo(position);
            Log.d(hhPlayerCore.TAG, "seekTo");
        }
    }

    //0为暂停，1为正常。。。
    public void setSpeed(float speed) {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.setSpeed(speed);
            Log.d(hhPlayerCore.TAG, "setSpeed");
        }
    }

    public void release() {
        hhPlayerCore.release();
        stop();
        hhPlayerCore.getCurrentStop();
        hhPlayerCore.mPlayerState = PlayerStateType.NONE;
        hhPlayerCore.isPrepared = false;
        Log.d(hhPlayerCore.TAG, "doRelease");
    }

    public Object getMediaPath() {
        return hhPlayerCore.getMediaPath();
    }

    public boolean isPlaying() {
        return hhPlayerCore.isMediaPlaying();
    }

    public long getCurrentPosition() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getCurrentPosition();
        }
        return -1;
    }

    public long getMediaDuration() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getMediaDuration();
        }
        return -1;
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        hhPlayerCore.setPreparedListener(preparedListener);
    }

    public void setCompletionListener(final HOnListener<Object> completionListener) {
        hhPlayerCore.setCompletionListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                Log.e(hhPlayerCore.TAG, "play Completion");
                hhPlayerCore.mPlayerState = PlayerStateType.PLAYBACK_COMPLETED;
                completionListener.onListen(o);
            }
        });
    }

    public void setProgessListener(HOnListener<Long> progessListener) {
        hhPlayerCore.setProgessListener(progessListener);
    }

    public void setOnBufferingUpdateListener(HOnListener<Integer> onBufferingUpdateListener) {
        hhPlayerCore.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    public PlayerStateType getPlayerState() {
        return hhPlayerCore.mPlayerState;
    }

    public void fullScreen() {

    }

/*

    public void fullScreen() {
//        if (mTarget.getWindowType() != VideoView.WindowType.FULLSCREEN) {
//            //new VideoView
//            VideoView videoView = new VideoView(getContext());
//            //set parent
//            videoView.setParentVideoView(mTarget);
//            videoView.setUp(mTarget.getDataSourceObject(), VideoView.WindowType.FULLSCREEN, mTarget.getData());
//            videoView.setControlPanel(new ControlPanel(getContext()));
//            //start fullscreen0
//            videoView.startFullscreen(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//            //MediaPlayerManager.instance().startFullscreen(videoView, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        }
    }

    */
/**
 * 全屏，将mContainer(内部包含mTextureView和mController)从当前容器中移除，并添加到android.R.content中.
 * 切换横屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
 * 以避免Activity重新走生命周期
 *//*

    public void enterFullScreen(Context mContext) {
        if (mCurrentMode == PlayerMode.MODE_FULL_SCREEN) return;

        // 隐藏ActionBar、状态栏，并横屏
        hl_view.hideActionBar(mContext);
        hl_context.scanForActivity(mContext)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ViewGroup contentView = hl_context.scanForActivity(mContext)
                .findViewById(android.R.id.content);
        if (mCurrentMode == PlayerMode.MODE_TINY_WINDOW) {
            contentView.removeView(mContainer);
        } else {
//            this.removeView(mContainer);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(mContainer, params);

        mCurrentMode = PlayerMode.MODE_FULL_SCREEN;
    }

    */
/**
 * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
 * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
 * 以避免Activity重新走生命周期.
 *
 * @return true退出全屏.
 *//*

    public boolean exitFullScreen(Context mContext) {
        if (mCurrentMode == PlayerMode.MODE_FULL_SCREEN) {
            hl_view.showActionBar(mContext);
            hl_context.scanForActivity(mContext)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ViewGroup contentView = hl_context.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
//            this.addView(mContainer, params);

            mCurrentMode = PlayerMode.MODE_NORMAL;
            return true;
        }
        return false;
    }

    */
/**
 * 进入小窗口播放，小窗口播放的实现原理与全屏播放类似。
 *//*

    public void enterTinyWindow(Context mContext) {
        if (mCurrentMode == PlayerMode.MODE_TINY_WINDOW) return;
//        this.removeView(mContainer);

        ViewGroup contentView = hl_context.scanForActivity(mContext)
                .findViewById(android.R.id.content);
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (hl_screen.getScreenWidth(mContext) * 0.6f),
                (int) (hl_screen.getScreenWidth(mContext) * 0.6f * 9f / 16f));
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.rightMargin = (int) hl_screen.dp2Px(mContext, 8f);
        params.bottomMargin = (int) hl_screen.dp2Px(mContext, 8f);

        contentView.addView(mContainer, params);

        mCurrentMode = PlayerMode.MODE_TINY_WINDOW;
    }

    */
/**
 * 退出小窗口播放
 *//*

    public boolean exitTinyWindow(Context mContext) {
        if (mCurrentMode == PlayerMode.MODE_TINY_WINDOW) {
            ViewGroup contentView = hl_context.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
//            this.addView(mContainer, params);

            mCurrentMode = PlayerMode.MODE_NORMAL;
            return true;
        }
        return false;
    }
*/
}
