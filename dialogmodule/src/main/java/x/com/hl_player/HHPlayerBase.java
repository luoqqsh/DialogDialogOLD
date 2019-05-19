package x.com.hl_player;

import android.view.SurfaceView;
import android.view.TextureView;

import x.com.hl_base.HOnListener;
import x.com.hl_rx.TaskDelayManager;

/**
 * 播放器基础类
 */
public abstract class HHPlayerBase {
    PlayerState mPlayerState = PlayerState.IDLE;
    PlayerPrePareState playerPrePareState = PlayerPrePareState.NONE;
    public String TAG = "HHPlayerBase";

    String mediaPath;

    float volume = 1;
    boolean isLooping = true; //是否循环播放
    boolean isPrepared = false;//是否准备好
    HOnListener<Object> preparedListener;
    HOnListener<Long> progessListener;
    HOnListener<Object> completionListener;
    private TaskDelayManager a;

    enum PlayerState {
        ERROR,//出错
        IDLE,//闲置，初始化
        PLAYING,//播放中
        PREPARED,//准备就绪,准备好未播放的状态，判断准备状态以PlayerPrePareState为准
        PAUSED,//暂停中
        PHONE,//通话中
        PLAYBACK_COMPLETED,//播放完成
        NONE//结束，需要重新new
    }

    enum PlayerPrePareState {
        PREPARED,
        PREPARING,
        NONE,
        ERROR
    }

    enum PlayerMode {
        MODE_FULL_SCREEN,
        MODE_NORMAL,
        MODE_TINY_WINDOW
    }

    public abstract void prepare(String mediaPath);

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


    public void setSurface(SurfaceView surface) {

    }

    public void setTexture(TextureView texture) {

    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void setProgessListener(HOnListener<Long> progessListener) {
        this.progessListener = progessListener;
    }

    public void setCompletionListener(HOnListener<Object> completionListener) {
        this.completionListener = completionListener;
    }

    public boolean isPlayControl() {
        return mPlayerState == PlayerState.PLAYING || mPlayerState == PlayerState.PAUSED || mPlayerState == PlayerState.PHONE
                || mPlayerState == PlayerState.PREPARED || mPlayerState == PlayerState.PLAYBACK_COMPLETED
                && playerPrePareState == PlayerPrePareState.PREPARED;
    }

    void getCurrentStart() {
        if (a == null) {
            a = new TaskDelayManager() {
                @Override
                public void onListen(Long index) {
                    if (progessListener != null) {
                        progessListener.onListen(getCurrentPosition());
                    }
                }
            };
            a.loop(1000);
        }
    }

    void getCurrentStop() {
        if (a != null) {
            a.cancel();
            a = null;
        }
    }
}
