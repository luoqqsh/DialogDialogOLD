package com.xing.hhplayer.common.base;

import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;

public interface ControlViewInterface {
    void getPrePareState(PlayerPrePareStateType playerPrePareStateType);

    void getPlayState(PlayerStateType playerStateType);

    void changeProgress(long time);

}
