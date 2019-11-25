package com.xing.hhplayer.common.TvListBean

import java.util.*

class TvDataList {
    var channelName: String = ""//频道名称
    var channelClass: Int = -1//频道分类
    var channelThumb: String? = null//封面图片
    var isChannelSelect: Boolean? = false//封面图片
    var channelURLs = ArrayList<UrlData>()//频道播放地址

    constructor(typeClass: TypeClass, channelName: String, channelUrl: String, clarity: TypeClarity, state: TypeState) {
        val url = UrlData(channelUrl, clarity, state)
        this.channelURLs.add(url)
        this.channelName = channelName
        this.channelClass = typeClass.value
    }

    constructor(typeClass: TypeClass, channelName: String) {
        this.channelName = channelName
        this.channelClass = typeClass.value
    }

    fun addUrl(channelUrl: String, clarity: TypeClarity, state: TypeState) {
        channelURLs.add(UrlData(channelUrl, clarity, state))
    }

    fun getClassific(): TypeClass {
        return TypeClass.values()[channelClass]
    }
}