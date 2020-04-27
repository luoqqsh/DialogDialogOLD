package com.xing.hhplayer.common.View;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.TypeBean.PlayerViewType;
import com.xing.hhplayer.common.base.PlayerDoubleClickListener;
import com.xing.hhplayer.common.tvbean.TvDataList;

import java.util.List;

import x.com.hl_tools.hl_time;
import x.com.hl_tools.hl_volume;

public class PTVView extends PBaseVideoView {
    private ViewGroup mTopContainer, mBottomContainer, layout_list, seekBarLayout;
    private Context mActivity;
    private View contentPanel;
    private CListView cListView;
    private View mStartButton;
    private TextView mCurrentTimeTextView, mTotalTimeTextView;
    private TextView mTvTitle;
    private SeekBar mProgressBar;
    private boolean isPanelShow = true;//是否显示面板
    private boolean isShowList = false;//是否显示列表
    private boolean isShowSeekBar = false;//是否显示进度条
    private int showTime = 3;
    private CLoadingView cLoadingView;

    public PTVView(@NonNull Context context) {
        super(context);
        this.mActivity = context;
    }

    public PTVView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PTVView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        cLoadingView = new CLoadingView(mContext);
        View.inflate(mActivity, getLayoutId(), this);
        //top
        mTopContainer = findViewById(R.id.liveTopLayout);
        mTvTitle = findViewById(R.id.tvTitle);
        //bottom
        seekBarLayout = findViewById(R.id.liveSeekLayout);
        mBottomContainer = findViewById(R.id.liveBottomLayout);
        mCurrentTimeTextView = findViewById(R.id.livePlayTime);
        contentPanel = findViewById(R.id.contentPanel);
        mTotalTimeTextView = findViewById(R.id.liveTotalTime);
        mProgressBar = findViewById(R.id.liveSeek);

        //list
        layout_list = findViewById(R.id.layout_list);
        //mid
        mStartButton = findViewById(R.id.loadingMidPause);
        mStartButton.setOnClickListener(this);
        contentPanel.setOnClickListener(new PlayerDoubleClickListener(new PlayerDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                playControl();
            }

            @Override
            public void onOneClick() {
                isPanelShow = !isPanelShow;
                showPlayControl();
            }
        }));
    }

    private int getLayoutId() {
        return R.layout.player_ptv;
    }

    private int mType = 0;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.loadingMidPause) {
            playControl();
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
        //下面先不显示
//        mBottomContainer.setVisibility(isPanelShow ? VISIBLE : INVISIBLE);
        mTopContainer.setVisibility(isPanelShow ? VISIBLE : GONE);
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
    public void getPlayBuffer(int progress) {
        if (progress >= 0 && progress < 100) {
            addViewWithTag(cLoadingView, "LoadingView");
        } else {
            removeViewByTag("LoadingView");
        }
    }

    @Override
    public void getPrePareState(PlayerPrePareStateType playerPrePareStateType) {
        switch (playerPrePareStateType) {
            case PREPARING:
                addViewWithTag(cLoadingView, "LoadingView");
                break;
            case PREPARED:
                removeViewByTag("LoadingView");
                if (TextUtils.isEmpty(hhPlayer3.getTitle())) {
                    mTvTitle.setText(hhPlayer3.getMediaPath().toString());
                } else {
                    mTvTitle.setText(hhPlayer3.getTitle());
                }
                if (hhPlayer3.getMediaDurationMS() != 0) {
                    isShowSeekBar = true;
                    seekBarLayout.setVisibility(VISIBLE);
                    mProgressBar.setMax((int) (hhPlayer3.getMediaDurationMS() / 1000));
                    mTotalTimeTextView.setText(hl_time.formatTime(hhPlayer3.getMediaDurationMS()));
                } else {
                    isShowSeekBar = false;
                    seekBarLayout.setVisibility(INVISIBLE);
                }
                if (hhPlayer3.getUrlDataList() != null && cListView == null) {
                    cListView = new CListView(mActivity, hhPlayer3.getUrlDataList(), hhPlayer3);
                    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.RIGHT;
                    layout_list.addView(cListView, params);
                    layout_list.setVisibility(GONE);//默认隐藏
                }
                isPanelShow = true;
                showPlayControl();
                break;
            case NONE:
            case ERROR:
                isPanelShow = true;
                showPlayControl();
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
    public boolean receiveKeyDown(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP://导航上
                if (!isShowList) {
                    upStep();
                } else {
                    downStep();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN://导航下
                if (!isShowList) {
                    downStep();
                } else {
                    upStep();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT://导航左
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT://导航右
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP://音量+
                hl_volume.mediaVolumeUp(mActivity);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN://音量-
                hl_volume.mediaVolumeDown(mActivity);
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER://导航-确定
                //列表没显示则显示列表
                if (!isShowList) {
                    isShowList = true;
                    layout_list.setVisibility(VISIBLE);
                } else {
                    //列表已显示播放选中频道
                    hhPlayer3.prepare(hhPlayer3.getUrlDataList(), cListView.getCurrentSelect());
                }
                return true;
            case KeyEvent.KEYCODE_BACK://返回键
                if (isShowList) {
                    isShowList = false;
                    layout_list.setVisibility(GONE);
                    return true;
                }
//                if (System.currentTimeMillis() - mExitTime > 2000) {
//                    Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                    mExitTime = System.currentTimeMillis();
//                } else {
//                    mActivity.finish();
//                }
                hhPlayer3.getActivity().finish();
                return false;
            case KeyEvent.KEYCODE_MENU://菜单键
                return true;
            default:
                return false;
        }
    }

    @Override
    public void refreshPlayList(List<TvDataList> tvDataLists) {
        cListView.refreshList(tvDataLists);
    }

    private void upStep() {
        int currentSelectUp = cListView.getCurrentSelect() + 1;
        if (currentSelectUp >= cListView.getListSize()) {
            currentSelectUp = 0;
        }
        cListView.setCurrentSelectPlay(currentSelectUp);
        if (!isShowList) {
            hhPlayer3.prepare(hhPlayer3.getUrlDataList(), currentSelectUp);
        }
    }

    private void downStep() {
        int currentSelectDown = cListView.getCurrentSelect() - 1;
        if (currentSelectDown < 0) {
            currentSelectDown = cListView.getListSize() - 1;
        }
        cListView.setCurrentSelectPlay(currentSelectDown);
        if (!isShowList) {
            hhPlayer3.prepare(hhPlayer3.getUrlDataList(), currentSelectDown);
        }
    }
}
