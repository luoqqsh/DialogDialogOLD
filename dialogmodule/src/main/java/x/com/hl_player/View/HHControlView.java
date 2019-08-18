package x.com.hl_player.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;

import x.com.hl_dialog.R;

public class HHControlView extends FrameLayout {
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

    public HHControlView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public HHControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HHControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        mBottomContainer = findViewById(R.id.layout_bottom);
        mTopContainer = findViewById(R.id.layout_top);
        mBottomProgressBar = findViewById(R.id.bottom_progressbar);
        mThumbImageViewLayout = findViewById(R.id.thumb);
        mLockScreen = findViewById(R.id.lock_screen);
    }

    private int getLayoutId() {
        return 0;
    }
}
