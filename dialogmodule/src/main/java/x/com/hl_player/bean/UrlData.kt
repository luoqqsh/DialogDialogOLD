package x.com.hl_player.bean

class UrlData(var channelUrl: String, urlClarity: TypeClarity, urlState: TypeState) {
    var urlClarity: Int = TypeClarity.未知.value//清晰度：-1未知，0标清，1高清，2 4k ，3 8k
    var urlState: Int = TypeState.未知.value//-1未知，0流畅，1卡顿，2延迟，3有问题(有视频没声音，视频不正常，花屏等)，4不可用
    var urlDetail: UrlDetail? = null//视频信息

    init {
        this.urlClarity = urlClarity.value
        this.urlState = urlState.value
    }

    fun getClarity(): TypeClarity {
        return TypeClarity.values()[urlClarity + 1]
    }

    fun getState(): TypeState {
        return TypeState.values()[urlState + 1]
    }
}