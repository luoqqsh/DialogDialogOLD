//package com.example.hhplayer.common.base;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//
//import com.aliyun.vodplayer.media.AliyunLocalSource;
//import com.aliyun.vodplayer.media.AliyunVodPlayer;
//import com.aliyun.vodplayer.media.IAliyunVodPlayer;
//import com.cll.video.tools.hhplayer.TypeBean.PlayerPrePareStateType;
//import com.cll.video.tools.hhplayer.TypeBean.PlayerStateType;
//
//import java.io.File;
//
//import x.com.hl_log.ViseLog;
//
//public class HHAliPlayer extends HHPlayerCore {
//    private AliyunVodPlayer mAliyunVodPlayer;
//    private boolean isCompletePlay = false;
//
//    public HHAliPlayer(Context context) {
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
//        if (mAliyunVodPlayer != null && surface.isValid()) {
//            mAliyunVodPlayer.setSurface(surface);
//            Log.d(TAG, "surface" + surface.toString());
//        }
//    }
//
//    @Override
//    public void initView(SurfaceHolder surfaceHolder) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setDisplay(surfaceHolder);
//            Log.d(TAG, "surfaceHolder" + surfaceHolder.toString());
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
//                mAliyunVodPlayer.setVolume((int) volume);
//                mAliyunVodPlayer.setCirclePlay(isLooping);
//                videoWidth = mAliyunVodPlayer.getVideoWidth();
//                videoHeight = mAliyunVodPlayer.getVideoHeight();
//                if (onPreparedListener != null) {
//                    onPreparedListener.onListen(mAliyunVodPlayer);
//                }
//                UIHelper.showLog(TAG, "Prepared OK");
//            }
//        });
//        //播放器出错监听
//        mAliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
//            @Override
//            public void onError(int errorCode, int errorEvent, String errorMsg) {
//                UIHelper.showLog(TAG, "监听到错误。  " + mPlayerState);
//                mPlayerState = PlayerStateType.ERROR;
//                playerPrePareState = PlayerPrePareStateType.ERROR;
//                ViseLog.e("ERROR: " + errorCode + "  " + errorMsg);
//                if (onErrorListener != null) {
//                    onErrorListener.onListen("errorCode:" + errorCode + "  errorEvent:" + errorEvent + "  errorMsg:" + errorMsg);
//                }
//            }
//        });
//        mAliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion() {
//                isCompletePlay = true;
//                Log.d(TAG, "completion");
//                if (onCompletionListener != null) {
//                    onCompletionListener.onListen(mAliyunVodPlayer);
//                }
//            }
//        });
//        mAliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete() {
//                mPlayerState = PlayerStateType.PLAYING;
//                start();
//                Log.d(TAG, "Seek Complete");
//            }
//        });
//    }
//
//    @Override
//    public void prepare(Object mediaPath) {
//        if ("HHPlayerBase".equals(TAG)) {
//            TAG = "HH-AliPlayer-Music";
//        }
//        this.mediaPath = mediaPath;
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
//                Log.d(TAG, "replay");
//            }
//            isCompletePlay = false;
//        } else {
//            if (mAliyunVodPlayer != null) {
//                mAliyunVodPlayer.start();
//                Log.d(TAG, "start");
//            }
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.pause();
//            Log.d(TAG, "pause");
//        }
//    }
//
//    @Override
//    public void stop() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.stop();
//            isCompletePlay = true;
//            Log.d(TAG, "stop");
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.reset();
//            Log.d(TAG, "reset");
//        }
//    }
//
//    @Override
//    public void seekTo(long position) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.seekTo((int) position);
//            Log.d(TAG, "seekTo " + position);
//        }
//    }
//
//    @Override
//    public void release() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.release();
//            mAliyunVodPlayer = null;
//            Log.d(TAG, "release");
//        }
//    }
//
//    @Override
//    public void setVolume(float volume) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setVolume((int) volume);
//            Log.d(TAG, "setVolume " + volume);
//        }
//    }
//
//    @Override
//    public void setSpeed(float speed) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setPlaySpeed(speed);
//            Log.d(TAG, "setSpeed " + speed);
//        }
//    }
//
//    @Override
//    public long getCurrentPosition() {
//        if (mAliyunVodPlayer != null) {
//            if (mAliyunVodPlayer.getCurrentPosition() > getMediaDuration()) {
//                return getMediaDuration();
//            }
//            return mAliyunVodPlayer.getCurrentPosition();
//        }
//        return 0;
//    }
//
//    @Override
//    public long getMediaDuration() {
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
//}
//
