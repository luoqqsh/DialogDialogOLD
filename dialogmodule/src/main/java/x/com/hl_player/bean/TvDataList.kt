package x.com.hl_player.bean

import java.util.*

class TvDataList(typeClass: TypeClass, channelName: String, channelUrl: String, clarity: TypeClarity, state: TypeState) {
    var channelName: String = channelName//频道名称
    var channelClass: Int = typeClass.value//频道分类
    var channelThumb: String? = null//封面图片
    var channelUrls = ArrayList<UrlData>()//频道播放地址

    var channelUrl: String? = null//主要播放地址
    var channelClarity: Int = TypeClarity.未知.value//清晰度：-1未知，0标清，1高清，2 4k ，3 8k
    var channelState: Int = TypeState.未知.value//-1未知，0流畅，1卡顿，2延迟，3有问题(有视频没声音，视频不正常，花屏等)，4不可用

    init {
        val url = UrlData(channelUrl, clarity, state)
        this.channelUrls.add(url)
        this.channelUrl = channelUrls[0].channelUrl
        this.channelClarity = channelUrls[0].urlClarity
        this.channelState = channelUrls[0].urlState
    }

    fun addUrl(channelUrl: String, clarity: TypeClarity, state: TypeState) {
        channelUrls.add(UrlData(channelUrl, clarity, state))
    }

    fun getClassific(): TypeClass {
        return TypeClass.values()[channelClass]
    }
}