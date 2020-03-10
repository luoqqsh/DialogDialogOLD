package com.xing.hhplayer.common.base;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 播放器基础类
 */
public abstract class HHPlayerCore {
    public PlayerStateType mPlayerState = PlayerStateType.IDLE;
    public PlayerPrePareStateType playerPrePareState = PlayerPrePareStateType.NONE;
    public String TAG = "HHPlayer3";
    public String ETAG = "HHPlayer3_error";

    public Object mediaPath;
    public Uri mediaUri;
    public int videoWidth;
    public int videoHeight;

    public float volume = 1;
    public Context context;//有的播放器需要context
    public boolean isLooping = false; //是否循环播放
    public OnPlayerListener<Object> onCorePreparedListener;
    public OnPlayerListener<Long> onCoreProgressListener;
    public OnPlayerListener<Object> onCoreCompletionListener;
    public OnPlayerListener<Object> onCoreErrorListener;
    public OnPlayerListener<Integer> onCoreBufferingUpdateListener;
    public OnPlayerListener<Integer> onCoreInfoListener;
    private Map<String, String> mHeaders;
    private Timer timer;
    public boolean isLive = false;//是否实时直播
    private long timeMs = 0;//时间计数
    public int videoScalingMode;//缩放模式,0为默认大小，1为填充

    public abstract void prepare(String mediaPath);

    public abstract void prepare(Uri mediaUri);

    public abstract void start();

    public abstract void pause();

    public abstract void stop();

    public abstract void reset();

    public abstract void seekTo(long position);

    public abstract void release();

    public abstract void setVolume(float volume);

    public abstract void setSpeed(float speed);

    public abstract long getCurrentPositionMS();

    public abstract long getMediaDurationMS();

    public abstract boolean isPlaying();

    public abstract void initView(Surface surface);

    public abstract void initView(SurfaceHolder surfaceHolder);

    public abstract void setScalingMode(int videoScalingMode);

    public abstract void resetPlayer();

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isPrepared() {
        return playerPrePareState == PlayerPrePareStateType.PREPARED;
    }

    public Object getMediaPath() {
        return mediaPath;
    }

    public void setCorePreparedListener(OnPlayerListener<Object> preparedListener) {
        this.onCorePreparedListener = preparedListener;
    }

    public void setCoreProgressListener(OnPlayerListener<Long> progressListener) {
        this.onCoreProgressListener = progressListener;
    }

    public void setCoreErrorListener(OnPlayerListener<Object> errorListener) {
        this.onCoreErrorListener = errorListener;
    }

    public void setCoreCompletionListener(OnPlayerListener<Object> completionListener) {
        this.onCoreCompletionListener = completionListener;
    }

    public void setCoreBufferingUpdateListener(OnPlayerListener<Integer> bufferingUpdateListener) {
        this.onCoreBufferingUpdateListener = bufferingUpdateListener;
    }

    public void setCoreInfoListener(OnPlayerListener<Integer> onCoreInfoListener) {
        this.onCoreInfoListener = onCoreInfoListener;
    }

    //是否可以进行播放暂停等控制操作
    public boolean isPlayControl() {
        return mPlayerState == PlayerStateType.PLAYING || mPlayerState == PlayerStateType.PAUSE || mPlayerState == PlayerStateType.PHONE
                || mPlayerState == PlayerStateType.PREPARED || mPlayerState == PlayerStateType.PLAYBACK_COMPLETED
                && playerPrePareState == PlayerPrePareStateType.PREPARED;
    }

    //是否正在播放
    public boolean isMediaPlaying() {
        return mPlayerState == PlayerStateType.PLAYING && isPlaying();
    }

    //读取播放进度线程开启
    public void getCurrentStart() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (onCoreProgressListener != null) {
                        if (isLive) {
                            if (isMediaPlaying()) {
                                timeMs += 1000;
                                onCoreProgressListener.onListen(timeMs);
                            }
                        } else {
                            onCoreProgressListener.onListen(getCurrentPositionMS());
                        }
                    }
                }
            }, 0, 1000);//delay延迟时间，period间隔时间
        }
    }

    //读取播放进度线程停止
    public void getCurrentStop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     * 设置请求头
     *
     * @param mHeaders 播放地址请求头
     */
    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }
}
