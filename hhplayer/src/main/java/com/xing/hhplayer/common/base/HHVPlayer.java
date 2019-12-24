//package com.xing.hhplayer.common.base;
//
//import android.os.Build;
//import android.util.Log;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//
//import com.lansosdk.videoplayer.OnLSOPlayerBufferingUpdateListener;
//import com.lansosdk.videoplayer.OnLSOPlayerCompletionListener;
//import com.lansosdk.videoplayer.OnLSOPlayerErrorListener;
//import com.lansosdk.videoplayer.OnLSOPlayerInfoListener;
//import com.lansosdk.videoplayer.OnLSOPlayerPreparedListener;
//import com.lansosdk.videoplayer.OnLSOPlayerSeekCompleteListener;
//import com.lansosdk.videoplayer.VideoPlayer;
//import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
//import com.xing.hhplayer.common.TypeBean.PlayerStateType;
//
//import x.com.hl_log.ViseLog;
//
///**
// * MediaPlayer具体实现
// */
//public class HHVPlayer extends HHPlayerCore {
//    private VideoPlayer player;
//
//    public HHVPlayer() {
//        player = new VideoPlayer();
//        player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//        player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "overlay-format", VideoPlayer.SDL_FCC_RV32);
//        player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//        player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "startPreview-on-prepared", 0);
//        player.setOption(VideoPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//        player.setOption(VideoPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        initListener();
//    }
//
//    @Override
//    public void initView(SurfaceHolder surfaceHolder) {
//        if (player != null) {
//            player.setDisplay(surfaceHolder);
//            ViseLog.showLog(TAG, "surfaceHolder" + surfaceHolder.toString());
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
//    public void initView(Surface surface) {
//        if (player != null && surface.isValid()) {
//            player.setSurface(surface);
//            ViseLog.showLog(TAG, "surface" + surface.toString());
//        }
//    }
//
//    private void initListener() {
//        if (player != null) {
//            player.setOnPreparedListener(new OnLSOPlayerPreparedListener() {
//                @Override
//                public void onPrepared(VideoPlayer videoPlayer) {
//                    playerPrePareState = PlayerPrePareStateType.PREPARED;
//                    mPlayerState = PlayerStateType.PREPARED;
//                    videoWidth = player.getVideoWidth();
//                    videoHeight = player.getVideoHeight();
//                    setVolume(volume);
//                    player.setLooping(isLooping);
//                    if (onPreparedListener != null) {
//                        onPreparedListener.onListen(videoPlayer);
//                    }
//                    StringBuilder videoInfoLog = new StringBuilder("Prepared OK\n");
//                    videoInfoLog.append("path: ").append(mediaPath).append("\n");
//                    videoInfoLog.append("width: ").append(player.getVideoWidth()).append("\n");
//                    videoInfoLog.append("height: ").append(player.getVideoHeight()).append("\n");
//                    videoInfoLog.append("duration: ").append(player.getDuration()).append("\n");
//                    videoInfoLog.append("TAG: ").append(TAG);
//                    ViseLog.d(videoInfoLog);
//                }
//            });
//            player.setOnSeekCompleteListener(new OnLSOPlayerSeekCompleteListener() {
//                @Override
//                public void onSeekComplete(VideoPlayer videoPlayer) {
//                    mPlayerState = PlayerStateType.PLAYING;
//                    start();
//                    ViseLog.showLog(TAG, "Seek Complete");
//                }
//            });
//            player.setOnCompletionListener(new OnLSOPlayerCompletionListener() {
//                @Override
//                public void onCompletion(VideoPlayer videoPlayer) {
//                    if (onCompletionListener != null) {
//                        onCompletionListener.onListen(videoPlayer);
//                    }
//                }
//            });
//            player.setOnErrorListener(new OnLSOPlayerErrorListener() {
//                @Override
//                public boolean onError(VideoPlayer videoPlayer, int what, int extra) {
//                    Log.e(ETAG, "监听到错误。  " + mPlayerState);
//                    mPlayerState = PlayerStateType.ERROR;
//                    playerPrePareState = PlayerPrePareStateType.ERROR;
//                    Log.e(ETAG, what + "  " + extra + "\n" + "播放出错：" + mediaPath);
//                    if (onErrorListener != null) {
//                        String err = "mediaplayer播放出错：" + what + " " + extra + " " + mPlayerState;
//                        onErrorListener.onListen(err);
//                    }
//                    return false;
//                }
//            });
//            player.setOnBufferingUpdateListener(new OnLSOPlayerBufferingUpdateListener() {
//                @Override
//                public void onBufferingUpdate(VideoPlayer videoPlayer, int percent) {
//                    if (onBufferingUpdateListener != null) {
//                        onBufferingUpdateListener.onListen(percent);
//                    }
//                }
//            });
//            player.setOnInfoListener(new OnLSOPlayerInfoListener() {
//                @Override
//                public boolean onInfo(VideoPlayer videoPlayer, int i, int i1) {
//                    return false;
//                }
//            });
//        }
//    }
//
//    private void doPrepare(Object mediaPath, boolean isFirst) {
//        if (mediaPath == null) {
//            Log.e(ETAG, "地址为空或找不到！");
//            return;
//        }
//        this.mediaPath = mediaPath;
//        stop();
//        reset();
//        try {
//            playerPrePareState = PlayerPrePareStateType.PREPARING;
//            player.setDataSource(mediaPath.toString());
//            player.prepareAsync();//异步
//        } catch (Exception e) {
//            e.printStackTrace();
//            mPlayerState = PlayerStateType.ERROR;
//            playerPrePareState = PlayerPrePareStateType.ERROR;
//            Log.e(ETAG, "Prepare失败。  " + this.mediaPath);
//            if (isFirst) dealError();
//        }
//    }
//
//    @Override
//    public long getCurrentPosition() {
//        if (player != null) {
//            if (player.getCurrentPosition() > getMediaDuration()) {
//                return getMediaDuration();
//            }
//            return player.getCurrentPosition();
//        }
//        return 0;
//    }
//
//    @Override
//    public long getMediaDuration() {
//        if (player != null) {
//            return player.getDuration();
//        }
//        return 0;
//    }
//
//    @Override
//    public void prepare(Object mediaPath) {
//        if ("HHPlayerCore".equals(TAG)) {
//            TAG = "HH-MediaPlayer-Music";
//        }
//        this.mediaPath = mediaPath;
//        doPrepare(mediaPath, true);
//    }
//
//    @Override
//    public void start() {
//        if (player != null) {
//            player.start();
//            ViseLog.showLog(TAG, "start");
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (player != null) {
//            player.pause();
//            ViseLog.showLog(TAG, "pause");
//        }
//    }
//
//    @Override
//    public void seekTo(long position) {
//        if (player != null) {
//            player.seekTo(position);
//            ViseLog.showLog(TAG, "seekTo " + position);
//        }
//    }
//
//    @Override
//    public void stop() {
//        //调用stop方法之后要重新调用prepare方法，不然无法播放
//        if (player != null && isPlayControl()) {
//            player.stop();
//            playerPrePareState = PlayerPrePareStateType.NONE;
//            mPlayerState = PlayerStateType.IDLE;
//            ViseLog.showLog(TAG, "stop");
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (player != null) {
//            player.reset();
//            ViseLog.showLog(TAG, "reset");
//        }
//    }
//
//    @Override
//    public void release() {
//        if (player != null) {
//            player.release();
////            mediaPlayer = null;
//            ViseLog.showLog(TAG, "release");
//        }
//    }
//
//    @Override
//    public void setVolume(float volume) {
//        if (player != null) {
//            player.setVolume(volume, volume);
//            ViseLog.showLog(TAG, "setVolume " + volume);
//        }
//    }
//
//    @Override
//    public void setSpeed(float speed) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (player != null) {
//                player.setSpeed(speed);
//                ViseLog.showLog(TAG, "setSpeed " + speed);
//            }
//        }
//    }
//
//    private void dealError() {
//        doPrepare(mediaPath, false);
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return player.isPlaying();
//    }
//}