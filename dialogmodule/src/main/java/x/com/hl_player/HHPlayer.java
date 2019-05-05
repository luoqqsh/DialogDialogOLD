package x.com.hl_player;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import x.com.hl_base.HOnListener;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_context;
import x.com.hl_tools.hl_screen;
import x.com.hl_tools.hl_view;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer {
    private HHPlayerBase hhPlayerBase;//播放器内核
    private HHPlayerBase.PlayerMode mCurrentMode = HHPlayerBase.PlayerMode.MODE_NORMAL;
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

    public void setStartWhenPrepared(boolean startWhenPrepared) {
        hhPlayerBase.setStartWhenPrepared(startWhenPrepared);
    }

    public void start() {
        hhPlayerBase.start();
    }

    public void pause() {
        hhPlayerBase.pause();
    }

    public void reset() {
        hhPlayerBase.reset();
    }

    public void stop() {
        hhPlayerBase.stop();
    }

    //单位毫秒
    public void seekTo(long position) {
        hhPlayerBase.seekTo(position);
    }

    //0为暂停，1为正常。。。
    public void setSpeed(float speed) {
        hhPlayerBase.setSpeed(speed);
    }

    public void release() {
        hhPlayerBase.release();
    }

    public String getMediaPath() {
        return hhPlayerBase.getMusicPath();
    }

    public long getCurrentPosition() {
        return hhPlayerBase.getCurrentPosition();
    }

    public long getMediaDuration() {
        return hhPlayerBase.getMediaDuration();
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        hhPlayerBase.setPreparedListener(preparedListener);
    }

    public void setCompletionListener(HOnListener<Object> completionListener) {
        hhPlayerBase.setCompletionListener(completionListener);
    }

    public void setProgessListener(HOnListener<Long> progessListener) {
        hhPlayerBase.setProgessListener(progessListener);
    }

    public HHPlayerBase.PlayerState getState() {
        return hhPlayerBase.getmPlayerState();
    }

    public boolean isPrepared() {
        return hhPlayerBase.isPrepared();
    }

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

    /**
     * 全屏，将mContainer(内部包含mTextureView和mController)从当前容器中移除，并添加到android.R.content中.
     * 切换横屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期
     */
    public void enterFullScreen(Context mContext) {
        if (mCurrentMode == HHPlayerBase.PlayerMode.MODE_FULL_SCREEN) return;

        // 隐藏ActionBar、状态栏，并横屏
        hl_view.hideActionBar(mContext);
        hl_context.scanForActivity(mContext)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ViewGroup contentView = hl_context.scanForActivity(mContext)
                .findViewById(android.R.id.content);
        if (mCurrentMode == HHPlayerBase.PlayerMode.MODE_TINY_WINDOW) {
            contentView.removeView(mContainer);
        } else {
//            this.removeView(mContainer);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(mContainer, params);

        mCurrentMode = HHPlayerBase.PlayerMode.MODE_FULL_SCREEN;
    }

    /**
     * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
     * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期.
     *
     * @return true退出全屏.
     */
    public boolean exitFullScreen(Context mContext) {
        if (mCurrentMode == HHPlayerBase.PlayerMode.MODE_FULL_SCREEN) {
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

            mCurrentMode = HHPlayerBase.PlayerMode.MODE_NORMAL;
            return true;
        }
        return false;
    }

    /**
     * 进入小窗口播放，小窗口播放的实现原理与全屏播放类似。
     */
    public void enterTinyWindow(Context mContext) {
        if (mCurrentMode == HHPlayerBase.PlayerMode.MODE_TINY_WINDOW) return;
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

        mCurrentMode = HHPlayerBase.PlayerMode.MODE_TINY_WINDOW;
    }

    /**
     * 退出小窗口播放
     */
    public boolean exitTinyWindow(Context mContext) {
        if (mCurrentMode == HHPlayerBase.PlayerMode.MODE_TINY_WINDOW) {
            ViewGroup contentView = hl_context.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
//            this.addView(mContainer, params);

            mCurrentMode = HHPlayerBase.PlayerMode.MODE_NORMAL;
            return true;
        }
        return false;
    }
}
