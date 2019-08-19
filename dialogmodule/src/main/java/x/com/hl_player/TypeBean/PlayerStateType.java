package x.com.hl_player.TypeBean;

public enum PlayerStateType {
    ERROR,//出错
    IDLE,//闲置，初始化
    PLAYING,//播放中
    PREPARED,//准备就绪,准备好未播放的状态，判断准备状态以PlayerPrePareState为准
    PAUSE,//暂停中
    PHONE,//通话中
    PLAYBACK_COMPLETED,//播放完成
    NONE//结束，需要重新new
}
