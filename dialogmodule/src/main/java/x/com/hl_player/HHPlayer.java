package x.com.hl_player;

import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import x.com.hl_base.HOnListener;
import x.com.hl_log.ViseLog;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer {

    private HHPlayerBase hhPlayerBase;//播放器内核
    private View mContainer;

    public HHPlayer(HHPlayerBase hhPlayerBase) {
        this.hhPlayerBase = hhPlayerBase;
    }

    public HHPlayer(HHPlayerBase hhPlayerBase, SurfaceView surface) {
        this.hhPlayerBase = hhPlayerBase;
        hhPlayerBase.setSurface(surface);
    }

    public HHPlayer(HHPlayerBase hhPlayerBase, TextureView texture) {
        this.hhPlayerBase = hhPlayerBase;
        hhPlayerBase.setTexture(texture);
    }

    public void prepare(String path) {
        hhPlayerBase.prepare(path);
    }

    public void setLooping(boolean looping) {
        hhPlayerBase.setLooping(looping);
    }

    public void setMute(boolean mute) {
        setVolume(mute ? 0 : 1);
    }

    public void setVolume(float volume) {
        if (volume >= 0 && volume <= 1) {
            hhPlayerBase.setVolume(volume);
        } else {
            hhPlayerBase.setVolume(1);
            ViseLog.e("volume超范围：" + volume);
        }
    }

    public void start() {
        if (hhPlayerBase.isPrepared && hhPlayerBase.isPlayControl()) {
            hhPlayerBase.start();
            hhPlayerBase.getCurrentStart();
            hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.PLAYING;
            Log.e(hhPlayerBase.TAG, "doStart");
        }
    }

    public void pause() {
        if (hhPlayerBase.isPlayControl() && hhPlayerBase.isPlaying()) {
            hhPlayerBase.pause();
            hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.PAUSED;
            Log.e(hhPlayerBase.TAG, "doPause");
        }
    }

    public void reset() {
        hhPlayerBase.reset();
        hhPlayerBase.getCurrentStop();
        hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.IDLE;
    }

    public void stop() {
        if (hhPlayerBase.isPlayControl()) {
            hhPlayerBase.stop();
            hhPlayerBase.isPrepared = false;
            hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.IDLE;
            hhPlayerBase.getCurrentStop();
            Log.d(hhPlayerBase.TAG, "doStop");
        }
    }

    //单位毫秒
    public void seekTo(long position) {
        if (hhPlayerBase.isPlayControl()) {
            hhPlayerBase.playerPrePareState = HHPlayerBase.PlayerPrePareState.PREPARING;
            hhPlayerBase.seekTo(position);
            Log.d(hhPlayerBase.TAG, "seekTo");
        }
    }

    //0为暂停，1为正常。。。
    public void setSpeed(float speed) {
        if (hhPlayerBase.isPlayControl()) {
            hhPlayerBase.setSpeed(speed);
            Log.d(hhPlayerBase.TAG, "setSpeed");
        }
    }

    public void release() {
        hhPlayerBase.release();
        stop();
        hhPlayerBase.getCurrentStop();
        hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.NONE;
        hhPlayerBase.isPrepared = false;
        Log.d(hhPlayerBase.TAG, "doRelease");
    }

    public Object getMediaPath() {
        return hhPlayerBase.getMediaPath();
    }

    public boolean isPlaying() {
        return hhPlayerBase.isMediaPlaying();
    }

    public long getCurrentPosition() {
        if (hhPlayerBase.isPlayControl()) {
            return hhPlayerBase.getCurrentPosition();
        }
        return -1;
    }

    public long getMediaDuration() {
        if (hhPlayerBase.isPlayControl()) {
            return hhPlayerBase.getMediaDuration();
        }
        return -1;
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        hhPlayerBase.setPreparedListener(preparedListener);
    }

    public void setCompletionListener(final HOnListener<Object> completionListener) {
        hhPlayerBase.setCompletionListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                Log.e(hhPlayerBase.TAG, "play Completion");
                hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.PLAYBACK_COMPLETED;
                completionListener.onListen(o);
            }
        });
    }

    public void setProgessListener(HOnListener<Long> progessListener) {
        hhPlayerBase.setProgessListener(progessListener);
    }

    public void setOnBufferingUpdateListener(HOnListener<Integer> onBufferingUpdateListener) {
        hhPlayerBase.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    public HHPlayerBase.PlayerState getPlayerState() {
        return hhPlayerBase.mPlayerState;
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
