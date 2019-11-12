package com.xing.hhplayer.common.TvListBean

class UrlData(var channelURL: String, urlClarity: TypeClarity, urlState: TypeState) {
    var channelClarity: Int = TypeClarity.未知.value//清晰度：-1未知，0标清，1高清，2 4k ，3 8k
    var channelState: Int = TypeState.未知.value//-1未知，0流畅，1卡顿，2延迟，3有问题(有视频没声音，视频不正常，花屏等)，4不可用
    var channelDetail: UrlDetail? = null//视频信息

    init {
        this.channelClarity = urlClarity.value
        this.channelState = urlState.value
    }

    fun getClarity(): TypeClarity {
        return TypeClarity.values()[channelClarity + 1]
    }

    fun getState(): TypeState {
        return TypeState.values()[channelState + 1]
    }
}