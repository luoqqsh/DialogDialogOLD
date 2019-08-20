package x.com.hl_player.base;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import x.com.hl_base.HOnListener;
import x.com.hl_player.TypeBean.PlayerPrePareStateType;
import x.com.hl_player.TypeBean.PlayerStateType;
import x.com.hl_rx.TaskDelayManager;

/**
 * 播放器基础类
 */
public abstract class HHPlayerCore {
    public PlayerStateType mPlayerState = PlayerStateType.IDLE;
    public PlayerPrePareStateType playerPrePareState = PlayerPrePareStateType.NONE;
    public String TAG = "HHPlayerCore";
    public String ETAG = "hh_error";

    public Object mediaPath;

    public float volume = 1;
    public Context context;//有的播放器需要context
    public boolean isCompletePlay = false;//是否完成播放
    public boolean isLooping = true; //是否循环播放
    public boolean isPrepared = false;//是否准备好
    public HOnListener<Object> onPreparedListener;
    public HOnListener<Long> onProgessListener;
    public HOnListener<Object> onCompletionListener;
    public HOnListener<Object> onErrorListener;
    public HOnListener<Integer> onBufferingUpdateListener;
    private TaskDelayManager a;
    private Map<String, String> mHeaders;
    private Timer timer;

    public abstract void prepare(Object mediaPath);

    public abstract void start();

    public abstract void pause();

    public abstract void stop();

    public abstract void reset();

    public abstract void seekTo(long position);

    public abstract void release();

    public abstract void setVolume(float volume);

    public abstract void setSpeed(float speed);

    public abstract long getCurrentPosition();

    public abstract long getMediaDuration();

    public abstract boolean isPlaying();

    public abstract void initView(Surface surface);

    public abstract void initView(SurfaceHolder surfaceHolder);

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public Object getMediaPath() {
        return mediaPath;
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        this.onPreparedListener = preparedListener;
    }

    public void setProgessListener(HOnListener<Long> progessListener) {
        this.onProgessListener = progessListener;
    }

    public void setOnErrorListener(HOnListener<Object> onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void setCompletionListener(HOnListener<Object> completionListener) {
        this.onCompletionListener = completionListener;
    }

    public void setOnBufferingUpdateListener(HOnListener<Integer> OnBufferingUpdateListener) {
        this.onBufferingUpdateListener = OnBufferingUpdateListener;
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
                    if (onProgessListener != null) {
                        onProgessListener.onListen(getCurrentPosition());
                    }
                }
            }, 0, 1000);//delay延迟时间，period间隔时间
        }

//        if (a == null) {
//            a = new TaskDelayManager() {
//                @Override
//                public void onListen(Long index) {
//                    if (onProgessListener != null) {
//                        onProgessListener.onListen(getCurrentPosition());
//                    }
//                }
//            };
//            a.loop(1000);
//        }
    }

    //读取播放进度线程停止
    public void getCurrentStop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (a != null) {
            a.cancel();
            a = null;
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
