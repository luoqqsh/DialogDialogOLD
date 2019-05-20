package x.com.hl_player;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.File;

import x.com.hl_log.ViseLog;

/**
 * MediaPlayer具体实现
 */
public class HHMediaPlayer extends HHPlayerBase {
    private MediaPlayer mediaPlayer;

    @Override
    public void setSurface(SurfaceView surfaceView) {
        TAG = "HH-MediaPlayer-surface";
        mediaPlayer = new MediaPlayer();
        initListener();
        surfaceView.setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                release();
            }
        });
    }

    @Override
    public void setTexture(TextureView textureView) {
        TAG = "HH-MediaPlayer-texture";
        mediaPlayer = new MediaPlayer();
        initListener();
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                mediaPlayer.setSurface(new Surface(surfaceTexture));
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if (surface != null) {
                    surface.release();
                }
                release();
                return true;
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getMediaDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void prepare(String mediaPath) {
        if ("HHPlayerBase".equals(TAG)) {
            TAG = "HH-MediaPlayer-Music";
        }
        this.mediaPath = mediaPath;
        doPrepare(mediaPath, true);
    }

    @Override
    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
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
        }
    }

    @Override
    public void stop() {
        //调用stop方法之后要调用prepare方法，不然无法播放
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public void setSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }
    }

    private void initListener() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    playerPrePareState = PlayerPrePareState.PREPARED;
                    mPlayerState = PlayerState.PREPARED;
                    isPrepared = true;
                    mediaPlayer.setVolume(volume, volume);
                    mediaPlayer.setLooping(isLooping);
                    if (preparedListener != null) {
                        preparedListener.onListen(mp);
                    }
                    Log.e(TAG, "Prepared OK");
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mp.start();
                    Log.e(TAG, "Seek Complete");
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (completionListener != null) {
                        completionListener.onListen(mp);
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "监听到错误。  " + mPlayerState);
                    mPlayerState = PlayerState.ERROR;
                    playerPrePareState = PlayerPrePareState.ERROR;
                    ViseLog.e("ERROR: " + what + "  " + extra);
                    return false;
                }
            });
        }
    }

    private void doPrepare(String mediaPath, boolean isFirst) {
        if (!new File(mediaPath).exists()) {
            ViseLog.e("媒体文件不存在。  " + mediaPath);
            return;
        }
        this.mediaPath = mediaPath;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
            initListener();
        }
        mPlayerState = PlayerState.IDLE;
        try {
            playerPrePareState = PlayerPrePareState.PREPARING;
            mediaPlayer.setDataSource(mediaPath);
            mediaPlayer.prepareAsync();//异步
        } catch (Exception e) {
            e.printStackTrace();
            mPlayerState = PlayerState.ERROR;
            playerPrePareState = PlayerPrePareState.ERROR;
            Log.e(TAG, "Prepare失败。  " + this.mediaPath);
            if (isFirst) dealError();
        }
    }

    private void dealError() {
        doPrepare(mediaPath, false);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}