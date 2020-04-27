package com.xing.hhplayer.common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.tvbean.TvDataList;

import java.util.List;

import x.com.hl_log.ViseLog;

/**
 * 这是对应的场景view，不是用来添加的view
 */
public class PLiveView extends PBaseVideoView {
    private Context mContext;
    private CLoadingView cLoadingView;
    private CTopBottomView cTopBottomView;

    public PLiveView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public PLiveView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PLiveView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void initView() {
        cLoadingView = new CLoadingView(mContext);
        cTopBottomView = new CTopBottomView(mContext);
        cTopBottomView.setHhPlayer3(hhPlayer3);
        addViewWithTag(cTopBottomView, "TopBottomView");
    }

    @Override
    public void getPrePareState(PlayerPrePareStateType playerPrePareStateType) {
        if (playerPrePareStateType == PlayerPrePareStateType.PREPARING) {
            addViewWithTag(cLoadingView, "LoadingView");
        } else {
            removeViewByTag("LoadingView");
        }
        cTopBottomView.changePrepareState(playerPrePareStateType);
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
    public boolean receiveKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hhPlayer3.getActivity().onBackPressed();
            return false;
        }
        return false;
    }

    @Override
    public void refreshPlayList(List<TvDataList> tvDataLists) {
        cTopBottomView.refreshPlayList(tvDataLists);
    }

    @Override
    public void changeProgress(long time) {
        cTopBottomView.changeProgress(time);
    }

}
