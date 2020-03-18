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

import com.example.hhplayer.R;
import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.TypeBean.PlayerViewType;
import com.xing.hhplayer.common.base.ControlViewInterface;
import com.xing.hhplayer.common.base.PlayerDoubleClickListener;

import java.util.List;

import x.com.hl_tools.hl_time;

public class HHControlView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    private ViewGroup mTopContainer, mBottomContainer, loading_layout, layout_list, seekbarlayout;
    private FragmentActivity mActivity;
    private View contentPanel;
    private HHPlayer3 hhPlayer3;
    private HHListView hhListView;
    private View mStartButton;
    private PlayerLoadingView loading;
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
    private int showTime = 3;

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
        mFullscreenButton.setOnClickListener(this);
        contentPanel.setOnClickListener(new PlayerDoubleClickListener(new PlayerDoubleClickListener.DoubleClickCallback() {
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
                showPlayControl();
            }
        }));
    }

    private int getLayoutId() {
        return R.layout.hhplayer_liveview;
    }

    private int mType = 0;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (isPanelShow) {
            showTime = 3;
        }
        //播放暂停
        if (id == R.id.livePlay || id == R.id.loadingMidPause) {
            playControl();
        }

        if (id == R.id.liveFullScreen) {
            boolean isFull = hhPlayer3.getScreenType() == PlayerViewType.SCREEN_FULL;
            if (isFull) {
                hhPlayer3.exitFullScreen();
            } else {
                hhPlayer3.fullScreen();
            }
            scaleView.setText("默认比例");
        }
        //显示比例
        if (id == R.id.liveScale) {
            if (mType == 0) {
                mType = 1;
                scaleView.setText("16:9");
                hhPlayer3.changeScale(PlayerViewType.SCREEN_SCALE_16_9);
            } else if (mType == 1) {
                mType = 2;
                scaleView.setText("4:3");
                hhPlayer3.changeScale(PlayerViewType.SCREEN_SCALE_4_3);
            } else if (mType == 2) {
                mType = 3;
                scaleView.setText("1:1");
                hhPlayer3.changeScale(PlayerViewType.SCREEN_SCALE_1_1);
            } else if (mType == 3) {
                mType = 4;
                scaleView.setText("拉伸全屏");
                hhPlayer3.changeScale(PlayerViewType.SCREEN_SCALE_FULL);
            } else if (mType == 4) {
                mType = 0;
                scaleView.setText("默认比例");
                hhPlayer3.changeScale(PlayerViewType.SCREEN_SCALE_DEFAULT);
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
                layout_list.setVisibility(VISIBLE);
            } else {
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

    private void showPlayControl() {
        showTime = isPanelShow ? 3 : 0;
        mBottomContainer.setVisibility(isPanelShow ? VISIBLE : INVISIBLE);
        mTopContainer.setVisibility(isPanelShow ? VISIBLE : GONE);
//        mLockScreen.setVisibility(isPanelShow ? VISIBLE : GONE);
    }

    private void playControl() {
        if (hhPlayer3.isPlaying()) {
            hhPlayer3.pause();
            showMidStart(true);
        } else {
            hhPlayer3.start();
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
                isPanelShow = true;
                showPlayControl();
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
                isPanelShow = true;
                showPlayControl();
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
        if (isPanelShow) {
            showTime--;
        }
        mCurrentTimeTextView.post(new Runnable() {
            @Override
            public void run() {
                if (showTime == 0) {
                    isPanelShow = false;
                    showPlayControl();
                }
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
}
