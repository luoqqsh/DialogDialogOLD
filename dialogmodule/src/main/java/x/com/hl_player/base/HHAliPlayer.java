//package x.com.hl_player.base;
//
//import android.os.Environment;
//import android.util.Log;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//import x.com.hl_log.ViseLog;
//
//public class HHAliPlayer extends HHPlayerCore {
//    private AliyunVodPlayer mAliyunVodPlayer;
//
//    @Override
//    public void initView(SurfaceView surface) {
//        mAliyunVodPlayer = new AliyunVodPlayer(surface.getContext());
//        initListener();
//        surface.setKeepScreenOn(true);
//        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                mAliyunVodPlayer.setDisplay(holder);
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                mAliyunVodPlayer.surfaceChanged();
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                release();
//            }
//        });
//    }
//
//    @Override
//    public void initView(Surface surface) {
//        TAG = "HH-MediaPlayer-surface";
//        mAliyunVodPlayer = new AliyunVodPlayer(context);
//        initListener();
//        mAliyunVodPlayer.setSurface(surface);
//    }
//
//    private void initListener() {
//        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
//        mAliyunVodPlayer.setPlayingCache(true, sdDir, 60 * 60 /*单个文件的最大时长, s */, 300 /*缓存目录文件的总大小，MB*/);
////        mAliyunVodPlayer.enableNativeLog();
//        //设置准备回调
//        mAliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared() {
//                playerPrePareState = PlayerPrePareState.PREPARED;
//                mPlayerState = PlayerState.PREPARED;
//                isPrepared = true;
//                mAliyunVodPlayer.setVolume((int) volume);
//                mAliyunVodPlayer.setCirclePlay(isLooping);
//                if (isPlayWhenPrepared) {
//                    start();
//                }
//                if (onPreparedListener != null) {
//                    onPreparedListener.onListen(mAliyunVodPlayer);
//                }
//                Log.e(TAG, "Prepared OK");
//            }
//        });
//        //播放器出错监听
//        mAliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
//            @Override
//            public void onError(int errorCode, int errorEvent, String errorMsg) {
//                Log.e(TAG, "监听到错误。  " + mPlayerState);
//                mPlayerState = PlayerState.ERROR;
//                playerPrePareState = PlayerPrePareState.ERROR;
//                ViseLog.e("ERROR: " + errorCode + "  " + errorMsg);
//            }
//        });
//        mAliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion() {
//                isCompletePlay = true;
//                if (onCompletionListener != null) {
//                    onCompletionListener.onListen(mAliyunVodPlayer);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void prepare(Object mediaPath) {
//        if ("HHPlayerCore".equals(TAG)) {
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
//            }
//            isCompletePlay = false;
//        } else {
//            if (mAliyunVodPlayer != null) {
//                mAliyunVodPlayer.start();
//                getCurrentStart();
//            }
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.pause();
//        }
//    }
//
//    @Override
//    public void stop() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.stop();
//            isCompletePlay = true;
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.reset();
//        }
//    }
//
//    @Override
//    public void seekTo(long position) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.seekTo((int) position);
//        }
//    }
//
//    @Override
//    public void release() {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.release();
//            mAliyunVodPlayer = null;
//        }
//    }
//
//    @Override
//    public void setVolume(float volume) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setVolume((int) volume);
//        }
//    }
//
//    @Override
//    public void setSpeed(float speed) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setPlaySpeed(speed);
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
//
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
