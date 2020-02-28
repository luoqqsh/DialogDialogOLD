package com.xing.hhplayer.common.View;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import x.com.hl_base.hl_interface.OnDoubleClickListener;
import x.com.hl_tools.hl_time;
import x.com.hl_tools.hl_volume;
import x.com.hl_view.RotateImageView;

public class HHTVView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    private ViewGroup mTopContainer, mBottomContainer, loadingLayout, layout_list, seekBarLayout;
    private FragmentActivity mActivity;
    private View contentPanel;
    private HHPlayer3 hhPlayer3;
    private HHListView hhListView;
    private View mStartButton;
    private RotateImageView loading;
    private TextView mCurrentTimeTextView, mTotalTimeTextView;
    private TextView mTvTitle;
    private SeekBar mProgressBar;
    private TextView scaleView;
    private boolean isPanelShow = true;//是否显示面板
    private boolean isShowList = false;//是否显示列表
    private boolean isShowSeekBar = false;//是否显示进度条
    private Timer mDismissControlViewTimer;

    public HHTVView(@NonNull FragmentActivity context, HHPlayer3 hhPlayer3) {
        super(context);
        this.hhPlayer3 = hhPlayer3;
        this.mActivity = context;
        hhPlayer3.setCvInterface(this);
        initView(context);
    }

    public HHTVView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HHTVView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        View.inflate(context, getLayoutId(), this);
        //top
        mTopContainer = findViewById(R.id.layout_top);
        mTvTitle = findViewById(R.id.tvTitle);
        //bottom
        seekBarLayout = findViewById(R.id.seekBarLayout);
        mBottomContainer = findViewById(R.id.layout_bottom);
        mCurrentTimeTextView = findViewById(R.id.current);
        contentPanel = findViewById(R.id.contentPanel);
        mTotalTimeTextView = findViewById(R.id.total);
        mProgressBar = findViewById(R.id.seek);

        scaleView = findViewById(R.id.scaleView);
        scaleView.setOnClickListener(this);
        //list
        layout_list = findViewById(R.id.layout_list);
        //mid
        loadingLayout = findViewById(R.id.loadingLayout);
        loading = findViewById(R.id.loadingView);
        mStartButton = findViewById(R.id.loadingMidPause);
        mStartButton.setOnClickListener(this);
        contentPanel.setOnClickListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                playControl();
            }

            @Override
            public void onOneClick() {
                isPanelShow = !isPanelShow;
                showPlayControl(isPanelShow);
            }
        }));
    }

    private int getLayoutId() {
        return R.layout.hhplayer_tvview;
    }

    private int mType = 0;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.loadingMidPause) {
            playControl();
        }
        //显示比例
        if (id == R.id.scaleView) {
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
    }

    private void showMidStart(boolean isShow) {
        if (isShow) {
            mStartButton.setVisibility(VISIBLE);
        } else {
            mStartButton.setVisibility(GONE);
        }
    }

    private void showPlayControl(boolean isPanelShow) {
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
        if (progress > 0 && progress < 100) {
            loading.setVisibility(VISIBLE);
            loading.start();
        } else {
            loading.setVisibility(GONE);
            loading.stop();
        }
    }

    @Override
    public void getPrePareState(PlayerPrePareStateType playerPrePareStateType) {
        switch (playerPrePareStateType) {
            case PREPARING:
                loadingLayout.setVisibility(VISIBLE);
                loading.start();
                break;
            case PREPARED:
                if (TextUtils.isEmpty(hhPlayer3.getTitle())) {
                    mTvTitle.setText(hhPlayer3.getMediaPath().toString());
                } else {
                    mTvTitle.setText(hhPlayer3.getTitle());
                }
                //隐藏加载转圈
                loadingLayout.setVisibility(GONE);
                loading.stop();
                if (hhPlayer3.getMediaDurationMS() != 0) {
                    isShowSeekBar = true;
                    seekBarLayout.setVisibility(VISIBLE);
                    mProgressBar.setMax((int) (hhPlayer3.getMediaDurationMS() / 1000));
                    mTotalTimeTextView.setText(hl_time.formatTime(hhPlayer3.getMediaDurationMS()));
                } else {
                    isShowSeekBar = false;
                    seekBarLayout.setVisibility(INVISIBLE);
                }
                if (hhPlayer3.getUrlDataList() != null && hhListView == null) {
                    hhListView = new HHListView(mActivity, hhPlayer3.getUrlDataList(), hhPlayer3);
                    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.RIGHT;
                    layout_list.addView(hhListView, params);
                    layout_list.setVisibility(GONE);//默认隐藏
                }
                isPanelShow = true;
                showPlayControl(true);
                startDismissTimer();
                break;
            case NONE:
            case ERROR:
                cancelDismissTimer();
                loadingLayout.setVisibility(GONE);
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
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP://导航上
                if (!isShowList) {
                    upStep();
                } else {
                    downStep();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://导航下
                if (!isShowList) {
                    downStep();
                } else {
                    upStep();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT://导航左
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT://导航右
                break;
            case KeyEvent.KEYCODE_VOLUME_UP://音量+
                hl_volume.mediaVolumeUp(mActivity);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN://音量-
                hl_volume.mediaVolumeDown(mActivity);
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER://导航-确定
                //列表没显示则显示列表
                if (!isShowList) {
                    isShowList = true;
                    layout_list.setVisibility(VISIBLE);
                } else {
                    //列表已显示播放选中频道
                    hhPlayer3.prepare(hhPlayer3.getUrlDataList(), hhListView.getCurrentSelect());
                }
                break;
            case KeyEvent.KEYCODE_BACK://返回键
                if (isShowList) {
                    isShowList = false;
                    layout_list.setVisibility(GONE);
                    return;
                }
//                if (System.currentTimeMillis() - mExitTime > 2000) {
//                    Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                    mExitTime = System.currentTimeMillis();
//                } else {
//                    mActivity.finish();
//                }
                cancelDismissTimer();
                mActivity.finish();
                break;
            case KeyEvent.KEYCODE_MENU://菜单键
                break;
        }
    }

    @Override
    public void refreshPlayList(List<TvDataList> tvDataLists) {
        hhListView.refreshList(tvDataLists);
    }

    private void upStep() {
        int currentSelectUp = hhListView.getCurrentSelect() + 1;
        if (currentSelectUp >= hhListView.getListSize()) {
            currentSelectUp = 0;
        }
        hhListView.setCurrentSelectPlay(currentSelectUp);
        if (!isShowList) {
            hhPlayer3.prepare(hhPlayer3.getUrlDataList(), currentSelectUp);
        }
    }

    private void downStep() {
        int currentSelectDown = hhListView.getCurrentSelect() - 1;
        if (currentSelectDown < 0) {
            currentSelectDown = hhListView.getListSize() - 1;
        }
        hhListView.setCurrentSelectPlay(currentSelectDown);
        if (!isShowList) {
            hhPlayer3.prepare(hhPlayer3.getUrlDataList(), currentSelectDown);
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
