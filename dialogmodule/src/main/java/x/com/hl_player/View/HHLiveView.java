package x.com.hl_player.View;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import x.com.hl_base.OnDoubleClickListener;
import x.com.hl_dialog.R;
import x.com.hl_player.HHPlayer3;
import x.com.hl_player.TypeBean.PlayerPrePareStateType;
import x.com.hl_player.base.ControlViewInterface;
import x.com.hl_tools.hl_time;

public class HHLiveView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    private HHPlayer3 hhPlayer3;
    //播放按键
    protected View mStartButton;
    //全屏按键
    protected ImageView mFullscreenButton;
    //返回按键
    protected ImageView mBackButton;
    //锁定图标
    protected ImageView mLockScreen;
    //时间显示
    protected TextView mCurrentTimeTextView;
    //title
    protected TextView mTitleTextView;
    //顶部和底部区域
    protected ViewGroup mTopContainer, mBottomContainer;
    //触摸显示消失定时
    protected Timer mDismissControlViewTimer;
    private View contentPanel;
    private boolean isPanelShow = true;
    private RotateImageView loading;
    private ViewGroup loading_layout;

    public HHLiveView(@NonNull Context context, HHPlayer3 hhPlayer3) {
        super(context);
        this.hhPlayer3 = hhPlayer3;
        hhPlayer3.setCvInterface(this);
        initView(context);
    }

    public HHLiveView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HHLiveView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        View.inflate(context, getLayoutId(), this);
        //top
        mTopContainer = findViewById(R.id.layout_top);
        mTitleTextView = findViewById(R.id.title);
        mBackButton = findViewById(R.id.back);

        //bottom
        mBottomContainer = findViewById(R.id.layout_bottom);
        mFullscreenButton = findViewById(R.id.fullscreen);
        mCurrentTimeTextView = findViewById(R.id.current);
        TextView play = findViewById(R.id.play);
        contentPanel = findViewById(R.id.contentPanel);

        //mid
        loading_layout = findViewById(R.id.loading_layout);
        mLockScreen = findViewById(R.id.lock_screen);
        loading = findViewById(R.id.loading);
        mStartButton = findViewById(R.id.play_pause);
        play.setOnClickListener(this);
        mStartButton.setOnClickListener(this);
        contentPanel.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
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
        return R.layout.player_liveview;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.play || id == R.id.play_pause) {
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

    private void showPlayControl(boolean isPanelShow) {
        if (isPanelShow && hhPlayer3.isPlaying()) {
            startDismissTimer();
        } else {
            cancelDismissTimer();
        }
        mBottomContainer.setVisibility(isPanelShow ? VISIBLE : INVISIBLE);
        mTopContainer.setVisibility(isPanelShow ? VISIBLE : GONE);
        mLockScreen.setVisibility(isPanelShow ? VISIBLE : GONE);
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
                break;
        }
    }

    @Override
    public void changeProgress(final long time) {
        mCurrentTimeTextView.post(new Runnable() {
            @Override
            public void run() {
                mCurrentTimeTextView.setText(hl_time.formatTime(time));
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
