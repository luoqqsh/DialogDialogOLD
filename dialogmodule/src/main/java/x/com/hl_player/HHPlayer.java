package x.com.hl_player;

import android.view.SurfaceView;
import android.view.TextureView;

import x.com.hl_base.HOnListener;
import x.com.hl_log.ViseLog;

/**
 * 播放器控制类，也就是我们的使用类
 */
public class HHPlayer {
    private HHPlayerBase hhPlayerBase;//播放器内核

    public HHPlayer(HHPlayerBase hhPlayerBase) {
        this.hhPlayerBase = hhPlayerBase;
    }

    public HHPlayer(HHPlayerBase hhPlayerBase, SurfaceView surface) {
        this.hhPlayerBase = hhPlayerBase;
        hhPlayerBase.setSurface(surface);
    }

    public HHPlayer(HHPlayerBase hhPlayerBase, TextureView texture) {
        this.hhPlayerBase = hhPlayerBase;
        hhPlayerBase.setTexture(texture);
    }

    public void prepare(String path) {
        hhPlayerBase.prepare(path);
    }

    public void setLooping(boolean looping) {
        hhPlayerBase.setLooping(looping);
    }

    public void setMute(boolean mute) {
        setVolume(mute ? 0 : 1);
    }

    public void setVolume(float volume) {
        if (volume >= 0 && volume <= 1) {
            hhPlayerBase.setVolume(volume);
        } else {
            hhPlayerBase.setVolume(1);
            ViseLog.e("volume超范围：" + volume);
        }
    }

    public void setStartWhenPrepared(boolean startWhenPrepared) {
        hhPlayerBase.setStartWhenPrepared(startWhenPrepared);
    }

    public void start() {
        hhPlayerBase.start();
    }

    public void pause() {
        hhPlayerBase.pause();
    }

    public void reset() {
        hhPlayerBase.reset();
    }

    public void stop() {
        hhPlayerBase.stop();
    }

    //单位毫秒
    public void seekTo(long position) {
        hhPlayerBase.seekTo(position);
    }

    public void release() {
        hhPlayerBase.release();
    }

    public String getMediaPath() {
        return hhPlayerBase.getMusicPath();
    }

    public long getCurrentPosition() {
        return hhPlayerBase.getCurrentPosition();
    }

    public long getMediaDuration() {
        return hhPlayerBase.getMediaDuration();
    }

    public void setPreparedListener(HOnListener<Object> preparedListener) {
        hhPlayerBase.setPreparedListener(preparedListener);
    }

    public void setCompletionListener(HOnListener<Object> completionListener) {
        hhPlayerBase.setCompletionListener(completionListener);
    }

    public HHPlayerBase.PlayerState getState() {
        return hhPlayerBase.getmPlayerState();
    }

    public boolean isPrepared() {
        return hhPlayerBase.isPrepared();
    }

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
}
