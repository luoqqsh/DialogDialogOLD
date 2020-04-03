//package com.xing.hhplayer.common.base;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.res.AssetFileDescriptor;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Looper;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//
//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.DefaultRenderersFactory;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
//import com.xing.hhplayer.common.TypeBean.PlayerStateType;
//
//import java.lang.reflect.Method;
//import java.util.Map;
//
//import tv.danmaku.ijk.media.player.IMediaPlayer;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//import x.com.hl_log.ViseLog;
//import x.com.hl_tools.hl_uri;
//
//public class HHexoPlayer extends HHPlayerCore {
//    private SimpleExoPlayer mediaPlayer;
//    private Surface surface;
//    private SurfaceHolder surfaceHolder;
//    private boolean preferExtensionDecoders = true;
//    private boolean useExtensionRenderers = true;//是否开启扩展
//
//    public HHexoPlayer(Context context) {
//        initPlayer();
//        initListener();
//    }
//
//    @Override
//    public void initView(Surface surface) {
//        if (surface.isValid() && mediaPlayer != null) {
//            this.surface = surface;
//            mediaPlayer.setVideoSurface(surface);
//            ViseLog.showInfo(TAG, "surface设置成功");
//        }
//    }
//
//    @Override
//    public void initView(SurfaceHolder surfaceHolder) {
//        if (mediaPlayer != null) {
//            this.surface = surfaceHolder.getSurface();
//            mediaPlayer.setVideoSurface(surface);
//            ViseLog.showInfo(TAG, "surfaceHolder设置成功");
//        }
//    }
//
//    private void initPlayer() {
//        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode = useExtensionRenderers
//                ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
//                : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
//                : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
//        DefaultRenderersFactory mRendererFactory = new DefaultRenderersFactory(context);
//        mRendererFactory.setExtensionRendererMode(extensionRendererMode);
//        mediaPlayer = new SimpleExoPlayer.Builder(context, mRendererFactory)
//                .setLooper(Looper.getMainLooper())
//                .setTrackSelector(new DefaultTrackSelector(context))
//                .setLoadControl(new DefaultLoadControl()).build();
//    }
//
//    private void initListener() {
//        if (mediaPlayer != null) {
//            mediaPlayer.setOnPrepaseredListener(new IjkMediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(IMediaPlayer mp) {
//                    playerPrePareState = PlayerPrePareStateType.PREPARED;
//                    mPlayerState = PlayerStateType.PREPARED;
//                    videoWidth = mediaPlayer.getVideoWidth();
//                    videoHeight = mediaPlayer.getVideoHeight();
//                    mediaPlayer.setVolume(volume);
//                    mediaPlayer.setPlayWhenReady(isLooping);
//                    mediaPlayer.setLooping(isLooping);
//                    onCorePreparedListener.onListen(mp);
//                    StringBuilder videoInfoLog = new StringBuilder("Prepared OK\n");
//                    videoInfoLog.append("path: ").append(mediaPath).append("\n");
//                    videoInfoLog.append("width: ").append(mediaPlayer.getVideoWidth()).append("\n");
//                    videoInfoLog.append("height: ").append(mediaPlayer.getVideoHeight()).append("\n");
//                    videoInfoLog.append("duration: ").append(mediaPlayer.getDuration()).append("\n");
//                    videoInfoLog.append("tag: ").append(TAG);
//                    ViseLog.d(TAG, videoInfoLog);
//                }
//            });
//            mediaPlayer.setOnSeekCompleteListener(new IjkMediaPlayer.OnSeekCompleteListener() {
//                @Override
//                public void onSeekComplete(IMediaPlayer mp) {
//                    start();
//                    ViseLog.showInfo(TAG, "Seek Complete");
//                }
//            });
//            mediaPlayer.setOnCompletionListener(new IjkMediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(IMediaPlayer mp) {
//                    onCoreCompletionListener.onListen(mp);
//                }
//            });
//            mediaPlayer.setOnErrorListener(new IjkMediaPlayer.OnErrorListener() {
//                @Override
//                public boolean onError(IMediaPlayer mp, int what, int extra) {
//                    mPlayerState = PlayerStateType.ERROR;
//                    playerPrePareState = PlayerPrePareStateType.ERROR;
//                    String err = "ijk播放出错：" + what + " " + extra + " " + mPlayerState + "\n" + mediaPath;
//                    onCoreErrorListener.onListen(err);
//                    return false;
//                }
//            });
//            mediaPlayer.setOnBufferingUpdateListener(new IjkMediaPlayer.OnBufferingUpdateListener() {
//                @Override
//                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
//                    onCoreBufferingUpdateListener.onListen(percent);
//                }
//            });
//        }
//    }
//
//    @Override
//    public long getCurrentPositionMS() {
//        if (mediaPlayer != null) {
//            if (mediaPlayer.getCurrentPosition() > getMediaDurationMS()) {
//                return getMediaDurationMS();
//            }
//            return mediaPlayer.getCurrentPosition();
//        }
//        return 0;
//    }
//
//    @Override
//    public long getMediaDurationMS() {
//        if (mediaPlayer != null) {
//            return mediaPlayer.getDuration();
//        }
//        return 0;
//    }
//
//    @Override
//    public void prepare(String path) {
//        if (path == null) {
//            onCoreErrorListener.onListen("地址为空！");
//            return;
//        }
//        this.mediaPath = path;
//        stop();
//        reset();//ijkplayer调用reset会连画布一起重置，画布不会销毁
//        try {
//            playerPrePareState = PlayerPrePareStateType.PREPARING;
//            Uri uri = Uri.parse((String) mediaPath);
//            if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
//                AssetFileDescriptor fileDescriptor = context.getApplicationContext().getContentResolver().openAssetFileDescriptor(hl_uri.str2Uri(String.valueOf(this.mediaPath)), "r");
//                mediaPlayer.setDataSource(new RawDataSourceProvider(fileDescriptor));
//            } else if (getHeaders() != null) {//url with headers
//                Class<MediaPlayer> clazz = MediaPlayer.class;
//                Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
//                method.invoke(mediaPlayer, mediaPath, getHeaders());
//            } else {
//                mediaPlayer.setDataSource(String.valueOf(mediaPath));
//            }
//            mediaPlayer.prepareAsync();//异步
//        } catch (Exception e) {
//            e.printStackTrace();
//            mPlayerState = PlayerStateType.ERROR;
//            playerPrePareState = PlayerPrePareStateType.ERROR;
//            onCoreErrorListener.onListen("Prepare异常");
//        }
//    }
//
//    @Override
//    public void prepare(Uri mediaUri) {
//        if (mediaUri == null) {
//            onCoreErrorListener.onListen("地址为空！");
//            return;
//        }
//        this.mediaUri = mediaUri;
//        stop();
//        reset();//ijkplayer调用reset会连画布一起重置，画布不会销毁
//        try {
//            playerPrePareState = PlayerPrePareStateType.PREPARING;
//            mDataSource = mediaUri.toString();
//            mMediaSource = mExoHelper.getMediaSource(mDataSource, isPreview, isCache, isLooping, mCacheDir, mOverrideExtension);
//            mediaPlayer.setDataSource(context, mediaUri);
//            mediaPlayer.prepareAsync();//异步
//        } catch (Exception e) {
//            e.printStackTrace();
//            mPlayerState = PlayerStateType.ERROR;
//            playerPrePareState = PlayerPrePareStateType.ERROR;
//            onCoreErrorListener.onListen("Prepare异常");
//        }
//    }
//
//    @Override
//    public void start() {
//        if (mediaPlayer != null) {
//            mediaPlayer.st();
//            ViseLog.showInfo(TAG, "开始");
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (mediaPlayer != null) {
//            mediaPlayer.pause();
//            ViseLog.showInfo(TAG, "暂停");
//        }
//    }
//
//    @Override
//    public void seekTo(long position) {
//        if (mediaPlayer != null) {
//            mediaPlayer.seekTo((int) position);
//        }
//    }
//
//    @Override
//    public void stop() {
//        //调用stop方法之后要调用prepare方法，不然无法播放
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            playerPrePareState = PlayerPrePareStateType.NONE;
//            mPlayerState = PlayerStateType.IDLE;
//            ViseLog.showInfo(TAG, "停止");
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (mediaPlayer != null) {
//            mediaPlayer.reset();
//            if (surface != null) {
//                if (surface.isValid()) {
//                    mediaPlayer.setVideoSurface(surface);
//                    ViseLog.showInfo(TAG, "surface设置成功");
//                }
//            }
//            if (surfaceHolder != null) {
//                mediaPlayer.setVideoSurfaceHolder(surfaceHolder);
//                ViseLog.showInfo(TAG, "surfaceHolder设置成功");
//            }
//            ViseLog.showInfo(TAG, "重置");
//        }
//    }
//
//    @Override
//    public void release() {
//        surface = null;
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//            context = null;
//            ViseLog.showInfo(TAG, "释放");
//        }
//    }
//
//    @Override
//    public void setVolume(float volume) {
//        if (mediaPlayer != null) {
//            mediaPlayer.setVolume(volume);
//            ViseLog.showInfo(TAG, "设置音量:" + volume);
//        }
//    }
//
//    @Override
//    public void setSpeed(float speed) {
//        if (mediaPlayer != null) {
//            mediaPlayer.setSpeed(speed);
//        }
//    }
//
//    @Override
//    public void setScalingMode(int scalingMode) {
//        if (videoScalingMode != scalingMode) {
//            videoScalingMode = scalingMode;
//        }
//    }
//
//    @Override
//    public void resetPlayer() {
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//        initPlayer();
//        initListener();
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return mediaPlayer.isPlaying();
//    }
//
//}