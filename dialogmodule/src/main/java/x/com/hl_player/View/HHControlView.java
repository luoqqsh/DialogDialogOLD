package x.com.hl_player.View;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Timer;

import x.com.hl_base.HOnListener;
import x.com.hl_dialog.R;
import x.com.hl_player.HHPlayer3;
import x.com.hl_player.base.ControlViewInterface;
import x.com.hl_tools.hl_time;

public class HHControlView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    private HHPlayer3 hhPlayer3;
    //播放按键
    protected View mStartButton;

    //封面
    protected View mThumbImageView;

    //loading view
    protected View mLoadingProgressBar;

    //进度条
    protected SeekBar mProgressBar;

    //全屏按键
    protected ImageView mFullscreenButton;

    //返回按键
    protected ImageView mBackButton;

    //锁定图标
    protected ImageView mLockScreen;

    //时间显示
    protected TextView mCurrentTimeTextView, mTotalTimeTextView;

    //title
    protected TextView mTitleTextView;

    //顶部和底部区域
    protected ViewGroup mTopContainer, mBottomContainer;

    //封面父布局
    protected RelativeLayout mThumbImageViewLayout;

    //底部进度调
    protected ProgressBar mBottomProgressBar;

    //进度定时器
    protected Timer updateProcessTimer;

    //触摸显示消失定时
    protected Timer mDismissControlViewTimer;
    private TextView play;
    private View contentPanel;
    private boolean isPanelShow = false;

    public HHControlView(@NonNull Context context, HHPlayer3 hhPlayer3) {
        super(context);
        this.hhPlayer3 = hhPlayer3;
        hhPlayer3.setCvInterface(this);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, getLayoutId() != 0 ? getLayoutId() : R.layout.player_controlview, this);
        mTitleTextView = findViewById(R.id.title);
        mBackButton = findViewById(R.id.back);
        mFullscreenButton = findViewById(R.id.fullscreen);
        mProgressBar = findViewById(R.id.progress);
        mCurrentTimeTextView = findViewById(R.id.current);
        mTotalTimeTextView = findViewById(R.id.total);
        mTopContainer = findViewById(R.id.layout_top);
        mBottomProgressBar = findViewById(R.id.bottom_progressbar);
        mThumbImageViewLayout = findViewById(R.id.thumb);
        mLockScreen = findViewById(R.id.lock_screen);

        //底部按键
        mBottomContainer = findViewById(R.id.layout_bottom);
        play = findViewById(R.id.play);
        contentPanel = findViewById(R.id.contentPanel);

        play.setOnClickListener(this);
        contentPanel.setOnClickListener(this);

        hhPlayer3.setPreparedListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                mTotalTimeTextView.setText(String.valueOf(hhPlayer3.getMediaDuration()));
            }
        });
    }

    private int getLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.play) {
            if (hhPlayer3.isPlaying()) {
                hhPlayer3.pause();
            } else {
                hhPlayer3.start();
            }
        } else if (id == R.id.contentPanel) {
            mBottomContainer.setVisibility(isPanelShow ? VISIBLE : INVISIBLE);
            mTopContainer.setVisibility(isPanelShow ? VISIBLE : GONE);
            mLockScreen.setVisibility(isPanelShow ? VISIBLE : GONE);
            isPanelShow = !isPanelShow;
        }
    }

    @Override
    public void getState() {

    }

    @Override
    public void changeProgress(final long time) {
        mProgressBar.post(new Runnable() {
            @Override
            public void run() {
                mCurrentTimeTextView.setText(hl_time.formatTime(time));
                mProgressBar.setProgress((int) (time / 1000));
            }
        });
    }

    @Override
    public void prepared() {
        if (TextUtils.isEmpty(hhPlayer3.getTitle())) {
            mTitleTextView.setText(hhPlayer3.getMediaPath().toString());
        } else {
            mTitleTextView.setText(hhPlayer3.getTitle());
        }
        mTotalTimeTextView.setText(hl_time.formatTime(hhPlayer3.getMediaDuration()));
        mProgressBar.setMax((int) (hhPlayer3.getMediaDuration() / 1000));
    }
}
