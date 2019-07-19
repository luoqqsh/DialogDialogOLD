package x.com.hl_player.base


class TvInfo {
    private var vWidth: Int = 0//视频宽
    private var vHeight: Int = 0//视频高
    private var vDuration: Int = 0//视频时长
    private var vBitRate: Int = 0//码率
    private var vTotalFrames: Int = 0//帧数
    private var vFrameRate: Float = 0.toFloat()//频率
    private var vRotateAngle: Float = 0.toFloat()//旋转角度
    private var vHasBFrame: Boolean = false//是否存在B帧。即双向预测帧, 如果有的话, 在裁剪时需要注意, 目前大部分的视频不存在B帧.
    private var aSampleRate: Int = 0//音频采样率
    private var aChannels: Int = 0//音频通道数量
    private var aDuration: Float = 0.toFloat()//音频总时长
    private var aCodecName: String? = null//音频可以用的 解码器
}