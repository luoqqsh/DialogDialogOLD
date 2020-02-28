//package com.xing.hhplayer.common.base;
//
//
//import android.content.Context;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//
//import com.aliyun.vodplayer.media.AliyunLocalSource;
//import com.aliyun.vodplayer.media.AliyunVodPlayer;
//import com.aliyun.vodplayer.media.IAliyunVodPlayer;
//import com.cll.video.tools.config.FileConfig;
//import com.cll.video.tools.hhplayer.TypeBean.PlayerPrePareStateType;
//import com.cll.video.tools.hhplayer.TypeBean.PlayerStateType;
//import com.cll.video.tools.viselog.ViseLog;
//
//import java.io.File;
//
//public class HHAliPlayer extends HHPlayerCore {
//    private AliyunVodPlayer mAliyunVodPlayer;
//    private boolean isCompletePlay = false;
//    private SurfaceHolder surfaceHolder;
//    private Surface surface;
//
//
//    public HHAliPlayer(Context context) {
//        this.context = context;
//        mAliyunVodPlayer = new AliyunVodPlayer(context);
//        //缓存路径
//        String sdDir = FileConfig.getQdir(context).getPath() + File.separator + "ali_video_cache";
//        mAliyunVodPlayer.setPlayingCache(true, sdDir, 60 * 60 /*单个文件的最大时长, s */, 300 /*缓存目录文件的总大小，MB*/);
////        mAliyunVodPlayer.enableNativeLog();
//        initListener();
//    }
//
//    @Override
//    public void initView(Surface surface) {
//        if (surface != null) {
//            this.surface = surface;
//            if (mAliyunVodPlayer != null && surface.isValid()) {
//                mAliyunVodPlayer.setSurface(surface);
//                ViseLog.showLog(TAG, "surface设置成功");
//            }
//        } else {
//            onCoreErrorListener.onListen("surface为空！");
//        }
//    }
//
//    @Override
//    public void initView(SurfaceHolder surfaceHolder) {
//        if (surfaceHolder != null) {
//            this.surfaceHolder = surfaceHolder;
//            if (mAliyunVodPlayer != null) {
//                mAliyunVodPlayer.setDisplay(surfaceHolder);
//                ViseLog.showLog(TAG, "surfaceHolder设置成功");
//            }
//        } else {
//            onCoreErrorListener.onListen("surfaceHolder为空！");
//        }
//    }
//
//    @Override
//    public void setScalingMode(int scalingMode) {
//        if (videoScalingMode != scalingMode) {
//            videoScalingMode = scalingMode;
//        }
//        IAliyunVodPlayer.VideoScalingMode mode = IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT;
//        switch (scalingMode) {
//            case 0:
//                mode = IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT;
//                break;
//            case 1:
//                mode = IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;
//                break;
//        }
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setVideoScalingMode(mode);
//        }
//    }
//
//    private void initListener() {
//        //设置准备回调
//        mAliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared() {
//                playerPrePareState = PlayerPrePareStateType.PREPARED;
//                mPlayerState = PlayerStateType.PREPARED;
//                setVolume(volume);
//                mAliyunVodPlayer.setCirclePlay(isLooping);
//                videoWidth = mAliyunVodPlayer.getVideoWidth();
//                videoHeight = mAliyunVodPlayer.getVideoHeight();
//                onCorePreparedListener.onListen(mAliyunVodPlayer);
//                StringBuilder videoInfoLog = new StringBuilder("Prepared OK\n");
//                videoInfoLog.append("path: ").append(mediaPath).append("\n");
//                videoInfoLog.append("width: ").append(mAliyunVodPlayer.getVideoWidth()).append("\n");
//                videoInfoLog.append("height: ").append(mAliyunVodPlayer.getVideoHeight()).append("\n");
//                videoInfoLog.append("duration: ").append(mAliyunVodPlayer.getDuration()).append("\n");
//                videoInfoLog.append("tag: ").append(TAG);
//                ViseLog.d(videoInfoLog);
//            }
//        });
//        //播放器出错监听
//        mAliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
//            @Override
//            public void onError(int errorCode, int errorEvent, String errorMsg) {
//                mPlayerState = PlayerStateType.ERROR;
//                playerPrePareState = PlayerPrePareStateType.ERROR;
//                onCoreErrorListener.onListen("errorCode:" + errorCode + "  errorEvent:" + errorEvent + "  errorMsg:" + errorMsg);
//            }
//        });
//        mAliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion() {
//                isCompletePlay = true;
//                onCoreCompletionListener.onListen(mAliyunVodPlayer);
//            }
//        });
//        mAliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete() {
//                mPlayerState = PlayerStateType.PLAYING;
//                start();
//                ViseLog.showLog(TAG, "Seek Complete");
//            }
//        });
//        mAliyunVodPlayer.setOnInfoListener(new IAliyunVodPlayer.OnInfoListener() {
//            @Override
//            public void onInfo(int arg0, int arg1) {
//                int arg = 0;
//                switch (arg0) {
//                    case 101://开始缓冲
//                        arg = 1;
//                        break;
//                    case 102:
//                        arg = 100;
//                        break;
//                    case 105:
//                        arg = arg1;
//                        ViseLog.showLog("加载缓冲" + arg1);
//                        break;
//                    case 4://error
//                        break;
//                }
//                onCoreInfoListener.onListen(arg);
//            }
//        });
//    }
//
//    @Override
//    public void prepare(String path) {
//        if (path == null) {
//            onCoreErrorListener.onListen("地址为空或不支持！");
//            return;
//        }
//        if ("HHPlayerBase".equals(TAG)) {
//            TAG = "HH-AliPlayer-Music";
//        }
//        this.mediaPath = path;
//        stop();
//        reset();
//        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
//        alsb.setSource(String.valueOf(mediaPath));
//        AliyunLocalSource localSource = alsb.build();
//        mAliyunVodPlayer.prepareAsync(localSource);
//    }
//
//    @Override
//    public void start() {
//        if (isCompletePlay) {
//            if (mAliyunVodPlayer != null) {
//                mAliyunVodPlayer.replay();
//                ViseLog.showLog(TAG, "重播");
//            }
//            isCompletePlay = false;
//        } else {
//            if (mAliyunVodPlayer != null) {
//                mAliyunVodPlayer.start();
//                ViseLog.showLog(TAG, "开始");
//            }
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.pause();
//            ViseLog.showLog(TAG, "暂停");
//        }
//    }
//
//    @Override
//    public void stop() {
//        if (mAliyunVodPlayer != null && isPlayControl()) {
//            mAliyunVodPlayer.stop();
//            playerPrePareState = PlayerPrePareStateType.NONE;
//            mPlayerState = PlayerStateType.IDLE;
//            ViseLog.showLog(TAG, "停止");
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.reset();
//            ViseLog.showLog(TAG, "重置");
//        }
//    }
//
//    @Override
//    public void seekTo(long position) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.seekTo((int) position);
//            ViseLog.showLog(TAG, "seekTo:" + position);
//        }
//    }
//
//    @Override
//    public void release() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.release();
//            mAliyunVodPlayer = null;
//            context = null;
//            ViseLog.showLog(TAG, "释放");
//        }
//    }
//
//    @Override
//    public void setVolume(float volume) {
//        if (mAliyunVodPlayer != null) {
//            //阿里云播放器音量只支持整数
//            mAliyunVodPlayer.setVolume((int) volume);
//            ViseLog.showLog(TAG, "设置音量:" + volume);
//        }
//    }
//
//    @Override
//    public void setSpeed(float speed) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setPlaySpeed(speed);
//            ViseLog.showLog(TAG, "速度:" + speed);
//        }
//    }
//
//    @Override
//    public long getCurrentPositionMS() {
//        if (mAliyunVodPlayer != null) {
//            if (mAliyunVodPlayer.getCurrentPosition() > getMediaDurationMS()) {
//                return getMediaDurationMS();
//            }
//            return mAliyunVodPlayer.getCurrentPosition();
//        }
//        return 0;
//    }
//
//    @Override
//    public long getMediaDurationMS() {
//        if (mAliyunVodPlayer != null) {
//            return mAliyunVodPlayer.getDuration();
//        }
//        return 0;
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return mAliyunVodPlayer.isPlaying();
//    }
//
//    @Override
//    public void resetPlayer() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.release();
//            mAliyunVodPlayer = null;
//        }
//        mAliyunVodPlayer = new AliyunVodPlayer(context);
//        initListener();
//        if (surface != null) {
//            if (surface.isValid()) {
//                mAliyunVodPlayer.setSurface(surface);
//                ViseLog.showLog(TAG, "surface设置成功");
//            }
//        }
//        if (surfaceHolder != null) {
//            mAliyunVodPlayer.setDisplay(surfaceHolder);
//            ViseLog.showLog(TAG, "surfaceHolder设置成功");
//        }
//    }
//}
