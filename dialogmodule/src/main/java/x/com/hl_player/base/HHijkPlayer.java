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

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import x.com.hl_log.ViseLog;

public class HHijkPlayer extends HHPlayerBase {
    private IjkMediaPlayer mediaPlayer;
    private Surface surface;

    @Override
    public void initView(SurfaceView surfaceView) {
        TAG = "HH-IJKPlayer-surface";
        mediaPlayer = new IjkMediaPlayer();
        initListener();
        surfaceView.setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surface = holder.getSurface();
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
        TAG = "HH-IJKPlayer-texture";
        mediaPlayer = new IjkMediaPlayer();
        initListener();
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                surface = new Surface(surfaceTexture);
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
        TAG = "HH-IJKPlayer-surface";
        this.surface = surface;
        mediaPlayer = new IjkMediaPlayer();
        initListener();
    }

    private void initListener() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(new IjkMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
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
            mediaPlayer.setOnSeekCompleteListener(new IjkMediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(IMediaPlayer mp) {
                    start();
                    Log.d(TAG, "Seek Complete");
                }
            });
            mediaPlayer.setOnCompletionListener(new IjkMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer mp) {
                    isCompletePlay = true;
                    if (onCompletionListener != null) {
                        onCompletionListener.onListen(mp);
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new IjkMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "监听到错误。  " + mPlayerState);
                    mPlayerState = PlayerState.ERROR;
                    playerPrePareState = PlayerPrePareState.ERROR;
                    Log.e(TAG, what + "  " + extra + "\n" + "播放出错：" + mediaPath);
                    return false;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new IjkMediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    if (onBufferingUpdateListener != null) {
                        onBufferingUpdateListener.onListen(percent);
                    }
                }
            });
            initSetting();
        }
    }

    private void doPrepare(Object mediaPath, boolean isFirst) {
        this.mediaPath = mediaPath;
        if (mediaPlayer == null) {
            mediaPlayer = new IjkMediaPlayer();
            initListener();
        } else {
            stop();
            reset();//ijkplayer调用reset会连画布一起重置，画布不会销毁
            mediaPlayer.setSurface(surface);
        }
        mPlayerState = PlayerState.IDLE;
        try {
            playerPrePareState = PlayerPrePareState.PREPARING;
            if (mediaPath instanceof AssetFileDescriptor) {//Android assets file
//                AssetFileDescriptor fd = (AssetFileDescriptor) mediaPath;
//                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            } else if (mediaPath != null && getHeaders() != null) {//url with headers
                Class<MediaPlayer> clazz = MediaPlayer.class;
                Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
                method.invoke(mediaPlayer, mediaPath.toString(), getHeaders());
            } else if (mediaPath != null) {
                mediaPlayer.setDataSource(mediaPath.toString());
            }
            mediaPlayer.prepareAsync();//异步
        } catch (Exception e) {
            e.printStackTrace();
            mPlayerState = PlayerState.ERROR;
            playerPrePareState = PlayerPrePareState.ERROR;
            Log.e(TAG, "Prepare失败。  " + this.mediaPath);
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
            TAG = "HH-IJKPlayer-Music";
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
            mediaPlayer.seekTo((int) position);
        }
    }

    @Override
    public void stop() {
        //调用stop方法之后要调用prepare方法，不然无法播放
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPrepared = false;
            mPlayerState = PlayerState.IDLE;
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
//            if (mediaPlayer != null) {
//                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
//            }
        }
    }


    private void dealError() {
        doPrepare(mediaPath, false);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private void initSetting() {
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 30);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_YV12);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 1024);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 3);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probsize", "4096");
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", "2000000");

        // 最大缓冲cache是3s， 有时候网络波动，会突然在短时间内收到好几秒的数据
// 因此需要播放器丢包，才不会累积延时
// 这个和第三个参数packet-buffering无关。
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 3000);
// 无限制收流
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);

// 设置无缓冲，这是播放器的缓冲区，有数据就播放
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);

// 可选，有时候tcp模式出画面更快，因为rtsp是先udp，不成功再切到tcp的
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");

// 设置在解析的 url 之前 （这里设置超时为5秒）
// 如果没有设置stimeout，在解析时（也就是avformat_open_input）把网线拔掉，av_read_frame会阻塞（时间单位是微妙）
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "stimeout", "5000000");
    }
}