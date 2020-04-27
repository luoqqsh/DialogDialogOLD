package com.xing.hhplayer.common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.base.ControlViewInterface;
import com.xing.hhplayer.common.tvbean.TvDataList;

import java.util.List;

import x.com.hl_log.ViseLog;

public abstract class PBaseVideoView extends FrameLayout implements View.OnClickListener, ControlViewInterface {
    public Context mContext;
    public HHPlayer3 hhPlayer3;

    public PBaseVideoView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public PBaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PBaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public View initPlayer(HHPlayer3 hhPlayer3) {
        setId(R.id.video_view_id);
        this.hhPlayer3 = hhPlayer3;
        hhPlayer3.setCvInterface(this);
        initView();
        return this;
    }

    protected abstract void initView();

    @Override
    public void getPlayState(PlayerStateType playerStateType) {

    }

    @Override
    public void changeProgress(long time) {

    }

    @Override
    public boolean receiveKeyDown(int keyCode) {
        return false;
    }

    @Override
    public void refreshPlayList(List<TvDataList> tvDataLists) {

    }

    @Override
    public void getPlayBuffer(int progress) {

    }

    @Override
    public void getPrePareState(PlayerPrePareStateType playerPrePareStateType) {

    }

    @Override
    public void onClick(View v) {

    }

    //有问题
    public void addViewWithTag(View view, String tag) {
        View tempView = findViewWithTag(tag);
        if (tempView == null) {
            view.setTag(tag);
            addView(view);
        }
    }

    public void removeViewByTag(String tag) {
        View tempView = findViewWithTag(tag);
        if (tempView != null) {
            removeView(tempView);
        }
    }
}
