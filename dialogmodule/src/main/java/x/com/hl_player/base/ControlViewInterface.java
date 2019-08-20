package x.com.hl_player.base;

public interface ControlViewInterface {
    void getState();

    void changeProgress(long time);

    void prepared();
}
