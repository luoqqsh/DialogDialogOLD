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
        if (mediaPlayer != null && isCanControl()) {
            return mediaPlayer.getCurrentPosition();
        } else {
            Log.e(TAG, "获取当前位置失败" + mPlayerState);
            return 0;
        }
    }

    @Override
    public long getMediaDuration() {
        if (isCanControl() && mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            Log.e(TAG, "获取总时长失败" + mPlayerState);
            return 0;
        }
    }

    @Override
    public void prepare(String mediaPath) {
        musicPath = mediaPath;
        doPrepare(mediaPath, true);
    }

    @Override
    public void start() {
        if (!isPlaying() && isPrepared) {
            try {
                mediaPlayer.start();
                mPlayerState = PlayerState.PLAYING;
                Log.e(TAG, "doStart");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "start失败" + mPlayerState);
                mPlayerState = PlayerState.ERROR;
                dealError();
            }
        }
    }

    @Override
    public void pause() {
        try {
            if (isPlaying()) {
                mediaPlayer.pause();
                mPlayerState = PlayerState.PAUSED;
                Log.e(TAG, "doPause");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "pause失败" + mPlayerState);
            mPlayerState = PlayerState.ERROR;
        }
    }

    @Override
    public void seekTo(long position) {
        if (mediaPlayer != null && isCanControl()) {
            mPlayerState = PlayerState.PREPARING;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.seekTo(position, MediaPlayer.SEEK_CLOSEST);
            } else {
                mediaPlayer.seekTo((int) position);
            }
        }
    }

    @Override
    public void stop() {
        if (isCanControl()) {
            try {
                //调用stop方法之后要调用prepare方法，不然无法播放
                mediaPlayer.stop();
                isPrepared = false;
                mPlayerState = PlayerState.IDLE;
                Log.e(TAG, "doStop");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "stop失败" + mPlayerState);
                mPlayerState = PlayerState.ERROR;
            }
        }
    }

    @Override
    public void reset() {
        mediaPlayer.reset();
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            stop();
            mediaPlayer.release();
            mPlayerState = PlayerState.NONE;
            isPrepared = false;
            mediaPlayer = null;
            Log.e(TAG, "doRelease");
        }
    }

    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        mediaPlayer.setVolume(volume, volume);
    }

    private void doPrepare(String mediaPath, boolean isFirst) {
        if (!(new File(mediaPath)).exists()) {
            ViseLog.e("指定媒体文件不存在。  " + mediaPath);
            return;
        }
        this.musicPath = mediaPath;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }
        mPlayerState = PlayerState.IDLE;
        try {
            mediaPlayer.setDataSource(mediaPath);
            mPlayerState = PlayerState.INITIALIZED;
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.setLooping(isLooping);
            mPlayerState = PlayerState.PREPARING;
            mediaPlayer.prepareAsync();//异步
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayerState = PlayerState.PREPARED;
                    if (preparedListener != null) {
                        preparedListener.onListen(mp);
                    }
                    isPrepared = true;
                    Log.e(TAG, "Prepared OK");
                    if (isStartWhenPrepared) {
                        mp.start();
                        mPlayerState = PlayerState.PLAYING;
                    }
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    Log.e(TAG, "Seek Complete");
                    mp.start();
                    mPlayerState = PlayerState.PLAYING;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e(TAG, "play Completion");
                    mPlayerState = PlayerState.PLAYBACK_COMPLETED;
                    if (completionListener != null) {
                        completionListener.onListen(mp);
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayerState = PlayerState.ERROR;
                    ViseLog.e("ERROR: " + what + "  " + extra);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mPlayerState = PlayerState.ERROR;
            Log.e(TAG, "Prepare失败。  " + musicPath);
            if (isFirst) dealError();
        }
    }

    private void dealError() {
        doPrepare(musicPath, false);
    }

    private boolean isPlaying() {
        return mPlayerState == PlayerState.PLAYING && mediaPlayer.isPlaying();
    }

    private boolean isCanControl() {
        return mPlayerState == PlayerState.PLAYING || mPlayerState == PlayerState.PAUSED || mPlayerState == PlayerState.PHONE
                || mPlayerState == PlayerState.PLAYBACK_COMPLETED || mPlayerState == PlayerState.PREPARED;
    }
}
