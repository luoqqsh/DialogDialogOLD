//package com.xing.hhplayer.common.base;
//
//import android.view.Surface;
//import android.view.SurfaceHolder;
//
//import com.cll.video.tools.hhplayer.TypeBean.PlayerPrePareStateType;
//import com.cll.video.tools.hhplayer.TypeBean.PlayerStateType;
//import com.cll.video.tools.viselog.ViseLog;
//import com.jiujie.base.util.ViseLog;
//import com.lansosdk.videoplayer.OnLSOPlayerBufferingUpdateListener;
//import com.lansosdk.videoplayer.OnLSOPlayerCompletionListener;
//import com.lansosdk.videoplayer.OnLSOPlayerErrorListener;
//import com.lansosdk.videoplayer.OnLSOPlayerInfoListener;
//import com.lansosdk.videoplayer.OnLSOPlayerPreparedListener;
//import com.lansosdk.videoplayer.OnLSOPlayerSeekCompleteListener;
//import com.lansosdk.videoplayer.VideoPlayer;
//
///**
// * MediaPlayer具体实现
// */
//public class HHVPlayer extends HHPlayerCore {
//    private VideoPlayer player;
//    private SurfaceHolder surfaceHolder;
//    private Surface surface;
//
//    public HHVPlayer() {
//        player = new VideoPlayer();
//        initListener();
//    }
//
//    @Override
//    public void initView(SurfaceHolder surfaceHolder) {
//        if (surfaceHolder != null) {
//            this.surfaceHolder = surfaceHolder;
//            if (player != null) {
//                player.setDisplay(surfaceHolder);
//                ViseLog.showLog(TAG, "surfaceHolder设置成功");
//            }
//        } else {
//            onCoreErrorListener.onListen("surfaceHolder为空！");
//        }
//    }
//
//    @Override
//    public void initView(Surface surface) {
//        if (surface != null) {
//            this.surface = surface;
//            if (player != null && surface.isValid()) {
//                player.setSurface(surface);
//                ViseLog.showLog(TAG, "surface设置成功");
//            }
//        } else {
//            onCoreErrorListener.onListen("surface为空！");
//        }
//    }
//
//    private void initListener() {
//        if (player != null) {
//            player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//            player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "overlay-format", VideoPlayer.SDL_FCC_RV32);
//            player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//            player.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "startPreview-on-prepared", 0);
//            player.setOption(VideoPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//            player.setOption(VideoPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//            player.setOnPreparedListener(new OnLSOPlayerPreparedListener() {
//                @Override
//                public void onPrepared(VideoPlayer videoPlayer) {
//                    playerPrePareState = PlayerPrePareStateType.PREPARED;
//                    mPlayerState = PlayerStateType.PREPARED;
//                    videoWidth = player.getVideoWidth();
//                    videoHeight = player.getVideoHeight();
//                    setVolume(volume);
//                    player.setLooping(isLooping);
//                    onCorePreparedListener.onListen(videoPlayer);
//                    StringBuilder videoInfoLog = new StringBuilder("Prepared OK\n");
//                    videoInfoLog.append("path: ").append(mediaPath).append("\n");
//                    videoInfoLog.append("width: ").append(player.getVideoWidth()).append("\n");
//                    videoInfoLog.append("height: ").append(player.getVideoHeight()).append("\n");
//                    videoInfoLog.append("duration: ").append(player.getDuration()).append("\n");
//                    videoInfoLog.append("tag: ").append(TAG);
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
//                    onCoreCompletionListener.onListen(videoPlayer);
//                }
//            });
//            player.setOnErrorListener(new OnLSOPlayerErrorListener() {
//                @Override
//                public boolean onError(VideoPlayer videoPlayer, int what, int extra) {
//                    mPlayerState = PlayerStateType.ERROR;
//                    playerPrePareState = PlayerPrePareStateType.ERROR;
//                    String err = "player播放出错：" + what + " " + extra + " " + mPlayerState;
//                    onCoreErrorListener.onListen(err);
//                    return false;
//                }
//            });
//            player.setOnBufferingUpdateListener(new OnLSOPlayerBufferingUpdateListener() {
//                @Override
//                public void onBufferingUpdate(VideoPlayer videoPlayer, int percent) {
//                    if (onCoreBufferingUpdateListener != null) {
//                        onCoreBufferingUpdateListener.onListen(percent);
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
//    @Override
//    public long getCurrentPositionMS() {
//        if (player != null) {
//            if (player.getCurrentPosition() > getMediaDurationMS()) {
//                return getMediaDurationMS();
//            }
//            return player.getCurrentPosition();
//        }
//        return 0;
//    }
//
//    @Override
//    public long getMediaDurationMS() {
//        if (player != null) {
//            return player.getDuration();
//        }
//        return 0;
//    }
//
//    @Override
//    public void prepare(String path) {
//        if (path == null) {
//            onCoreErrorListener.onListen("地址为空或不支持！" + path);
//            return;
//        }
//        if ("HHPlayerCore".equals(TAG)) {
//            TAG = "HH-VPlayer-Music";
//        }
//        this.mediaPath = path;
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
//            onCoreErrorListener.onListen("Prepare异常");
//        }
//    }
//
//    @Override
//    public void start() {
//        if (player != null) {
//            player.start();
//            ViseLog.showLog(TAG, "开始");
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (player != null) {
//            player.pause();
//            ViseLog.showLog(TAG, "暂停");
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
//            ViseLog.showLog(TAG, "停止");
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (player != null) {
//            player.reset();
//            ViseLog.showLog(TAG, "重置");
//        }
//    }
//
//    @Override
//    public void release() {
//        if (player != null) {
//            player.release();
//            player = null;
//            context = null;
//            ViseLog.showLog(TAG, "释放");
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
//    public void setScalingMode(int scalingMode) {
//        if (videoScalingMode != scalingMode) {
//            videoScalingMode = scalingMode;
//        }
//    }
//
//    @Override
//    public void setSpeed(float speed) {
//        if (player != null) {
//            player.setSpeed(speed);
//            ViseLog.showLog(TAG, "速度:" + speed);
//        }
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return player.isPlaying();
//    }
//
//    public void resetPlayer() {
//        if (player != null) {
//            player.release();
//            player = null;
//        }
//        player = new VideoPlayer();
//        initListener();
//        if (surface != null) {
//            if (surface.isValid()) {
//                player.setSurface(surface);
//                ViseLog.showLog(TAG, "surface设置成功");
//            }
//        }
//        if (surfaceHolder != null) {
//            player.setDisplay(surfaceHolder);
//            ViseLog.showLog(TAG, "surfaceHolder设置成功");
//        }
//    }
//}