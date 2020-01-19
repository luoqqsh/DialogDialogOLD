package com.xing.hhplayer.common;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;

import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TvListBean.TvView;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.View.HHControlView;
import com.xing.hhplayer.common.View.HHLoadingView;
import com.xing.hhplayer.common.View.HHPlayerView;
import com.xing.hhplayer.common.View.HHSurfaceStateListener;
import com.xing.hhplayer.common.View.HHTVView;
import com.xing.hhplayer.common.View.HHThumbView;
import com.xing.hhplayer.common.base.ControlViewInterface;
import com.xing.hhplayer.common.base.HHPlayerCore;
import com.xing.hhplayer.common.base.OnPlayerListener;

import java.util.List;
import java.util.Map;

import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_screen;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer3 {
    private HHPlayerCore hhPlayerCore;//播放器内
    private HHPlayerView hhPlayerView;
    private FragmentActivity mActivity;
    private ViewGroup viewGroup;//视频容器
    private boolean isChangeBrightness = false;//是否设置改变亮度
    private boolean isPlayWhenPrepared = false;//准备好立即播放
    private boolean isReStart = false;//是否重新加载
    private ControlViewInterface cvInterface;
    private String title;
    private List<TvDataList> tvLists;//播放列表
    private OnPlayerListener<Object> preparedListener;
    private OnPlayerListener<PlayerStateType> stateChangeListener;
    private int currentPlayIndex = -1;

    /***********************************初始化播放器部分**************************************************************/
    //播放音乐
    public HHPlayer3(FragmentActivity context, HHPlayerCore hhPlayerCore) {
        this(context, hhPlayerCore, TvView.MUSIC, null, null);
    }

    //使用外部的suraface
    public HHPlayer3(FragmentActivity context, HHPlayerCore hhPlayerCore, Surface surface) {
        this(context, hhPlayerCore, TvView.SURFACE, null, surface);
    }

    //使用外部的viewGroup，需要选择NEWSURFACE、NEWTEXTURE
    public HHPlayer3(FragmentActivity context, HHPlayerCore hhPlayerCore, TvView playerView, ViewGroup viewGroup) {
        this(context, hhPlayerCore, playerView, viewGroup, null);
    }

    //使用NEWSURFACE、NEWTEXTURE必须传viewGroup
    public HHPlayer3(FragmentActivity mActivity, final HHPlayerCore hhPlayerCore, TvView playerView, final ViewGroup viewGroup, Surface surface) {
        hhPlayerCore.setContext(mActivity);
        hhPlayerCore.TAG = playerView.toString();
        this.hhPlayerCore = hhPlayerCore;
        this.mActivity = mActivity;
        this.viewGroup = viewGroup;
        if (viewGroup != null) {
            //给viewGroup设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = hl_screen.getHeightFromWidth(mActivity);
            viewGroup.setLayoutParams(layoutParams);
        }
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
                    ViseLog.e(hhPlayerCore.ETAG, "surface为空");
                }
                break;
            case HHSURFACE:
                initHHSurfaceView();
                break;
            case HHTEXTURE:
                initHHTextureView();
                break;
            case HHGLSURFACE:
                initHHGLSurfaceView();
                break;
            case MUSIC:
                break;
        }
        hhPlayerCore.setPreparedListener(new OnPlayerListener<Object>() {
            @Override
            public void onListen(Object o) {
                hhPlayerCore.setScalingMode(0);
                hhPlayerCore.isLive = hhPlayerCore.getMediaDurationMS() == 0;
                if (cvInterface != null) {
                    cvInterface.getPrePareState(PlayerPrePareStateType.PREPARED);
                }
                if (isPlayWhenPrepared || isReStart) {
                    isReStart = false;
                    start();
                }
                if (preparedListener != null) {
                    preparedListener.onListen(o);
                }
            }
        });
    }

    public void addControlView() {
        addExtraView(new HHControlView(mActivity, HHPlayer3.this), -1);
    }

    public void addTVView() {
        addExtraView(new HHTVView(mActivity, HHPlayer3.this), -1);
    }

    public void addLoadingView() {
        addExtraView(new HHLoadingView(mActivity, this), -1);
    }

    public ImageView addThumbView() {
        HHThumbView hhThumbView = new HHThumbView(mActivity, this);
        addExtraView(hhThumbView, 0);
        return hhThumbView.getDefaultThumb();
    }

    public void addExtraView(View view, int index) {
        if (viewGroup == null) {
            ViseLog.e(hhPlayerCore.TAG, "必须设置viewGroup");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewGroup.addView(view, index, params);
        }
    }

    private void initSurface(Surface surface) {
        hhPlayerCore.initView(surface);
    }

    private void initSurfaceHolder(SurfaceHolder surfaceHolder) {
        hhPlayerCore.initView(surfaceHolder);
    }

    /***********************************设置部分**************************************************************/
    public void setPlayWhenPrepared(boolean isPlayWhenPrepared) {
        this.isPlayWhenPrepared = isPlayWhenPrepared;
    }

    //设置屏幕亮度
    public void setScreenBrigtness(int brightness) {
        Window localWindow = ((Activity) mActivity).getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = (float) brightness / 100.0F;
        localWindow.setAttributes(localLayoutParams);
        isChangeBrightness = true;
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
            ViseLog.e(hhPlayerCore.ETAG, "volume超范围：" + volume);
        }
    }

    //设置播放速度，快放慢放。0就停了，1为正常速度。。。
    public void setSpeed(float speed) {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.setSpeed(speed);
        }
    }

    //设置视频缩放类型
    public void setVideoScaleType(int scaleType) {
        if (hhPlayerView != null) {
            hhPlayerView.setVideoScaleType(scaleType);
        }
    }

    //设置视频大小
    public void setVideoSize(int width, int height) {
        if (hhPlayerView != null) {
            hhPlayerView.setVideoSize(width, height);
        }
    }

    //设置添加view的交互
    public void setCvInterface(ControlViewInterface cvInterface) {
        this.cvInterface = cvInterface;
    }

    //设置请求头，在prepare之前执行
    public void setHeader(Map<String, String> mHeaders) {
        hhPlayerCore.setHeaders(mHeaders);
    }

    /***********************************播放控制部分**************************************************************/
    //准备-地址，标题为空
    public void prepare(String path) {
        prepare(path, "");
        currentPlayIndex = -1;
    }

    //准备-地址，标题
    public void prepare(String path, String title) {
        this.title = title;
        hhPlayerCore.mPlayerState = PlayerStateType.IDLE;
        if (cvInterface != null) {
            cvInterface.getPrePareState(PlayerPrePareStateType.PREPARING);
        }
        hhPlayerCore.prepare(path);
        ViseLog.showLog("开始加载" + path);
    }

    //刷新播放列表，刷新后自动播放当前列表第一个
    public void updatePlayList(List<TvDataList> tvLists) {
        if (cvInterface != null) {
            cvInterface.refreshPlayList(tvLists);
        }
        currentPlayIndex = 0;
        prepare(tvLists, currentPlayIndex);
    }

    //准备-列表，第几个
    public void prepare(List<TvDataList> tvLists, int position) {
        if (tvLists != null && tvLists.size() > 0 && tvLists.size() > position) {
            this.tvLists = tvLists;
            currentPlayIndex = position;
            if (tvLists.get(position) != null) {
                prepare(tvLists.get(position).getChannelURLs().get(0).getChannelURL(), tvLists.get(position).getChannelName());
            } else {
                ViseLog.e(hhPlayerCore.ETAG, "播放列表为空，或不存在");
            }
        } else {
            ViseLog.e(hhPlayerCore.ETAG, "播放列表为空，或不存在");
        }
    }

    public int getPlayIndex() {
        if (tvLists != null) {
            return currentPlayIndex;
        }
        return -1;
    }

    public void start() {
        if (hhPlayerCore.isPrepared() && hhPlayerCore.isPlayControl()) {
            hhPlayerCore.start();
            hhPlayerCore.getCurrentStart();
            hhPlayerCore.mPlayerState = PlayerStateType.PLAYING;
            if (stateChangeListener != null) {
                stateChangeListener.onListen(hhPlayerCore.mPlayerState);
            }
            if (cvInterface != null) {
                cvInterface.getPlayState(hhPlayerCore.mPlayerState);
            }
        }
    }

    public void restart() {
        if (!TextUtils.isEmpty(hhPlayerCore.getMediaPath().toString()) && hhPlayerCore.isPlayControl()) {
            hhPlayerCore.prepare(hhPlayerCore.getMediaPath().toString());
            hhPlayerCore.getCurrentStart();
            hhPlayerCore.mPlayerState = PlayerStateType.IDLE;
            isReStart = true;
        }
    }

    public void pause() {
        if (hhPlayerCore.isPlayControl() && hhPlayerCore.isMediaPlaying()) {
            hhPlayerCore.pause();
            hhPlayerCore.mPlayerState = PlayerStateType.PAUSE;
            if (stateChangeListener != null) {
                stateChangeListener.onListen(hhPlayerCore.mPlayerState);
            }
            if (cvInterface != null) {
                cvInterface.getPlayState(hhPlayerCore.mPlayerState);
            }
        }
    }

    public void reset() {
        hhPlayerCore.reset();
        hhPlayerCore.getCurrentStop();
        hhPlayerCore.mPlayerState = PlayerStateType.IDLE;
        if (stateChangeListener != null) {
            stateChangeListener.onListen(hhPlayerCore.mPlayerState);
        }
    }

    public void stop() {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.stop();
            hhPlayerCore.getCurrentStop();
        }
        if (stateChangeListener != null) {
            stateChangeListener.onListen(hhPlayerCore.mPlayerState);
        }
    }

    //单位毫秒
    public void seekTo(long position) {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.seekTo(position);
        }
    }

    //全屏
    public void fullScreen() {
        if (viewGroup == null) {
            ViseLog.e(hhPlayerCore.ETAG, "必须要有viewGroup才能执行此操作");
            return;
        }
    }

    public void release() {
        stop();
        hhPlayerCore.getCurrentStop();
        hhPlayerCore.release();
        hhPlayerCore.mPlayerState = PlayerStateType.NONE;
        hhPlayerCore.playerPrePareState = PlayerPrePareStateType.NONE;
        if (cvInterface != null) {
            cvInterface.getPrePareState(hhPlayerCore.playerPrePareState);
        }
    }

    /***********************************获取参数部分**************************************************************/
    //获取系统屏幕亮度
    private int getSystemScreenBrightness() {
        try {
            int brightNess = Settings.System.getInt(mActivity.getContentResolver(), "screen_brightness");
            return (int) ((float) (brightNess * 100) / 255.0F);
        } catch (Settings.SettingNotFoundException var4) {
            return -1;
        }
    }

    //获取屏幕亮度
    public int getScreenBrightness() {
        if (this.mActivity instanceof Activity) {
            Window localWindow = ((Activity) this.mActivity).getWindow();
            WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
            float screenBrightness = localLayoutParams.screenBrightness;
            if (screenBrightness > 1.0F || screenBrightness < 0.1) {
                return getSystemScreenBrightness();
            }
            return (int) (screenBrightness * 100.0F);
        }
        return getSystemScreenBrightness();
    }
    //获取当前音量

    public float getVolume() {
        return hhPlayerCore.volume;
    }

    public Object getMediaPath() {
        return hhPlayerCore.getMediaPath();
    }

    public boolean isPlaying() {
        return hhPlayerCore.isMediaPlaying();
    }

    public String getTitle() {
        return title;
    }

    //获取当前播放进度，-1为获取失败
    public long getCurrentPosition() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getCurrentPositionMS();
        }
        return -1;
    }

    //获取视频宽，需要在prepare完成之后执行
    public long getMediaWidth() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.videoWidth;
        }
        return 0;
    }

    //获取视频高，需要在prepare完成之后执行
    public long getMediaHeight() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.videoHeight;
        }
        return 0;
    }

    //获取媒体总时长，-1为获取失败
    public long getMediaDuration() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getMediaDurationMS();
        }
        return -1;
    }

    //获取当前播放器状态
    public PlayerStateType getPlayerState() {
        return hhPlayerCore.mPlayerState;
    }


    //获取播放列表
    public List<TvDataList> getUrlDataList() {
        return tvLists;
    }

    /***********************************初始化view部分**************************************************************/
    private void initSurfaceView() {
        SurfaceView mSurfaceView = new SurfaceView(mActivity.getApplicationContext());
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
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mSurfaceView, params);
    }

    private void initTextureView() {
        TextureView mTextureView = new TextureView(mActivity.getApplicationContext());
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
        hhPlayerView.addView(mActivity, viewGroup, 0, TvView.HHSURFACE, new HHSurfaceStateListener() {
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

    private void initHHTextureView() {
        hhPlayerView = new HHPlayerView();
        hhPlayerView.addView(mActivity, viewGroup, 0, TvView.HHTEXTURE, new HHSurfaceStateListener() {
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

    private void initHHGLSurfaceView() {
        hhPlayerView = new HHPlayerView();
        hhPlayerView.addView(mActivity, viewGroup, 0, TvView.HHGLSURFACE, new HHSurfaceStateListener() {
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

    /***********************************设置监听部分**************************************************************/
    public void setPreparedListener(final OnPlayerListener<Object> preparedListener) {
        this.preparedListener = preparedListener;
    }

    //播放器播放完成监听
    public void setCompletionListener(final OnPlayerListener<Object> completionListener) {
        hhPlayerCore.setCompletionListener(new OnPlayerListener<Object>() {
            @Override
            public void onListen(Object o) {
                hhPlayerCore.mPlayerState = PlayerStateType.PLAYBACK_COMPLETED;
                if (isChangeBrightness) {//播放完恢复原来的亮度，没设置亮度则不启用
                    setScreenBrigtness(getSystemScreenBrightness());
                }
                completionListener.onListen(o);
            }
        });
    }

    //播放器错误监听
    public void setOnErrorListener(OnPlayerListener<Object> errorListener) {
        hhPlayerCore.setOnErrorListener(new OnPlayerListener<Object>() {
            @Override
            public void onListen(Object o) {
                if (cvInterface != null) {
                    cvInterface.getPrePareState(PlayerPrePareStateType.ERROR);
                }
                if (errorListener != null) {
                    errorListener.onListen(o);
                }
            }
        });
    }

    //播放器播放进度监听//当前是timer线程
    public void setProgessListener(final OnPlayerListener<Long> progessListener) {
        hhPlayerCore.setProgessListener(new OnPlayerListener<Long>() {
            @Override
            public void onListen(Long aLong) {
                if (progessListener != null) {
                    progessListener.onListen(aLong);
                }
                if (cvInterface != null) {
                    cvInterface.changeProgress(aLong);
                }
            }
        });
    }

    //播放器缓存进度监听
    public void setOnBufferingUpdateListener(OnPlayerListener<Integer> onBufferingUpdateListener) {
        hhPlayerCore.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    //播放器状态改变监听
    public void setStateChangeListener(OnPlayerListener<PlayerStateType> stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public void sendKeyDown(int keyCode) {
        if (cvInterface != null) {
            cvInterface.receiveKeyDown(keyCode);
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
