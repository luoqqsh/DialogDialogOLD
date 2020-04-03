package com.xing.hhplayer.common.base;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioDeviceInfo;
import android.media.MediaPlayer;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;

import java.lang.reflect.Method;
import java.util.Map;

import x.com.hl_log.ViseLog;

/**
 * MediaPlayer具体实现
 */
public class HHMediaPlayer extends HHPlayerCore {
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private Surface surface;

    public HHMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        initListener();
    }

    @Override
    public void initView(SurfaceHolder surfaceHolder) {
        if (surfaceHolder != null) {
            this.surfaceHolder = surfaceHolder;
            if (mediaPlayer != null) {
                mediaPlayer.setDisplay(surfaceHolder);
                ViseLog.showInfo(TAG, "surfaceHolder设置成功");
            }
        } else {
            onCoreErrorListener.onListen("surfaceHolder为空！");
        }
    }

    @Override
    public void initView(Surface surface) {
        if (surface != null) {
            this.surface = surface;
            if (mediaPlayer != null && surface.isValid()) {
                mediaPlayer.setSurface(surface);
                ViseLog.showInfo(TAG, "surface设置成功");
            }
        } else {
            onCoreErrorListener.onListen("surface为空！");
        }
    }

    private void initListener() {
        if (mediaPlayer != null) {
            mediaPlayer.setScreenOnWhilePlaying(screenOnWhilePlaying);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    playerPrePareState = PlayerPrePareStateType.PREPARED;
                    mPlayerState = PlayerStateType.PREPARED;
                    videoWidth = mediaPlayer.getVideoWidth();
                    videoHeight = mediaPlayer.getVideoHeight();
                    setVolume(volume);
                    mediaPlayer.setLooping(isLooping);
                    onCorePreparedListener.onListen(mp);
                    StringBuilder videoInfoLog = new StringBuilder("Prepared OK\n");
                    videoInfoLog.append("path: ").append(mediaPath).append("\n");
                    videoInfoLog.append("width: ").append(mediaPlayer.getVideoWidth()).append("\n");
                    videoInfoLog.append("height: ").append(mediaPlayer.getVideoHeight()).append("\n");
                    videoInfoLog.append("duration: ").append(mediaPlayer.getDuration()).append("\n");
                    videoInfoLog.append("tag: ").append(TAG);
                    ViseLog.d(TAG, videoInfoLog);
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mPlayerState = PlayerStateType.PLAYING;
                    start();
                    ViseLog.showInfo(TAG, "Seek Complete");
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onCoreCompletionListener.onListen(mp);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayerState = PlayerStateType.ERROR;
                    playerPrePareState = PlayerPrePareStateType.ERROR;
                    String err = "player播放出错：" + what + " " + extra + " " + mPlayerState;
                    onCoreErrorListener.onListen(err);
                    return false;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    if (onCoreBufferingUpdateListener != null) {
                        onCoreBufferingUpdateListener.onListen(percent);
                    }
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int arg0, int arg1) {
                    int arg = 0;
                    switch (arg0) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            //第一次播放刚渲染完
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲
                            arg = 1;
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END://结束缓冲
                            arg = 100;
                            break;
                    }
                    onCoreInfoListener.onListen(arg);
                    return false;
                }
            });
        }
    }

    @Override
    public long getCurrentPositionMS() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getCurrentPosition() > getMediaDurationMS()) {
                return getMediaDurationMS();
            }
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getMediaDurationMS() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        if (mediaPlayer != null) {
            return getNetSpeed(context);
        }
        return 0;
    }

    @Override
    public void prepare(Uri mediaUri) {
        if (mediaUri == null) {
            onCoreErrorListener.onListen("地址为空！");
            return;
        }
        this.mediaUri = mediaUri;
        stop();
        reset();
        try {
            mediaPlayer.setDataSource(context, mediaUri);
            mediaPlayer.prepareAsync();//异步
        } catch (Exception e) {
            e.printStackTrace();
            mPlayerState = PlayerStateType.ERROR;
            playerPrePareState = PlayerPrePareStateType.ERROR;
            onCoreErrorListener.onListen("Prepare异常");
        }
    }

    @Override
    public void prepare(String path) {
        if (path == null) {
            onCoreErrorListener.onListen("地址为空！");
            return;
        }
        this.mediaPath = path;
        stop();
        reset();
        try {
            playerPrePareState = PlayerPrePareStateType.PREPARING;
            if (mediaPath instanceof AssetFileDescriptor) {//Android assets file
                AssetFileDescriptor fd = (AssetFileDescriptor) mediaPath;
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            } else if (getHeaders() != null) {//url with headers
                Class<MediaPlayer> clazz = MediaPlayer.class;
                Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
                method.invoke(mediaPlayer, mediaPath.toString(), getHeaders());
            } else {//本地、网络地址
                mediaPlayer.setDataSource(mediaPath.toString());
            }
            mediaPlayer.prepareAsync();//异步
        } catch (Exception e) {
            e.printStackTrace();
            mPlayerState = PlayerStateType.ERROR;
            playerPrePareState = PlayerPrePareStateType.ERROR;
            onCoreErrorListener.onListen("Prepare异常");
        }
    }

    @Override
    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            ViseLog.showInfo(TAG, "开始");
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            ViseLog.showInfo(TAG, "暂停");
        }
    }

    @Override
    public void seekTo(long position) {
        if (mediaPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.seekTo(position, MediaPlayer.SEEK_CLOSEST);
            } else {
                mediaPlayer.seekTo((int) position);
            }
            ViseLog.showInfo(TAG, "seekTo:" + position);
        }
    }

    @Override
    public void stop() {
        //调用stop方法之后要重新调用prepare方法，不然无法播放
        if (mediaPlayer != null && isPlayControl()) {
            mediaPlayer.stop();
            playerPrePareState = PlayerPrePareStateType.NONE;
            mPlayerState = PlayerStateType.IDLE;
            ViseLog.showInfo(TAG, "停止");
        }
    }

    @Override
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            ViseLog.showInfo(TAG, "重置");
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            context = null;
            ViseLog.showInfo(TAG, "释放");
        }
    }

    @Override
    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
            ViseLog.showInfo(TAG, "设置音量:" + volume);
        }
    }

    @Override
    public void setScalingMode(int scalingMode) {
        if (videoScalingMode != scalingMode) {
            videoScalingMode = scalingMode;
            if (mediaPlayer != null) {
                mediaPlayer.setVideoScalingMode(scalingMode);
            }
        }
    }

    @Override
    public void resetPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        initListener();
        if (surface != null) {
            if (surface.isValid()) {
                mediaPlayer.setSurface(surface);
                ViseLog.showInfo(TAG, "surface设置成功");
            }
        }
        if (surfaceHolder != null) {
            mediaPlayer.setDisplay(surfaceHolder);
            ViseLog.showInfo(TAG, "surfaceHolder设置成功");
        }
    }

    @Override
    public void setSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mediaPlayer != null) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
                ViseLog.showInfo(TAG, "速度:" + speed);
            }
        } else {
            onCoreErrorListener.onListen("速度设置失败，系统必须大于6.0！");
        }
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    private long getNetSpeed(Context context) {
        if (context == null) {
            return 0;
        }
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
        long nowTimeStamp = System.currentTimeMillis();
        long calculationTime = (nowTimeStamp - lastTimeStamp);
        if (calculationTime == 0) {
            return calculationTime;
        }
        //毫秒转换
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / calculationTime);
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }

    public int getVideoSarNum() {
        return 1;
    }

    public int getVideoSarDen() {
        return 1;
    }
}