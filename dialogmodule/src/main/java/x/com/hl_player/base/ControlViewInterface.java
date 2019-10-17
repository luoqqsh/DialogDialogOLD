package x.com.hl_player.base;

import x.com.hl_player.TypeBean.PlayerPrePareStateType;
import x.com.hl_player.TypeBean.PlayerStateType;

public interface ControlViewInterface {
    void getPrePareState(PlayerPrePareStateType playerPrePareStateType);

    void getPlayState(PlayerStateType playerStateType);

    void changeProgress(long time);

}
