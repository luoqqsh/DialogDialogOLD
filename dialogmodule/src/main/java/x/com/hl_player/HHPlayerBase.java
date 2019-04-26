package x.com.hl_player;

import android.view.SurfaceView;
import android.view.TextureView;

import x.com.hl_base.HOnListener;

/**
 * 播放器基础类
 */
public abstract class HHPlayerBase {
    protected String musicPath;
    protected PlayerState mPlayerState;
    protected String TAG = "HHPlayerBase";
    protected float volume = 1;
    protected boolean isLooping = true; //是否循环播放
    protected boolean isPrepared = false;//是否准备好
    protected boolean isStartWhenPrepared = true;//是否准备好立即播放
    protected HOnListener<Object> preparedListener;
    protected HOnListener<Object> completionListener;

    enum PlayerState {
        ERROR,//出错
        IDLE,//闲置，初始化
        PREPARING,//PREPAR中
        PREPARED,//PREPAR完毕
        INITIALIZED,//资源设置完毕
        PLAYING,//播放中
        PAUSED,//暂停中
        PHONE,//通话中
        PLAYBACK_COMPLETED,//播放完成
        END//结束，需要重新new
    }

    public abstract void prepare(String mediaPath);

    public abstract void start();

    public abstract void pause();

    public abstract void stop();

    public abstract void seekTo(long position);

    public abstract void release();

    public abstract void setVolume(float volume);

    public abstract long getCurrentPosition();

    public abstract long getMediaDuration();


    public void setSurface(SurfaceView surface) {

    }

    public void setTexture(TextureView texture) {

    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public void setStartWhenPrepared(boolean startWhenPrepared) {
        isStartWhenPrepared = startWhenPrepared;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public PlayerState getmPlayerState() {
        return mPlayerState;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void setCompletionListener(HOnListener<Object> completionListener) {
        this.completionListener = completionListener;
    }
}
