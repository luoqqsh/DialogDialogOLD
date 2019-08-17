package x.com.hl_player;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.provider.Settings;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import x.com.hl_base.HOnListener;
import x.com.hl_player.View.HHPlayerView;
import x.com.hl_player.View.HHSurfaceStateListener;
import x.com.hl_player.base.HHPlayerCore;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer3 {
    private HHPlayerCore hhPlayerCore;//播放器内
    private HHPlayerView hhPlayerView;
    private Context mContext;
    private boolean isChangeBrigtness = false;
    private ViewGroup viewGroup;

    public enum PlayerView {
        NEWSURFACE,//新建surfaceview
        NEWTEXTURE,//新建textureview
        SURFACE,//外部提供的surface
        MUSIC,//音乐
        HHSURFACE
    }

    //播放音乐
    public HHPlayer3(Context context, HHPlayerCore hhPlayerCore) {
        this(context, hhPlayerCore, PlayerView.MUSIC, null, null);
    }

    //使用外部的suraface
    public HHPlayer3(Context context, HHPlayerCore hhPlayerCore, Surface surface) {
        this(context, hhPlayerCore, PlayerView.SURFACE, null, surface);
    }

    //使用外部的viewGroup，需要选择NEWSURFACE、NEWTEXTURE
    public HHPlayer3(Context context, HHPlayerCore hhPlayerCore, PlayerView playerView, ViewGroup viewGroup) {
        this(context, hhPlayerCore, playerView, viewGroup, null);
    }

    //使用NEWSURFACE、NEWTEXTURE必须传viewGroup
    public HHPlayer3(Context context, HHPlayerCore hhPlayerCore, PlayerView playerView, ViewGroup viewGroup, Surface surface) {
        mContext = context;
        hhPlayerCore.setContext(mContext);
        this.viewGroup = viewGroup;
        this.hhPlayerCore = hhPlayerCore;
        switch (playerView) {
            case NEWSURFACE:
                initSurfaceView();
                break;
            case NEWTEXTURE:
                initTextureView();
                break;
            case SURFACE:
                if (surface != null) {
                    initSurface(surface);
                } else {
                    Log.e(hhPlayerCore.ETAG, "surface为空");
                }
                break;
            case HHSURFACE:
                initHHSurfaceView();
                break;
            case MUSIC:
                break;
        }
    }

    private void initSurfaceView() {
        SurfaceView mSurfaceView = new SurfaceView(mContext.getApplicationContext());
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                hhPlayerCore.initView(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                holder.removeCallback(this);
                release();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mSurfaceView, params);
    }

    private void initTextureView() {
        TextureView mTextureView = new TextureView(mContext.getApplicationContext());
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                hhPlayerCore.initView(new Surface(surfaceTexture));
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if (surface != null) {
                    surface.release();
                }
                release();
                return true;
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mTextureView, params);
    }

    private void initHHSurfaceView() {
        hhPlayerView = new HHPlayerView();
        hhPlayerView.addView(mContext, viewGroup, 0, new HHSurfaceStateListener() {
            @Override
            public void onSurfaceAvailable(Surface surface) {
                hhPlayerCore.initView(surface);
            }

            @Override
            public void onSurfaceSizeChanged(Surface surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceDestroyed(Surface surface) {
                return false;
            }

            @Override
            public void onSurfaceUpdated(Surface surface) {

            }
        });
    }

    private void initSurface(Surface surface) {
        hhPlayerCore.initView(surface);
    }

    private void initSurfaceHolder(SurfaceHolder surfaceHolder) {
        hhPlayerCore.initView(surfaceHolder);
    }

    public void addExtraView(View view) {
        if (viewGroup == null) {
            Log.e(hhPlayerCore.TAG, "必须设置viewGroup");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            viewGroup.addView(view, params);
        }
    }

    public void setPlayWhenPrepared(boolean isPlayWhenPrepared) {
        hhPlayerCore.setPlayWhenPrepared(isPlayWhenPrepared);
    }

    public void prepare(String path) {
        hhPlayerCore.prepare(path);
    }

    public void start() {
        if (hhPlayerCore.isPrepared && hhPlayerCore.isPlayControl()) {
            hhPlayerCore.start();
            hhPlayerCore.getCurrentStart();
            hhPlayerCore.mPlayerState = HHPlayerCore.PlayerState.PLAYING;
            Log.e(hhPlayerCore.TAG, "doStart");
        }
    }

    public void pause() {
        if (hhPlayerCore.isPlayControl() && hhPlayerCore.isPlaying()) {
            hhPlayerCore.pause();
            hhPlayerCore.mPlayerState = HHPlayerCore.PlayerState.PAUSE;
            Log.e(hhPlayerCore.TAG, "doPause");
        }
    }

    public void reset() {
        hhPlayerCore.reset();
        hhPlayerCore.getCurrentStop();
        hhPlayerCore.mPlayerState = HHPlayerCore.PlayerState.IDLE;
    }

    public void stop() {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.stop();
            hhPlayerCore.getCurrentStop();
            Log.d(hhPlayerCore.TAG, "doStop");
        }
    }

    //单位毫秒
    public void seekTo(long position) {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.playerPrePareState = HHPlayerCore.PlayerPrePareState.PREPARING;
            hhPlayerCore.seekTo(position);
            Log.d(hhPlayerCore.TAG, "seekTo");
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
        hhPlayerCore.setLooping(looping);
    }

    //设置静音
    public void setMute(boolean mute) {
        setVolume(mute ? 0 : 1);
    }

    //设置音量，范围0-1
    public void setVolume(float volume) {
        if (volume >= 0 && volume <= 1) {
            hhPlayerCore.volume = volume;
            hhPlayerCore.setVolume(volume);
        } else {
            hhPlayerCore.volume = 1;
            hhPlayerCore.setVolume(1);
            Log.e(hhPlayerCore.ETAG, "volume超范围：" + volume);
        }
    }

    //获取当前音量
    public float getVolume() {
        return hhPlayerCore.volume;
    }

    //设置播放速度，快放慢放。0就停了，1为正常速度。。。
    public void setSpeed(float speed) {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.setSpeed(speed);
            Log.d(hhPlayerCore.TAG, "setSpeed");
        }
    }

    public void release() {
        stop();
        hhPlayerCore.release();
        hhPlayerCore.getCurrentStop();
        hhPlayerCore.mPlayerState = HHPlayerCore.PlayerState.NONE;
        hhPlayerCore.isPrepared = false;
        Log.d(hhPlayerCore.TAG, "doRelease");
    }


    public Object getMediaPath() {
        return hhPlayerCore.getMediaPath();
    }

    public boolean isPlaying() {
        return hhPlayerCore.isMediaPlaying();
    }

    //获取当前播放进度，-1为获取失败
    public long getCurrentPosition() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getCurrentPosition();
        }
        return -1;
    }

    //获取媒体总时长，-1为获取失败
    public long getMediaDuration() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getMediaDuration();
        }
        return -1;
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        hhPlayerCore.setPreparedListener(preparedListener);
    }

    //播放器播放完成监听
    public void setCompletionListener(final HOnListener<Object> completionListener) {
        hhPlayerCore.setCompletionListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                Log.e(hhPlayerCore.TAG, "play Completion");
                hhPlayerCore.mPlayerState = HHPlayerCore.PlayerState.PLAYBACK_COMPLETED;
                if (isChangeBrigtness) {//播放完恢复原来的亮度，没设置亮度则不启用
                    setScreenBrigtness(getSystemScreenBrightness());
                }
                completionListener.onListen(o);
            }
        });
    }

    //播放器错误监听
    public void setOnErrorListener(HOnListener<Object> errorListener) {
        hhPlayerCore.setOnErrorListener(errorListener);
    }

    //播放器播放进度监听//当前是timer线程
    public void setProgessListener(HOnListener<Long> progessListener) {
        hhPlayerCore.setProgessListener(progessListener);
    }

    //播放器缓存进度监听
    public void setOnBufferingUpdateListener(HOnListener<Integer> onBufferingUpdateListener) {
        hhPlayerCore.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    //获取当前播放器状态
    public HHPlayerCore.PlayerState getPlayerState() {
        return hhPlayerCore.mPlayerState;
    }

    public void fullScreen() {
        if (viewGroup == null) {
            Log.e(hhPlayerCore.ETAG, "必须要有viewGroup才能执行此操作");
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
