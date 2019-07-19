package x.com.hl_player.base

import java.util.*

class TvBean {
    private var tvInfo: TvInfo? = null//视频信息
    private var type: String? = null//视频分类
    private var typeCode: TvType? = null//视频分类编号
    private var urls = ArrayList<UrlBean>()//视频播放地址
    private var channelUrl: String? = null//主要播放地址
    private var thumbUrl: String? = null//封面图片
    private var channelName: String? = null//频道名称

    constructor(typeCode: TvType, channelUrl: String, channelName: String) {
        this.typeCode = typeCode
        this.urls.add(UrlBean(channelUrl))
        this.channelName = channelName
        this.channelUrl = channelUrl
        type = typeCode.name

    }

    constructor(typeCode: TvType, channelUrl: String, channelName: String, tvInfo: TvInfo) {
        this.typeCode = typeCode
        this.urls.add(UrlBean(channelUrl))
        this.channelName = channelName
        this.tvInfo = tvInfo
        this.channelUrl = channelUrl
        type = typeCode.name
    }

    fun addUrl(channelUrl: String) {
        urls.add(UrlBean(channelUrl))
    }
}