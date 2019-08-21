package x.com.hl_player.base;

import x.com.hl_player.TypeBean.PlayerPrePareStateType;

public interface ControlViewInterface {
    void getPrePareState(PlayerPrePareStateType playerPrePareStateType);

    void changeProgress(long time);
}
