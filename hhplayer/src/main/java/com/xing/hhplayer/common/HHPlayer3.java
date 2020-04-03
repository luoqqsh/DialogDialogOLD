package com.xing.hhplayer.common;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TvListBean.TvView;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.TypeBean.PlayerViewType;
import com.xing.hhplayer.common.View.HHControlView;
import com.xing.hhplayer.common.View.HHGLSurfaceView;
import com.xing.hhplayer.common.View.HHLoadingView;
import com.xing.hhplayer.common.View.HHPlayerView;
import com.xing.hhplayer.common.View.HHTVView;
import com.xing.hhplayer.common.View.HHThumbView;
import com.xing.hhplayer.common.base.ControlViewInterface;
import com.xing.hhplayer.common.base.HHPlayerCore;
import com.xing.hhplayer.common.base.OnPlayerListener;

import java.util.List;
import java.util.Map;

import x.com.hl_base.activity.HLBaseActivity;
import x.com.hl_base.interfaces.HOnListener;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_screen;
import x.com.hl_tools.hl_view;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer3 {
    private FragmentActivity mActivity;
    private HHPlayerCore hhPlayerCore;//播放器内核
    private boolean isPlayWhenPrepared = false;//准备好立即播放
    private boolean isReStart = false;//是否重新加载，调用restart()方法的判断
    //监听（对外）
    private OnPlayerListener<Object> preparedListener;
    private OnPlayerListener<Object> errorListener;
    private OnPlayerListener<Long> progressListener;
    private OnPlayerListener<PlayerStateType> stateChangeListener;
    private OnPlayerListener<Object> completionListener;
    private OnPlayerListener<Integer> bufferingUpdateListener;
    private OnPlayerListener<Integer> infoListener;
    //视频显示相关
    private HHPlayerView hhPlayerView;
    private boolean isChangeBrightness = false;//是否设置改变亮度
    private ViewGroup viewGroup;//视频容器
    private int screenScale = PlayerViewType.SCREEN_SCALE_DEFAULT;//默认显示比例
    private int screenType = PlayerViewType.SCREEN_DEFAULT;//默认显示比例
    private ControlViewInterface cvInterface;
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    //播放列表相关
    private String title;
    private List<TvDataList> tvLists;//播放列表
    private int currentPlayIndex = -1;//列表中播放第几个，没列表时为-1
    //监听，内部
    private OnPlayerListener<Integer> coreInfoListener = new OnPlayerListener<Integer>() {
        @Override
        public void onListen(Integer arg) {
            if (cvInterface != null) {
                cvInterface.getPlayBuffer(arg);
            }
            if (infoListener != null) {
                infoListener.onListen(arg);
            }
        }
    };
    private OnPlayerListener<Long> coreProgressListener = new OnPlayerListener<Long>() {
        @Override
        public void onListen(Long aLong) {
            if (progressListener != null) {
                progressListener.onListen(aLong);
            }
            if (cvInterface != null) {
                cvInterface.changeProgress(aLong);
            }
        }
    };

    private OnPlayerListener<Object> corePreparedListener = new OnPlayerListener<Object>() {
        @Override
        public void onListen(Object o) {
            hhPlayerCore.setScalingMode(0);
            hhPlayerCore.isLive = hhPlayerCore.getMediaDurationMS() == 0;
            screenScale = PlayerViewType.SCREEN_SCALE_DEFAULT;
            changeScale(screenScale);
            if (cvInterface != null) {
                cvInterface.getPrePareState(PlayerPrePareStateType.PREPARED);
            }
            if (isPlayWhenPrepared || isReStart) {
                isReStart = false;
                start();
            }
            if (viewGroup != null) {
                defaultWidth = viewGroup.getWidth();
                defaultHeight = viewGroup.getHeight();
                ViseLog.showInfo(hhPlayerCore.TAG, "容器包含View个数：" + viewGroup.getChildCount());
                ViseLog.showInfo(hhPlayerCore.TAG, "容器大小：" + viewGroup.getWidth() + "," + viewGroup.getHeight());
                View view = viewGroup.findViewById(R.id.video_normal_id);
                if (view != null) {
                    ViseLog.showInfo(hhPlayerCore.TAG, "视频View大小：" + view.getWidth() + "," + view.getHeight());
                } else {
                    ViseLog.showInfo(hhPlayerCore.TAG, "视频View大小：0,0");
                }
            } else {
                ViseLog.showInfo(hhPlayerCore.TAG, "容器大小：0,0");
                ViseLog.showInfo(hhPlayerCore.TAG, "视频View大小：0,0");
            }
            if (preparedListener != null) {
                preparedListener.onListen(o);
            }
        }
    };

    private OnPlayerListener<Object> coreCompletionListener = new OnPlayerListener<Object>() {
        @Override
        public void onListen(Object o) {
            hhPlayerCore.mPlayerState = PlayerStateType.PLAYBACK_COMPLETED;
            if (isChangeBrightness) {//播放完恢复原来的亮度，没设置亮度则不启用
                setScreenBrigtness(getSystemScreenBrightness());
            }
            if (completionListener != null) {
                completionListener.onListen(o);
            }
        }
    };
    private OnPlayerListener<Integer> coreBufferingUpdateListener = new OnPlayerListener<Integer>() {
        @Override
        public void onListen(Integer integer) {
            if (bufferingUpdateListener != null) {
                bufferingUpdateListener.onListen(integer);
            }
        }
    };
    private OnPlayerListener<Object> coreErrorListener = new OnPlayerListener<Object>() {
        @Override
        public void onListen(Object o) {
            ViseLog.e(hhPlayerCore.ETAG, o);
            if (cvInterface != null) {
                cvInterface.getPrePareState(PlayerPrePareStateType.ERROR);
            }
            if (errorListener != null) {
                errorListener.onListen(o);
            }
        }
    };

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
    public HHPlayer3(FragmentActivity mActivity, HHPlayerCore hhPlayerCore, TvView playerView, ViewGroup viewGroup, Surface surface) {
        hhPlayerCore.setContext(mActivity);
        hhPlayerCore.setContext(this.mActivity);
        hhPlayerCore.setCorePreparedListener(corePreparedListener);
        hhPlayerCore.setCoreProgressListener(coreProgressListener);
        hhPlayerCore.setCoreErrorListener(coreErrorListener);
        hhPlayerCore.setCoreCompletionListener(coreCompletionListener);
        hhPlayerCore.setCoreBufferingUpdateListener(coreBufferingUpdateListener);
        hhPlayerCore.setCoreInfoListener(coreInfoListener);
        this.hhPlayerCore = hhPlayerCore;
        this.mActivity = mActivity;
        this.viewGroup = viewGroup;
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
            case HHGLSURFACE:
                initHHGLSurfaceView();
                break;
            case MUSIC:
                break;
        }
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
            ViseLog.e(hhPlayerCore.ETAG, "必须设置viewGroup");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewGroup.addView(view, index, params);
        }
    }

    /***********************************设置部分**************************************************************/

    /**
     * 检查videoview是否已经添加
     *
     * @return 结果
     */
    public boolean checkVideoAdd() {
        View view = viewGroup.findViewById(R.id.video_normal_id);
        return view != null;
    }

    //设置prepare后立即播放，默认否
    public void setPlayWhenPrepared(boolean isPlayWhenPrepared) {
        this.isPlayWhenPrepared = isPlayWhenPrepared;
    }

    //设置是否循环播放，默认否
    public void setLooping(boolean looping) {
        hhPlayerCore.setLooping(looping);
    }

    //设置屏幕亮度
    public void setScreenBrigtness(int brightness) {
        Window localWindow = mActivity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = brightness / 100.0F;
        localWindow.setAttributes(localLayoutParams);
        isChangeBrightness = true;
    }

    //设置静音
    public void setMute(boolean mute) {
        setVolume(mute ? 0 : 1);
    }

    //设置音量，范围0-1
    public void setVolume(float volume) {
        if (volume >= 0 && volume <= 1) {
            hhPlayerCore.volume = volume;
        } else {
            hhPlayerCore.volume = 1;
            ViseLog.e(hhPlayerCore.ETAG, "volume超范围：" + volume);
        }
        if (hhPlayerCore.mPlayerState != PlayerStateType.NONE) {
            hhPlayerCore.setVolume(hhPlayerCore.volume);
        }
    }

    //设置播放速度，快放慢放。0就停了，1为正常速度。。。
    public void setSpeed(float speed) {
        if (hhPlayerCore.isPlayControl()) {
            hhPlayerCore.setSpeed(speed);
            start();//?
        }
    }

    //设置视频大小,hhsurfaceview设置显示比例时有用，待优化
    public void setVideoSize(int width, int height) {
        View old = viewGroup.findViewById(R.id.video_normal_id);
        if (old != null) {
            hl_screen.setViewSize(old, width, height);
        }
    }

    private void rotateScreen() {
        //判断当前是否为横屏,判断是否旋转
        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            hl_screen.setViewSize(viewGroup, hl_screen.getDeviceWidth(mActivity), hl_screen.getDeviceHeight(mActivity));
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            hl_screen.setViewSize(viewGroup, ViewGroup.LayoutParams.MATCH_PARENT, defaultHeight);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }
    }

    public void fullScreen() {
        screenType = PlayerViewType.SCREEN_FULL;
        screenScale = PlayerViewType.SCREEN_SCALE_DEFAULT;
        ((HLBaseActivity) mActivity).hideBar();
        rotateScreen();
//        //由于屏幕旋转，view的宽高更新需要点时间
        viewGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeScale(screenScale);
            }
        }, 200);
    }

    public void exitFullScreen() {
        screenType = PlayerViewType.SCREEN_DEFAULT;
        screenScale = PlayerViewType.SCREEN_SCALE_DEFAULT;
        rotateScreen();
        ((HLBaseActivity) mActivity).showBar();
        viewGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeScale(screenScale);
            }
        }, 200);
    }

    /**
     * 改变显示比例
     *
     * @param screenScaleType 显示比例类型
     */
    public void changeScale(int screenScaleType) {
        ViseLog.d(hhPlayerCore.TAG, PlayerViewType.getScaleStr(screenScaleType));
        switch (screenScaleType) {
            case PlayerViewType.SCREEN_SCALE_DEFAULT://0
                setVideoSize(hl_screen.getWidthByScaleWH(getMediaWidth() * 1f / getMediaHeight(), viewGroup.getHeight()), viewGroup.getHeight());
                break;
            case PlayerViewType.SCREEN_SCALE_4_3:
            case PlayerViewType.SCREEN_SCALE_16_9:
            case PlayerViewType.SCREEN_SCALE_1_1:
                setVideoSize(hl_screen.getWidthByScaleWH(PlayerViewType.getScaleByScreenType(screenScaleType), viewGroup.getHeight()), viewGroup.getHeight());
                break;
            case PlayerViewType.SCREEN_SCALE_FULL:
                setVideoSize(viewGroup.getWidth(), viewGroup.getHeight());
                break;
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
        currentPlayIndex = -1;
        prepare(path, "");
    }

    //准备-地址，标题
    public void prepare(String path, String title) {
        this.title = title;
        hhPlayerCore.mPlayerState = PlayerStateType.IDLE;
        if (cvInterface != null) {
            cvInterface.getPrePareState(PlayerPrePareStateType.PREPARING);
        }
        hhPlayerCore.prepare(path);
        ViseLog.showInfo(hhPlayerCore.TAG, "开始加载:" + path);
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
            if (tvLists.get(position) != null && tvLists.get(position).getChannelURLs().size() != 0) {
                prepare(tvLists.get(position).getChannelURLs().get(0).getChannelURL(), tvLists.get(position).getChannelName());
            } else {
                ViseLog.e(hhPlayerCore.ETAG, "播放频道为空数据");
                if (hhPlayerCore.onCoreErrorListener != null) {
                    hhPlayerCore.onCoreErrorListener.onListen("播放频道为空数据");
                }
            }
        } else {
            ViseLog.e(hhPlayerCore.ETAG, "播放列表为空，或不存在");
            if (hhPlayerCore.onCoreErrorListener != null) {
                hhPlayerCore.onCoreErrorListener.onListen("播放列表为空，或不存在");
            }
            currentPlayIndex = -1;
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
        if (!TextUtils.isEmpty((CharSequence) hhPlayerCore.getMediaPath()) && hhPlayerCore.isPlayControl()) {
            hhPlayerCore.prepare(hhPlayerCore.getMediaPath().toString());
            hhPlayerCore.getCurrentStop();
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

    public void resetPlayer() {
        hhPlayerCore.stop();
        hhPlayerCore.resetPlayer();
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

    public ViewGroup getPlayerView() {
        return viewGroup;
    }

    //获取屏幕亮度
    public int getScreenBrightness() {
        if (mActivity != null) {
            Window localWindow = mActivity.getWindow();
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

    public boolean isPrepared() {
        return hhPlayerCore != null && hhPlayerCore.isPrepared();
    }

    public String getTitle() {
        return title;
    }

    //获取当前播放进度，-1为获取失败
    public long getCurrentPositionMS() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getCurrentPositionMS();
        }
        return -1;
    }

    public long getTcpSpeed() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getTcpSpeed();
        }
        return 0;
    }

    //获取视频宽，需要在prepare完成之后执行
    public int getMediaWidth() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.videoWidth;
        }
        return 0;
    }

    //获取视频高，需要在prepare完成之后执行
    public int getMediaHeight() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.videoHeight;
        }
        return 0;
    }

    //获取媒体总时长，-1为获取失败
    public long getMediaDurationMS() {
        if (hhPlayerCore.isPlayControl()) {
            return hhPlayerCore.getMediaDurationMS();
        }
        return -1;
    }

    //获取当前播放器状态
    public PlayerStateType getPlayerState() {
        return hhPlayerCore.mPlayerState;
    }

    public int getScreenScale() {
        return screenScale;
    }

    public int getScreenType() {
        return screenType;
    }

    //获取播放列表
    public List<TvDataList> getUrlDataList() {
        return tvLists;
    }

    /***********************************初始化view部分**************************************************************/
    public void initSurface(Surface surface) {
        hhPlayerCore.initView(surface);
    }

    public void initSurfaceHolder(SurfaceHolder surfaceHolder) {
        hhPlayerCore.initView(surfaceHolder);
    }

    private void initSurfaceView() {
        if (checkVideoAdd()) {
            ViseLog.e(hhPlayerCore.ETAG, "视频View已存在");
            return;
        }
        SurfaceView mSurfaceView = new SurfaceView(mActivity.getApplicationContext());
        mSurfaceView.setId(R.id.video_normal_id);
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
        hl_view.addToParentCenter(viewGroup, mSurfaceView);
    }

    private void initTextureView() {
        if (checkVideoAdd()) {
            ViseLog.e(hhPlayerCore.ETAG, "视频View已存在");
            return;
        }
        TextureView mTextureView = new TextureView(mActivity.getApplicationContext());
        mTextureView.setId(R.id.video_normal_id);
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
        hl_view.addToParentCenter(viewGroup, mTextureView);
    }

    private void initHHGLSurfaceView() {
        if (checkVideoAdd()) {
            ViseLog.e(hhPlayerCore.ETAG, "视频View已存在");
            return;
        }
        HHGLSurfaceView mSurfaceView = new HHGLSurfaceView(mActivity.getApplicationContext());
        mSurfaceView.setId(R.id.video_normal_id);
        mSurfaceView.setSurfaceCreateListener(new HOnListener<Surface>() {
            @Override
            public void onListen(Surface surface) {
                hhPlayerCore.initView(surface);
            }
        });
        hl_view.addToParentCenter(viewGroup, mSurfaceView);
    }

    /***********************************设置监听部分**************************************************************/
    public void setOnPreparedListener(final OnPlayerListener<Object> preparedListener) {
        this.preparedListener = preparedListener;
    }

    //播放器播放完成监听
    public void setOnCompletionListener(final OnPlayerListener<Object> completionListener) {
        this.completionListener = completionListener;
    }

    //播放器错误监听
    public void setOnErrorListener(OnPlayerListener<Object> errorListener) {
        this.errorListener = errorListener;
    }

    //播放器播放进度监听//当前是timer线程
    public void setOnProgressListener(final OnPlayerListener<Long> progressListener) {
        this.progressListener = progressListener;
    }

    //播放器缓存进度监听
    public void setOnBufferingUpdateListener
    (OnPlayerListener<Integer> bufferingUpdateListener) {
        this.bufferingUpdateListener = bufferingUpdateListener;
    }

    //播放器状态改变监听
    public void setOnStateChangeListener
    (OnPlayerListener<PlayerStateType> stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public void setOnInfoListener(OnPlayerListener<Integer> infoListener) {
        this.infoListener = infoListener;
    }

    public boolean sendKeyDown(int keyCode) {
        if (screenType == PlayerViewType.SCREEN_FULL && keyCode == KeyEvent.KEYCODE_BACK) {
            exitFullScreen();
            return true;
        }
        if (cvInterface != null) {
            return cvInterface.receiveKeyDown(keyCode);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mActivity.onBackPressed();
            return false;
        }
        return false;
    }
}
