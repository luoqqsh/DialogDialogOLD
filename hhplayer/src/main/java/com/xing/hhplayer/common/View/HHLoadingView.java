package com.xing.hhplayer.common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.base.ControlViewInterface;
import com.xing.hhplayer.common.base.HHPlayerCore;

import java.util.List;

import x.com.hl_base.hl_interface.OnDoubleClickListener;
import x.com.hl_livedatabus.LiveEventBus;
import x.com.hl_log.ViseLog;
import x.com.hl_view.RotateImageView;

public class HHLoadingView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    private HHPlayer3 hhPlayer3;
    //播放按键
    protected View mStartButton;
    //锁定图标
    protected ImageView mLockScreen;
    //顶部和底部区域
    protected ViewGroup mTopContainer, mBottomContainer;

    private RelativeLayout contentPanel;
    private boolean isPanelShow = false;
    private RotateImageView loading;
    private ViewGroup loading_layout;

    public HHLoadingView(@NonNull FragmentActivity context, HHPlayer3 hhPlayer3) {
        super(context);
        this.hhPlayer3 = hhPlayer3;
        hhPlayer3.setCvInterface(this);
        initView(context);
    }

    public HHLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HHLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(FragmentActivity mActivity) {
        View.inflate(mActivity, getLayoutId() != 0 ? getLayoutId() : R.layout.hhplayer_loadview, this);

        loading_layout = findViewById(R.id.loadingLayout);
        contentPanel = findViewById(R.id.contentPanel);
        loading = findViewById(R.id.loadingView);
        loading.start();
        mStartButton = findViewById(R.id.loadingMidPause);
        mStartButton.setOnClickListener(this);
        contentPanel.setOnClickListener(this);
        contentPanel.setOnClickListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                playControl();
            }

            @Override
            public void onOneClick() {

            }
        }));
        LiveEventBus.get("HHPlayerPlayState", HHPlayerCore.class).observe(mActivity, new Observer<HHPlayerCore>() {
            @Override
            public void onChanged(HHPlayerCore hhPlayerCore) {
                ViseLog.d(hhPlayerCore);
            }
        });
    }

    private int getLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.loadingMidPause) {
            playControl();
        } else if (id == R.id.contentPanel) {
            mBottomContainer.setVisibility(isPanelShow ? VISIBLE : INVISIBLE);
            mTopContainer.setVisibility(isPanelShow ? VISIBLE : GONE);
            mLockScreen.setVisibility(isPanelShow ? VISIBLE : GONE);
            isPanelShow = !isPanelShow;
        }
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

    private void showMidStart(boolean isShow) {
        if (isShow) {
            mStartButton.setVisibility(VISIBLE);
        } else {
            mStartButton.setVisibility(GONE);
        }
    }

    @Override
    public void getPlayState(PlayerStateType playerStateType) {
        switch (playerStateType) {
            case PAUSE:
                showMidStart(true);
                break;
            case PLAYING:
                showMidStart(false);
                break;
        }
    }

    @Override
    public void getPrePareState(PlayerPrePareStateType playerPrePareStateType) {
        if (playerPrePareStateType == PlayerPrePareStateType.PREPARING) {
            loading_layout.setVisibility(VISIBLE);
            loading.start();
        } else {
            loading_layout.setVisibility(GONE);
            loading.stop();
        }
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

    @Override
    public void changeProgress(final long time) {

    }

    @Override
    public void receiveKeyDown(int keyCode) {

    }

    @Override
    public void refreshPlayList(List<TvDataList> tvDataLists) {

    }
}
