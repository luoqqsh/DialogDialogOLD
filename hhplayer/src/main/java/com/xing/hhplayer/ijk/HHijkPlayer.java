package com.xing.hhplayer.ijk;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.base.HHPlayerCore;
import com.xing.hhplayer.common.base.RawDataSourceProvider;

import java.lang.reflect.Method;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_uri;

public class HHijkPlayer extends HHPlayerCore {
    private IjkMediaPlayer mediaPlayer;
    private Surface surface;
    private SurfaceHolder surfaceHolder;

    public HHijkPlayer() {
        mediaPlayer = new IjkMediaPlayer();
        initListener();
    }

    @Override
    public void initView(Surface surface) {
        if (surface.isValid() && mediaPlayer != null) {
            this.surface = surface;
            mediaPlayer.setSurface(surface);
            ViseLog.showLog(TAG, "surface设置成功");
        }
    }

    @Override
    public void initView(SurfaceHolder surfaceHolder) {
        if (mediaPlayer != null) {
            this.surface = surfaceHolder.getSurface();
            mediaPlayer.setSurface(surface);
            ViseLog.showLog(TAG, "surfaceHolder设置成功");
        }
    }

    private void initListener() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(new IjkMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    playerPrePareState = PlayerPrePareStateType.PREPARED;
                    mPlayerState = PlayerStateType.PREPARED;
                    videoWidth = mediaPlayer.getVideoWidth();
                    videoHeight = mediaPlayer.getVideoHeight();
                    mediaPlayer.setVolume(volume, volume);
                    mediaPlayer.setLooping(isLooping);
                    onCorePreparedListener.onListen(mp);
                    StringBuilder videoInfoLog = new StringBuilder("Prepared OK\n");
                    videoInfoLog.append("path: ").append(mediaPath).append("\n");
                    videoInfoLog.append("width: ").append(mediaPlayer.getVideoWidth()).append("\n");
                    videoInfoLog.append("height: ").append(mediaPlayer.getVideoHeight()).append("\n");
                    videoInfoLog.append("duration: ").append(mediaPlayer.getDuration()).append("\n");
                    videoInfoLog.append("tag: ").append(TAG);
                    ViseLog.d(videoInfoLog);
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new IjkMediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(IMediaPlayer mp) {
                    start();
                    ViseLog.showLog(TAG, "Seek Complete");
                }
            });
            mediaPlayer.setOnCompletionListener(new IjkMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer mp) {
                    onCoreCompletionListener.onListen(mp);
                }
            });
            mediaPlayer.setOnErrorListener(new IjkMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer mp, int what, int extra) {
                    mPlayerState = PlayerStateType.ERROR;
                    playerPrePareState = PlayerPrePareStateType.ERROR;
                    String err = "ijk播放出错：" + what + " " + extra + " " + mPlayerState + "\n" + mediaPath;
                    onCoreErrorListener.onListen(err);
                    return false;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new IjkMediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    onCoreBufferingUpdateListener.onListen(percent);
                }
            });
            initSetting();
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
    public void prepare(String path) {
        if (path == null) {
            onCoreErrorListener.onListen("地址为空或不支持！");
            return;
        }
        if ("HHPlayer3".equals(TAG)) {
            TAG = "HH-IJKPlayer-Music";
        }
        this.mediaPath = path;
        stop();
        reset();//ijkplayer调用reset会连画布一起重置，画布不会销毁
        try {
            playerPrePareState = PlayerPrePareStateType.PREPARING;
            Uri uri = Uri.parse((String) mediaPath);
            if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
                AssetFileDescriptor fileDescriptor = context.getApplicationContext().getContentResolver().openAssetFileDescriptor(hl_uri.str2Uri(String.valueOf(this.mediaPath)), "r");
                mediaPlayer.setDataSource(new RawDataSourceProvider(fileDescriptor));
            } else if (getHeaders() != null) {//url with headers
                Class<MediaPlayer> clazz = MediaPlayer.class;
                Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
                method.invoke(mediaPlayer, mediaPath, getHeaders());
            } else {
                mediaPlayer.setDataSource(String.valueOf(mediaPath));
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
            ViseLog.showLog(TAG, "开始");
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            ViseLog.showLog(TAG, "暂停");
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
            playerPrePareState = PlayerPrePareStateType.NONE;
            mPlayerState = PlayerStateType.IDLE;
            ViseLog.showLog(TAG, "停止");
        }
    }

    @Override
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            if (surface != null) {
                if (surface.isValid()) {
                    mediaPlayer.setSurface(surface);
                    ViseLog.showLog(TAG, "surface设置成功");
                }
            }
            if (surfaceHolder != null) {
                mediaPlayer.setDisplay(surfaceHolder);
                ViseLog.showLog(TAG, "surfaceHolder设置成功");
            }
            ViseLog.showLog(TAG, "重置");
        }
    }

    @Override
    public void release() {
        surface = null;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            context = null;
            ViseLog.showLog(TAG, "释放");
        }
    }

    @Override
    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
            ViseLog.showLog(TAG, "设置音量:" + volume);
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

    @Override
    public void setScalingMode(int scalingMode) {
        if (videoScalingMode != scalingMode) {
            videoScalingMode = scalingMode;
        }
    }

    @Override
    public void resetPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new IjkMediaPlayer();
        initListener();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private void initSetting() {
        //1为开启，0为关闭
        //rtmp缩短延迟
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
        //播放前的探测Size，默认是1M, 改小一点会出画面更快
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024L * 16 / 2);
        //每处理一个packet之后刷新io上下文
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
        //是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验,1开启，0不开启
        //这个必须关闭，否则会出现播放一段时间后，一直卡主，控制台打印 FFP_MSG_BUFFERING_START
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L);
        //跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步。如果 CPU 解码能力不足，可以设置成 5，否则
        //会引起音视频不同步，也可以通过设置它来跳帧达到倍速播放
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L);
        // 解码方式，0:软解，1:硬解
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-hevc", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec_mpeg4", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        // 可选，有时候tcp模式出画面更快，因为rtsp是先udp，不成功再切到tcp的
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_flags", "prefer_tcp");
        //解决m3u8文件拖动问题 比如:一个3个多少小时的音频文件，开始播放几秒中，然后拖动到2小时左右的时间，要loading 10分钟
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "fastseek");//设置seekTo能够快速seek到指定位置并播放
        //SeekTo设置优化。某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
        //最小fps
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 25);
        //最大fps
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 30);
        //最大缓冲大小,单位kb
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024);
        //播放重连次数
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 1);
        //设置播放前的探测时间 1,达到首屏秒开效果
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1);
        //设置播放前的最大探测时间
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
        //快进和慢放接口，已经支持全版本，如果遇到声调问题
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1);
        //设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48L关闭，画面质量差点，解码开销小
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 0);
        //无缝切换码流
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        // 如果没有设置timeout，在解析时（也就是avformat_open_input）把网线拔掉，av_read_frame会阻塞（时间单位是微妙）
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", "5000000");
        //设置媒体类型
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "allowed_media_types", "video");
        // 清空DNS,有时因为在APP里面要播放多种类型的视频(如:MP4,直播,直播平台保存的视频,和其他http视频), 有时会造成因为DNS的问题而报10000问题的
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);
        // 无限制收流
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);
        // 最大缓冲cache是3s， 有时候网络波动，会突然在短时间内收到好几秒的数据。因此需要播放器丢包，才不会累积延时
        // 这个和第三个参数packet-buffering无关。// 最大缓存大小 5 秒，可以依据自己的需求修改
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 5000);
        // 设置音量大小，256 为标准音量。（要设置成两倍音量时则输入 512，依此类推）
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "vol", 512);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", "fcc-i420");
    }
}