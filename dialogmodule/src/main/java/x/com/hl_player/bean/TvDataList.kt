package x.com.hl_player.bean

import java.util.*

class TvDataList {
    var type: String? = null//视频分类
    var typeClassCode: TypeClass? = null//视频分类编号
    var urls = ArrayList<UrlData>()//视频播放地址
    var channelUrl: String? = null//主要播放地址
    var thumbUrl: String? = null//封面图片
    var channelName: String? = null//频道名称

    constructor(typeClassCode: TypeClass, channelName: String, channelUrl: String, clarity: TypeClarity, state: TypeState) {
        TvDataList(typeClassCode, channelUrl, channelName, clarity, state, null)
    }

    constructor(typeClassCode: TypeClass, channelName: String, channelUrl: String, clarity: TypeClarity, state: TypeState, urlDetail: UrlDetail?) {
        this.typeClassCode = typeClassCode
        type = typeClassCode.name
        this.channelName = channelName
        val url = UrlData(channelUrl, clarity, state)
        url.urlDetail = urlDetail

        this.urls.add(url)
        this.channelUrl = urls[0].channelUrl
    }

    fun addUrl(channelUrl: String, clarity: TypeClarity, state: TypeState) {
        urls.add(UrlData(channelUrl, clarity, state))
    }
}