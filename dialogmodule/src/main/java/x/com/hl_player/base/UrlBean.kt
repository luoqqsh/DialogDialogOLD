package x.com.hl_player.base


class UrlBean {
    var url: String? = ""
    var isAvailable: Int = -1//-1未知，0流畅，1卡顿，2超时，3有问题(有视频没声音，视频不正常，花屏等)

    constructor(url: String?) {
        this.url = url
    }
}