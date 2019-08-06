package x.com.hl_player.base;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.lang.reflect.Method;
import java.util.Map;

import x.com.hl_log.ViseLog;

/**
 * MediaPlayer具体实现
 */
public class HHMediaPlayer extends HHPlayerBase {
    private MediaPlayer mediaPlayer;

    @Override
    public void initView(SurfaceView surfaceView) {
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
                holder.removeCallback(this);
                release();
            }
        });
    }

    @Override
    public void initView(TextureView textureView) {
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
    public void initView(Surface surface) {
        TAG = "HH-MediaPlayer-surface";
        mediaPlayer = new MediaPlayer();
        initListener();
        mediaPlayer.setSurface(surface);
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
                    if (isPlayWhenPrepared) {
                        start();
                    }
                    if (onPreparedListener != null) {
                        onPreparedListener.onListen(mp);
                    }
                    StringBuilder videoInfoLog = new StringBuilder("Prepared OK\n");
                    videoInfoLog.append("width: ").append(mediaPlayer.getVideoWidth()).append("\n");
                    videoInfoLog.append("height: ").append(mediaPlayer.getVideoHeight()).append("\n");
                    videoInfoLog.append("duration: ").append(mediaPlayer.getDuration());
                    ViseLog.d(videoInfoLog);
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    start();
                    Log.d(TAG, "Seek Complete");
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isCompletePlay = true;
                    if (onCompletionListener != null) {
                        onCompletionListener.onListen(mp);
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(ETAG, "监听到错误。  " + mPlayerState);
                    mPlayerState = PlayerState.ERROR;
                    playerPrePareState = PlayerPrePareState.ERROR;
                    Log.e(ETAG, what + "  " + extra + "\n" + "播放出错：" + mediaPath);
                    return false;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    if (onBufferingUpdateListener != null) {
                        onBufferingUpdateListener.onListen(percent);
                    }
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                    ViseLog.d(i);
                    ViseLog.d(i1);
                    return false;
                }
            });
        }
    }

    private void doPrepare(Object mediaPath, boolean isFirst) {
        if (mediaPath == null) {
            Log.e(ETAG, "地址为空或找不到！");
            return;
        }
        this.mediaPath = mediaPath;
        stop();
        reset();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            initListener();
        }
        mPlayerState = PlayerState.IDLE;
        try {
            playerPrePareState = PlayerPrePareState.PREPARING;
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
            mPlayerState = PlayerState.ERROR;
            playerPrePareState = PlayerPrePareState.ERROR;
            Log.e(ETAG, "Prepare失败。  " + this.mediaPath);
            if (isFirst) dealError();
        }
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getCurrentPosition() > getMediaDuration()) {
                return getMediaDuration();
            }
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
    public void prepare(Object mediaPath) {
        if ("HHPlayerBase".equals(TAG)) {
            TAG = "HH-MediaPlayer-Music";
        }
        this.mediaPath = mediaPath;
        doPrepare(mediaPath, true);
    }

    @Override
    public void start() {
        isCompletePlay = false;
        if (mediaPlayer != null) {
            mediaPlayer.start();
            getCurrentStart();
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
        //调用stop方法之后要重新调用prepare方法，不然无法播放
        if (mediaPlayer != null && isPlayControl()) {
            mediaPlayer.stop();
            isPrepared = false;
            mPlayerState = HHPlayerBase.PlayerState.IDLE;
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
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public void setSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mediaPlayer != null) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
            }
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