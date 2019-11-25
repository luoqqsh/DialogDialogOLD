package com.xing.hhplayer.common.base;

import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;

import java.util.List;

public interface ControlViewInterface {
    void getPrePareState(PlayerPrePareStateType playerPrePareStateType);//准备状态改变

    void getPlayState(PlayerStateType playerStateType);//播放状态改变

    void changeProgress(long time);//进度改变

    void receiveKeyDown(int keyCode);//按键触发，tv专用

    void refreshPlayList(List<TvDataList> tvDataLists);//刷新播放列表

}
