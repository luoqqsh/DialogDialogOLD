package x.com.hl_player;

import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.File;

import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_media;

/**
 * Created by Lyd on 2019/4/7.
 * 音乐控制器
 */
public class MediaController {
    private MediaPlayer mediaPlayer;
    private String TAG = "MediaController";
    private String musicPath;
    private boolean isStartWhenPrepared = true;
    private float volume = 1;//范围0-1
    //进度单位统一为毫秒;
    private boolean isPrepared = false;
    private PreparedListener preparedListener;
    private SurfaceView surfaceView;

    public interface PreparedListener {
        void onPreparedListener(MediaPlayer mediaPlayer);
    }

    public void setMediaTag(String TAG) {
        this.TAG = "MediaController-" + TAG;
    }

    public void setOnPreparedListener(PreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void doPrepare(String mediaPath, boolean isStartWhenPrepared) {
        doPrepare(mediaPath, null, isStartWhenPrepared, true);
    }

    public void doPrepare(String mediaPath, SurfaceView surfaceView, boolean isStartWhenPrepared) {
        doPrepare(mediaPath, surfaceView, isStartWhenPrepared, true);
    }

    private void doPrepare(String mediaPath, final SurfaceView surfaceView, boolean isStartWhenPrepared, boolean isFirst) {
        if (!(new File(mediaPath)).exists()) {
            ViseLog.e("指定媒体文件不存在。  " + mediaPath);
            return;
        }
        this.musicPath = mediaPath;
        this.surfaceView = surfaceView;
        this.isStartWhenPrepared = isStartWhenPrepared;
        isPrepared = false;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }
        if (surfaceView != null) {
            initVideo(mediaPath, surfaceView, isFirst);
        } else {
            initMediaPlayer(mediaPath, isFirst);
        }
    }

    private void initMediaPlayer(String mediaPath, boolean isFirst) {
        try {
            mediaPlayer.setDataSource(mediaPath);
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();//异步
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                    if (preparedListener != null) {
                        preparedListener.onPreparedListener(mp);
                    }
                    Log.e(TAG, "Prepared OK");
                    if (MediaController.this.isStartWhenPrepared) {
                        mp.start();
                    }
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    try {
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        doPrepare(MediaController.this.musicPath, surfaceView, isStartWhenPrepared);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Prepare失败。  " + musicPath);
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (isFirst) doPrepare(mediaPath, surfaceView, isStartWhenPrepared, false);
        }
    }

    private void initVideo(final String path, SurfaceView surfaceView, final boolean isFirst) {
        ViseLog.d(hl_media.isVedioFile(path));
        if (surfaceView != null && hl_media.isVedioFile(path)) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback2() {
                @Override
                public void surfaceRedrawNeeded(SurfaceHolder holder) {

                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mediaPlayer.setDisplay(holder);
                    initMediaPlayer(path, isFirst);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    doRelease();
                }
            });
        }
    }

    public void seekTo(int position) {
        if (mediaPlayer != null && isPrepared) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.seekTo(position, MediaPlayer.SEEK_CLOSEST);
            } else {
                mediaPlayer.seekTo(position);
            }
        }
    }

    public String getMusicPath() {
        return musicPath;
    }

    public int getDuration() {
        if (isPrepared && mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            Log.e(TAG, "媒体文件未准备好");
            return 0;
        }
    }

    public int getCurrentPosition() {
        if (isPrepared && mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            Log.e(TAG, "媒体文件未准备好");
            return 0;
        }
    }

    public void doStart() {
        isStartWhenPrepared = true;
        if (mediaPlayer != null && isPrepared) {
            try {
                mediaPlayer.start();
                Log.e(TAG, "doStart");
            } catch (Exception e) {
                e.printStackTrace();
                doPrepare(musicPath, surfaceView, isStartWhenPrepared);
            }
        }
    }

    public void doPause() {
        try {
            if (isStartWhenPrepared && isPrepared) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    Log.e(TAG, "doPause");
                }
            }
            isStartWhenPrepared = false;
        } catch (Exception e) {
            e.printStackTrace();
            doRelease();
        }
    }

    public void doStop() {
        if (isStartWhenPrepared && isPrepared) {
            try {
                if (mediaPlayer != null) {
                    //调用stop方法之后要调用reset方法，不然无法播放
                    mediaPlayer.stop();
                    Log.e(TAG, "doStop");
                }
            } catch (Exception e) {
                e.printStackTrace();
                doRelease();
            }
        }
        isStartWhenPrepared = false;
    }

    public void setVoice(float volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            try {
                mediaPlayer.setVolume(volume, volume);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPlaying() {
        if (isPrepared && isStartWhenPrepared && mediaPlayer != null) {
            try {
                return mediaPlayer.isPlaying();
            } catch (Exception e) {
                e.printStackTrace();
                return isStartWhenPrepared;
            }
        }
        return false;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void doRelease() {
        try {
            if (mediaPlayer != null) {
                isPrepared = false;
                isStartWhenPrepared = false;
                mediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "doRelease");
    }

    public void onDestroy() {
        doRelease();
        mediaPlayer = null;
        Log.e(TAG, "onDestroy");
    }
}
