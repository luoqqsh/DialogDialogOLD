package com.xing.hhplayer.common;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.File;

import x.com.hl_log.ViseLog;

/**
 * Created by Lyd on 2019/4/7.
 * MediaPlayer媒体控制器
 */
public class MediaPlayerController1 {
    private MediaPlayer mediaPlayer;
    private String TAG = "MediaPlayerController1";
    private String musicPath;
    private boolean isStartWhenPrepared = true;
    private float volume = 1;//范围0-1
    //进度单位统一为毫秒;
    private PreparedListener preparedListener;
    private PlayerState mPlayerState = PlayerState.IDLE;
    private boolean isLooping = true; //是否循环播放
    private boolean isPrepared = false;

    public enum PlayerState {
        ERROR,//出错
        IDLE,//闲置，初始化
        PREPARING,//PREPAR中
        PREPARED,//PREPAR完毕
        PLAYING,//播放中
        PAUSED,//暂停中
        PLAYBACK_COMPLETED,//播放完成
        INITIALIZED,//资源设置完毕
        END//结束，需要重新new
    }

    public MediaPlayerController1() {
        mediaPlayer = new MediaPlayer();
    }

    public MediaPlayerController1(TextureView textureView) {
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
                    surface = null;
                }
                doRelease();
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

    public MediaPlayerController1(SurfaceView surfaceView) {
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
                doRelease();
            }
        });
    }

    public interface PreparedListener {
        void onPreparedListener(MediaPlayer mediaPlayer);
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public void setStartWhenPrepared(boolean startWhenPrepared) {
        isStartWhenPrepared = startWhenPrepared;
    }

    public void setMediaTag(String TAG) {
        this.TAG = "MediaPlayerController1-" + TAG;
    }

    public void setOnPreparedListener(PreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void doPrepare(String mediaPath) {
        doPrepare(mediaPath, true);
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
                        preparedListener.onPreparedListener(mp);
                    }
                    isPrepared = true;
                    ViseLog.e(TAG, "Prepared OK");
                    if (MediaPlayerController1.this.isStartWhenPrepared) {
                        mp.start();
                    }
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayerState = PlayerState.PLAYBACK_COMPLETED;
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
            ViseLog.e(TAG, "Prepare失败。  " + musicPath);
            if (isFirst) dealError();
        }
    }

    public void seekTo(int position) {
        if (mediaPlayer != null && mPlayerState == PlayerState.PREPARED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.seekTo(position, MediaPlayer.SEEK_CLOSEST);
            } else {
                mediaPlayer.seekTo(position);
            }
        }
    }

    public void doStart() {
        if (!isPlaying() && isPrepared) {
            try {
                mediaPlayer.start();
                mPlayerState = PlayerState.PLAYING;
                ViseLog.e(TAG, "doStart");
            } catch (Exception e) {
                e.printStackTrace();
                mPlayerState = PlayerState.ERROR;
                dealError();
            }
        }
    }

    public void doPause() {
        try {
            if (isPlaying()) {
                mediaPlayer.pause();
                mPlayerState = PlayerState.PAUSED;
                ViseLog.e(TAG, "doPause");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPlayerState = PlayerState.ERROR;
        }
    }

    public void doStop() {
        if (mPlayerState != PlayerState.IDLE) {
            try {
                //调用stop方法之后要调用prepare方法，不然无法播放
                mediaPlayer.stop();
                isPrepared = false;
                mPlayerState = PlayerState.IDLE;
                ViseLog.e(TAG, "doStop");
            } catch (Exception e) {
                e.printStackTrace();
                mPlayerState = PlayerState.ERROR;
            }
        }
    }

    private void dealError() {
        doPrepare(musicPath, false);
    }

    public void setVoice(float volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            try {
                mediaPlayer.setVolume(volume, volume);
            } catch (Exception e) {
                e.printStackTrace();
                mPlayerState = PlayerState.ERROR;
            }
        }
    }

    public String getDataSource() {
        return musicPath;
    }

    public int getDuration() {
        if (mPlayerState == PlayerState.PLAYING && mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            ViseLog.e(TAG, "媒体文件未准备好");
            return 0;
        }
    }

    public PlayerState getPlayerState() {
        return mPlayerState;
    }

    public boolean isPlaying() {
        return mPlayerState == PlayerState.PLAYING && mediaPlayer.isPlaying();
    }

    public void doRelease() {
        if (mediaPlayer != null) {
            doStop();
            mediaPlayer.release();
            isStartWhenPrepared = false;
            mPlayerState = PlayerState.END;
            isPrepared = false;
            mediaPlayer = null;
            ViseLog.e(TAG, "doRelease");
        }
    }
}
