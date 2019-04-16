package x.com.hl_player;

import android.view.Surface;

import java.util.Map;


public abstract class AbsMediaPlayer {
    //正在播放的当前url或uri
    private Object dataSource;
    //设置header信息
    private Map<String, String> mHeaders;

    //播放
    public abstract void start();

    //准备
    public abstract void prepare();

    //暂停
    public abstract void pause();

    //是否正在播放
    public abstract boolean isPlaying();

    //定位，long类型
    public abstract void seekTo(long time);

    //释放
    public abstract void release();

    //获取当前进度
    public abstract long getCurrentPosition();

    //获取总时长
    public abstract long getDuration();

    //设置surface
    public abstract void setSurface(Surface surface);

    //设置音量
    public abstract void setVolume(float leftVolume, float rightVolume);

    public abstract void mute(boolean isMute);

    //设置是否循环
    public abstract void setLooping(boolean isLoop);

    public Object getDataSource() {
        return dataSource;
    }

    public void setDataSource(Object dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }


    public void setHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
    }
}
