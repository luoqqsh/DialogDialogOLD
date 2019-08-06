package x.com.hl_player;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import x.com.hl_base.HOnListener;
import x.com.hl_player.base.HHPlayerBase;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer3 {
    private HHPlayerBase hhPlayerBase;//播放器内核
    private Context mContext;
    private boolean isChangeBrigtness = false;
    private ViewGroup viewGroup;

    public enum PlayerView {
        NEWSURFACE,//新建surfaceview
        NEWTEXTURE,//新建textureview
        SURFACE,//外部提供的surface
        MUSIC//音乐
    }

    //播放音乐
    public HHPlayer3(Context context, HHPlayerBase hhPlayerBase) {
        this(context, hhPlayerBase, PlayerView.MUSIC, null, null);
    }

    //使用外部的suraface
    public HHPlayer3(Context context, HHPlayerBase hhPlayerBase, Surface surface) {
        this(context, hhPlayerBase, PlayerView.SURFACE, null, surface);
    }

    //使用外部的viewGroup，需要选择NEWSURFACE、NEWTEXTURE
    public HHPlayer3(Context context, HHPlayerBase hhPlayerBase, PlayerView playerView, ViewGroup viewGroup) {
        this(context, hhPlayerBase, playerView, viewGroup, null);
    }

    //使用NEWSURFACE、NEWTEXTURE必须传viewGroup
    private HHPlayer3(Context context, HHPlayerBase hhPlayerBase, PlayerView playerView, ViewGroup viewGroup, Surface surface) {
        mContext = context;
        hhPlayerBase.setContext(mContext);
        this.viewGroup = viewGroup;
        this.hhPlayerBase = hhPlayerBase;
        switch (playerView) {
            case NEWSURFACE:
                initSurfaceView();
                break;
            case NEWTEXTURE:
                initTextureView();
                break;
            case SURFACE:
                initSurface(surface);
                break;
            case MUSIC:
                break;
        }
    }

    private void initSurfaceView() {
        SurfaceView mSurfaceView = new SurfaceView(mContext.getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mSurfaceView, params);
        hhPlayerBase.initView(mSurfaceView);
    }

    private void initTextureView() {
        TextureView mTextureView = new TextureView(mContext.getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mTextureView, params);
        hhPlayerBase.initView(mTextureView);
    }

    private void initSurface(Surface surface) {
        hhPlayerBase.initView(surface);
    }

    public void addExtraView(View view) {
        if (viewGroup == null) {
            Log.e(hhPlayerBase.TAG, "必须设置viewGroup");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            viewGroup.addView(view, params);
        }
    }

    public void setPlayWhenPrepared(boolean isPlayWhenPrepared) {
        hhPlayerBase.setPlayWhenPrepared(isPlayWhenPrepared);
    }

    public void prepare(String path) {
        hhPlayerBase.prepare(path);
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
            hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.PAUSE;
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

    //设置屏幕亮度
    public void setScreenBrigtness(int brightness) {
        Window localWindow = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = (float) brightness / 100.0F;
        localWindow.setAttributes(localLayoutParams);
        isChangeBrigtness = true;
    }

    //获取系统屏幕亮度
    private int getSystemScreenBrightness() {
        try {
            int brightNess = Settings.System.getInt(mContext.getContentResolver(), "screen_brightness");
            return (int) ((float) (brightNess * 100) / 255.0F);
        } catch (Settings.SettingNotFoundException var4) {
            return -1;
        }
    }

    //获取屏幕亮度
    public int getScreenBrightness() {
        if (this.mContext instanceof Activity) {
            Window localWindow = ((Activity) this.mContext).getWindow();
            WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
            float screenBrightness = localLayoutParams.screenBrightness;
            if (screenBrightness > 1.0F || screenBrightness < 0.1) {
                return getSystemScreenBrightness();
            }
            return (int) (screenBrightness * 100.0F);
        }
        return getSystemScreenBrightness();
    }

    //设置是否循环播放
    public void setLooping(boolean looping) {
        hhPlayerBase.setLooping(looping);
    }

    //设置静音
    public void setMute(boolean mute) {
        setVolume(mute ? 0 : 1);
    }

    //设置音量，范围0-1
    public void setVolume(float volume) {
        if (volume >= 0 && volume <= 1) {
            hhPlayerBase.volume = volume;
            hhPlayerBase.setVolume(volume);
        } else {
            hhPlayerBase.volume = 1;
            hhPlayerBase.setVolume(1);
            Log.e(hhPlayerBase.ETAG, "volume超范围：" + volume);
        }
    }

    //获取当前音量
    public float getVolume() {
        return hhPlayerBase.volume;
    }

    //设置播放速度，快放慢放。0就停了，1为正常速度。。。
    public void setSpeed(float speed) {
        if (hhPlayerBase.isPlayControl()) {
            hhPlayerBase.setSpeed(speed);
            Log.d(hhPlayerBase.TAG, "setSpeed");
        }
    }

    public void release() {
        stop();
        hhPlayerBase.release();
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

    //获取当前播放进度，-1为获取失败
    public long getCurrentPosition() {
        if (hhPlayerBase.isPlayControl()) {
            return hhPlayerBase.getCurrentPosition();
        }
        return -1;
    }

    //获取媒体总时长，-1为获取失败
    public long getMediaDuration() {
        if (hhPlayerBase.isPlayControl()) {
            return hhPlayerBase.getMediaDuration();
        }
        return -1;
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        hhPlayerBase.setPreparedListener(preparedListener);
    }

    //播放器播放完成监听
    public void setCompletionListener(final HOnListener<Object> completionListener) {
        hhPlayerBase.setCompletionListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                Log.e(hhPlayerBase.TAG, "play Completion");
                hhPlayerBase.mPlayerState = HHPlayerBase.PlayerState.PLAYBACK_COMPLETED;
                if (isChangeBrigtness) {//播放完恢复原来的亮度，没设置亮度则不启用
                    setScreenBrigtness(getSystemScreenBrightness());
                }
                completionListener.onListen(o);
            }
        });
    }

    //播放器错误监听
    public void setOnErrorListener(HOnListener<Object> errorListener) {
        hhPlayerBase.setOnErrorListener(errorListener);
    }

    //播放器播放进度监听
    public void setProgessListener(HOnListener<Long> progessListener) {
        hhPlayerBase.setProgessListener(progessListener);
    }

    //播放器缓存进度监听
    public void setOnBufferingUpdateListener(HOnListener<Integer> onBufferingUpdateListener) {
        hhPlayerBase.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    //获取当前播放器状态
    public HHPlayerBase.PlayerState getPlayerState() {
        return hhPlayerBase.mPlayerState;
    }

    public void fullScreen() {
        if (viewGroup == null) {
            Log.e(hhPlayerBase.ETAG, "必须要有viewGroup才能执行此操作");
            return;
        }
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
