package x.com.hl_player.View;

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

import java.util.Timer;
import java.util.TimerTask;

import x.com.hl_base.OnDoubleClickListener;
import x.com.hl_dialog.R;
import x.com.hl_player.HHPlayer3;
import x.com.hl_player.TypeBean.PlayerPrePareStateType;
import x.com.hl_player.TypeBean.ScaleType;
import x.com.hl_player.base.ControlViewInterface;
import x.com.hl_tools.hl_time;

public class HHControlView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    private ViewGroup mTopContainer, mBottomContainer, loading_layout, layout_list, seekbarlayout;
    private View contentPanel;
    private HHPlayer3 hhPlayer3;
    private View mStartButton;
    private ImageView mFullscreenButton;
    private ImageView mBackButton;
    private ImageView mLockScreen;
    private TextView mCurrentTimeTextView, mTotalTimeTextView;
    private TextView mTitleTextView;
    protected SeekBar mProgressBar;
    private boolean isPanelShow = true;
    private RotateImageView loading;
    private Context context;
    private TextView playlist;
    private boolean isShowList = false;
    private boolean isShowSeekBar = false;

    private Timer mDismissControlViewTimer;
    private HHListView hhListView;

    public HHControlView(@NonNull Context context, HHPlayer3 hhPlayer3) {
        super(context);
        this.hhPlayer3 = hhPlayer3;
        this.context = context;
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
        mTopContainer = findViewById(R.id.layout_top);
        mTitleTextView = findViewById(R.id.title);
        mBackButton = findViewById(R.id.back);

        //bottom
        seekbarlayout = findViewById(R.id.seekbarlayout);
        mBottomContainer = findViewById(R.id.layout_bottom);
        mFullscreenButton = findViewById(R.id.fullscreen);
        mCurrentTimeTextView = findViewById(R.id.current);
        contentPanel = findViewById(R.id.contentPanel);
        mTotalTimeTextView = findViewById(R.id.total);
        mProgressBar = findViewById(R.id.seek);

        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.scaleView).setOnClickListener(this);
        //list
        layout_list = findViewById(R.id.layout_list);
        //mid
        loading_layout = findViewById(R.id.loading_layout);
        mLockScreen = findViewById(R.id.lock_screen);
        loading = findViewById(R.id.loading);
        mStartButton = findViewById(R.id.play_pause);
        playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(this);
        mStartButton.setOnClickListener(this);
        contentPanel.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
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

    }

    private int getLayoutId() {
        return R.layout.player_liveview;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.play || id == R.id.play_pause) {
            playControl();
        }
        if (id == R.id.scaleView) {
            hhPlayer3.setVideoScaleType(ScaleType.SCALE_4_3);
        }
        if (id == R.id.playlist) {
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
                loading.setVisibility(VISIBLE);
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
                loading.setVisibility(GONE);
                loading_layout.setVisibility(GONE);
                loading.stop();
                if (hhPlayer3.getMediaDuration() != 0) {
                    isShowSeekBar = true;
                    seekbarlayout.setVisibility(VISIBLE);
                    mProgressBar.setMax((int) (hhPlayer3.getMediaDuration() / 1000));
                    mTotalTimeTextView.setText(hl_time.formatTime(hhPlayer3.getMediaDuration()));
                } else {
                    isShowSeekBar = false;
                    seekbarlayout.setVisibility(INVISIBLE);
                }
                if (hhPlayer3.getUrlDataList() != null && hhListView == null) {
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.RIGHT;
                    hhListView = new HHListView(context, hhPlayer3.getUrlDataList(), hhPlayer3);
                    layout_list.addView(hhListView, params);
                    hhListView.refreshList(hhPlayer3.getUrlDataList());
                    playlist.setVisibility(VISIBLE);
                    layout_list.setVisibility(GONE);
                }

                break;
        }
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
