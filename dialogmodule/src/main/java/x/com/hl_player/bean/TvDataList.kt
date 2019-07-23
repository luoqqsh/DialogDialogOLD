package x.com.hl_player.bean

import java.util.*

class TvDataList(typeClassCode: TypeClass, channelName: String, channelUrl: String, clarity: TypeClarity, state: TypeState) {
    var type: String? = null//视频分类
    var typeClassCode: TypeClass? = typeClassCode//视频分类编号
    var urls = ArrayList<UrlData>()//视频播放地址
    var channelUrl: String? = null//主要播放地址
    var thumbUrl: String? = null//封面图片
    var channelName: String? = channelName//频道名称

    init {
        type = typeClassCode.name
        val url = UrlData(channelUrl, clarity, state)
        this.urls.add(url)
        this.channelUrl = urls[0].channelUrl
    }

    fun addUrl(channelUrl: String, clarity: TypeClarity, state: TypeState) {
        urls.add(UrlData(channelUrl, clarity, state))
    }
}