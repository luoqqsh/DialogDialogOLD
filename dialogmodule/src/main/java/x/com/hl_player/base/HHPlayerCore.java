package x.com.hl_player.base;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import x.com.hl_base.HOnListener;
import x.com.hl_rx.TaskDelayManager;

/**
 * 播放器基础类
 */
public abstract class HHPlayerCore {
    public PlayerState mPlayerState = PlayerState.IDLE;
    public PlayerPrePareState playerPrePareState = PlayerPrePareState.NONE;
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

    public enum PlayerState {
        ERROR,//出错
        IDLE,//闲置，初始化
        PLAYING,//播放中
        PREPARED,//准备就绪,准备好未播放的状态，判断准备状态以PlayerPrePareState为准
        PAUSE,//暂停中
        PHONE,//通话中
        PLAYBACK_COMPLETED,//播放完成
        NONE//结束，需要重新new
    }

    public enum PlayerPrePareState {
        PREPARED,
        PREPARING,
        NONE,
        ERROR
    }

    public enum PlayerMode {
        MODE_FULL_SCREEN,
        MODE_NORMAL,
        MODE_TINY_WINDOW
    }

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

    public void initView(Surface surface) {
    }

    public void initView(SurfaceHolder surfaceHolder) {
    }

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
        return mPlayerState == PlayerState.PLAYING || mPlayerState == PlayerState.PAUSE || mPlayerState == PlayerState.PHONE
                || mPlayerState == PlayerState.PREPARED || mPlayerState == PlayerState.PLAYBACK_COMPLETED
                && playerPrePareState == PlayerPrePareState.PREPARED;
    }

    //是否正在播放
    public boolean isMediaPlaying() {
        return mPlayerState == PlayerState.PLAYING && isPlaying();
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
            }, 0,1000);//delay延迟时间，period间隔时间
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
