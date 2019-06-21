package x.com.hl_player.base;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;

import java.util.Map;

import x.com.hl_base.HOnListener;
import x.com.hl_rx.TaskDelayManager;

/**
 * 播放器基础类
 */
public abstract class HHPlayerBase {
    public PlayerState mPlayerState = PlayerState.IDLE;
    public PlayerPrePareState playerPrePareState = PlayerPrePareState.NONE;
    public String TAG = "HHPlayerBase";

    public Object mediaPath;

    public float volume = 1;
    public Context context;//有的播放器需要context
    public boolean isCompletePlay = false;//是否完成播放
    public boolean isLooping = true; //是否循环播放
    public boolean isPrepared = false;//是否准备好
    public boolean isPlayWhenPrepared = false;//准备好立即播放
    public HOnListener<Object> onPreparedListener;
    public HOnListener<Long> onProgessListener;
    public HOnListener<Object> onCompletionListener;
    public HOnListener<Integer> onBufferingUpdateListener;
    private TaskDelayManager a;
    private Map<String, String> mHeaders;

    public enum PlayerState {
        ERROR,//出错
        IDLE,//闲置，初始化
        PLAYING,//播放中
        PREPARED,//准备就绪,准备好未播放的状态，判断准备状态以PlayerPrePareState为准
        PAUSED,//暂停中
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


    public void initView(SurfaceView surface) {

    }

    public void initView(TextureView texture) {

    }

    public void initView(Surface surface) {

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

    public void setCompletionListener(HOnListener<Object> completionListener) {
        this.onCompletionListener = completionListener;
    }

    public void setOnBufferingUpdateListener(HOnListener<Integer> OnBufferingUpdateListener) {
        this.onBufferingUpdateListener = OnBufferingUpdateListener;
    }

    public void setPlayWhenPrepared(boolean playWhenPrepared) {
        isPlayWhenPrepared = playWhenPrepared;
    }

    public boolean isPlayControl() {
        return mPlayerState == PlayerState.PLAYING || mPlayerState == PlayerState.PAUSED || mPlayerState == PlayerState.PHONE
                || mPlayerState == PlayerState.PREPARED || mPlayerState == PlayerState.PLAYBACK_COMPLETED
                && playerPrePareState == PlayerPrePareState.PREPARED;
    }

    public boolean isMediaPlaying() {
        return mPlayerState == PlayerState.PLAYING && isPlaying();
    }

    public void getCurrentStart() {
        if (a == null) {
            a = new TaskDelayManager() {
                @Override
                public void onListen(Long index) {
                    if (onProgessListener != null) {
                        onProgessListener.onListen(getCurrentPosition());
                    }
                }
            };
            a.loop(1000);
        }
    }

    public void getCurrentStop() {
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
