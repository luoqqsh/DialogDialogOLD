package com.xing.hhplayer.common.View;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.TypeBean.PlayerViewType;
import com.xing.hhplayer.common.base.ControlViewInterface;
import com.xing.hhplayer.common.base.HHPlayerCore;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import x.com.hl_base.hl_interface.OnDoubleClickListener;
import x.com.hl_livedatabus.LiveEventBus;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_time;
import x.com.hl_view.RotateImageView;

public class HHControlView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    private ViewGroup mTopContainer, mBottomContainer, loading_layout, layout_list, seekbarlayout;
    private FragmentActivity mActivity;
    private View contentPanel;
    private HHPlayer3 hhPlayer3;
    private HHListView hhListView;
    private View mStartButton;
    private RotateImageView loading;
    private ImageView mFullscreenButton;
    private ImageView mLockScreen;
    private TextView mCurrentTimeTextView, mTotalTimeTextView;
    private TextView mTitleTextView;
    private SeekBar mProgressBar;
    private TextView playlist;
    private TextView scaleView;
    private boolean isPanelShow = true;//是否显示面板
    private boolean isShowList = false;//是否显示列表
    private boolean isShowSeekBar = false;//是否显示进度条
    private Timer mDismissControlViewTimer;

    public HHControlView(@NonNull FragmentActivity context, HHPlayer3 hhPlayer3) {
        super(context);
        this.hhPlayer3 = hhPlayer3;
        this.mActivity = context;
        hhPlayer3.setCvInterface(this);
        initView(context);
    }

    public HHControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HHControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        View.inflate(context, getLayoutId(), this);
        //top
        mTopContainer = findViewById(R.id.liveTopLayout);
        mTitleTextView = findViewById(R.id.liveTitle);
        ImageView mBackButton = findViewById(R.id.liveBack);
        mBackButton.setOnClickListener(this);
        //bottom
        seekbarlayout = findViewById(R.id.liveSeekLayout);
        mBottomContainer = findViewById(R.id.liveBottomLayout);
        mFullscreenButton = findViewById(R.id.liveFullScreen);
        mCurrentTimeTextView = findViewById(R.id.livePlayTime);
        contentPanel = findViewById(R.id.contentPanel);
        mTotalTimeTextView = findViewById(R.id.liveTotalTime);
        mProgressBar = findViewById(R.id.liveSeek);
        mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    hhPlayer3.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.livePlay).setOnClickListener(this);
        scaleView = findViewById(R.id.liveScale);
        scaleView.setOnClickListener(this);
        //list
        layout_list = findViewById(R.id.layout_list);
        //mid
        loading_layout = findViewById(R.id.loadingLayout);
        mLockScreen = findViewById(R.id.liveLock);
        loading = findViewById(R.id.loadingView);
        mStartButton = findViewById(R.id.loadingMidPause);
        playlist = findViewById(R.id.livePlayList);
        playlist.setOnClickListener(this);
        mStartButton.setOnClickListener(this);
        contentPanel.setOnClickListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                playControl();
            }

            @Override
            public void onOneClick() {
                if (isShowList) {
                    isShowList = false;
                    layout_list.setVisibility(GONE);
                    return;
                }
                isPanelShow = !isPanelShow;
                showPlayControl(isPanelShow);
            }
        }));
        LiveEventBus.get("HHPlayerPlayState", HHPlayerCore.class).observe(mActivity, new Observer<HHPlayerCore>() {
            @Override
            public void onChanged(HHPlayerCore hhPlayerCore) {
                ViseLog.d(hhPlayerCore);
            }
        });
        LiveEventBus.get("aaa", String.class).observe(mActivity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ViseLog.d(s);
            }
        });
    }

    private int getLayoutId() {
        return R.layout.hhplayer_liveview;
    }

    private int mType = 0;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        //播放暂停
        if (id == R.id.livePlay || id == R.id.loadingMidPause) {
            playControl();
        }
        //显示比例
        if (id == R.id.liveScale) {
            if (mType == 0) {
                mType = 1;
                scaleView.setText("16:9");
                hhPlayer3.setVideoScaleType(PlayerViewType.SCREEN_TYPE_16_9);
            } else if (mType == 1) {
                mType = 2;
                scaleView.setText("4:3");
                hhPlayer3.setVideoScaleType(PlayerViewType.SCREEN_TYPE_4_3);
            } else if (mType == 2) {
                mType = 3;
                scaleView.setText("全屏");
                hhPlayer3.setVideoScaleType(PlayerViewType.SCREEN_TYPE_FULL);
            } else if (mType == 3) {
                mType = 4;
                scaleView.setText("拉伸全屏");
                hhPlayer3.setVideoScaleType(PlayerViewType.SCREEN_MATCH_FULL);
            } else if (mType == 4) {
                mType = 0;
                scaleView.setText("默认比例");
                hhPlayer3.setVideoScaleType(PlayerViewType.SCREEN_TYPE_DEFAULT);
            }
        }
        //返回
        if (id == R.id.liveBack) {
            mActivity.finish();
        }
        //播放列表
        if (id == R.id.livePlayList) {
            isShowList = !isShowList;
            if (isShowList) {
                cancelDismissTimer();
                layout_list.setVisibility(VISIBLE);
            } else {
                startDismissTimer();
                layout_list.setVisibility(GONE);
            }
        }
    }

    private void showMidStart(boolean isShow) {
        if (isShow) {
            mStartButton.setVisibility(VISIBLE);
        } else {
            mStartButton.setVisibility(GONE);
        }
    }

    private void showPlayControl(boolean isPanelShow) {
        if (isPanelShow && hhPlayer3.isPlaying()) {
            startDismissTimer();
        } else {
            cancelDismissTimer();
        }
        mBottomContainer.setVisibility(isPanelShow ? VISIBLE : INVISIBLE);
        mTopContainer.setVisibility(isPanelShow ? VISIBLE : GONE);
//        mLockScreen.setVisibility(isPanelShow ? VISIBLE : GONE);
    }

    private void playControl() {
        if (hhPlayer3.isPlaying()) {
            hhPlayer3.pause();
            cancelDismissTimer();
            showMidStart(true);
        } else {
            hhPlayer3.start();
            startDismissTimer();
            showMidStart(false);
        }
    }

    @Override
    public void getPrePareState(PlayerPrePareStateType playerPrePareStateType) {
        switch (playerPrePareStateType) {
            case PREPARING:
                loading_layout.setVisibility(VISIBLE);
                loading.start();
                break;
            case PREPARED:
                if (TextUtils.isEmpty(hhPlayer3.getTitle())) {
                    mTitleTextView.setText(hhPlayer3.getMediaPath().toString());
                } else {
                    mTitleTextView.setText(hhPlayer3.getTitle());
                }
                startDismissTimer();
                //隐藏加载转圈
                loading_layout.setVisibility(GONE);
                loading.stop();
                if (hhPlayer3.getMediaDurationMS() != 0) {
                    isShowSeekBar = true;
                    seekbarlayout.setVisibility(VISIBLE);
                    mProgressBar.setMax((int) (hhPlayer3.getMediaDurationMS() / 1000));
                    mTotalTimeTextView.setText(hl_time.formatTime(hhPlayer3.getMediaDurationMS()));
                } else {
                    isShowSeekBar = false;
                    seekbarlayout.setVisibility(INVISIBLE);
                }
                if (hhPlayer3.getUrlDataList() != null && hhListView == null) {
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.RIGHT;
                    hhListView = new HHListView(mActivity, hhPlayer3.getUrlDataList(), hhPlayer3);
                    layout_list.addView(hhListView, params);
                    playlist.setVisibility(VISIBLE);
                    layout_list.setVisibility(GONE);
                }
                break;
            case NONE:
            case ERROR:
                cancelDismissTimer();
                loading_layout.setVisibility(GONE);
                loading.stop();
                break;
        }
    }

    @Override
    public void getPlayState(PlayerStateType playerStateType) {

    }

    @Override
    public void changeProgress(final long time) {
        mCurrentTimeTextView.post(new Runnable() {
            @Override
            public void run() {
                mCurrentTimeTextView.setText(hl_time.formatTime(time));
                if (isShowSeekBar) {
                    mProgressBar.setProgress((int) (time / 1000));
                }
            }
        });
    }

    @Override
    public void receiveKeyDown(int keyCode) {

    }

    @Override
    public void refreshPlayList(List<TvDataList> tvDataLists) {
        hhListView.refreshList(tvDataLists);
    }

    @Override
    public void getPlayBuffer(int progress) {
        if (progress > 0 && progress < 100) {
            loading.setVisibility(VISIBLE);
            loading_layout.setVisibility(VISIBLE);
            loading.start();
        } else {
            loading.setVisibility(GONE);
            loading_layout.setVisibility(GONE);
            loading.stop();
        }
    }

    private void startDismissTimer() {
        cancelDismissTimer();
        mDismissControlViewTimer = new Timer();
        mDismissControlViewTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                contentPanel.post(new Runnable() {
                    @Override
                    public void run() {
                        isPanelShow = false;
                        showPlayControl(false);
                        cancelDismissTimer();
                    }
                });
            }
        }, 3000);
    }

    private void cancelDismissTimer() {
        if (mDismissControlViewTimer != null) {
            mDismissControlViewTimer.cancel();
            mDismissControlViewTimer = null;
        }
    }
}
