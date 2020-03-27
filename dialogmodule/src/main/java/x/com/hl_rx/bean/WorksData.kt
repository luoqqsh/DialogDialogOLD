package x.com.hl_rx.bean

/**
 * Desc: 赚赚相关
 * Author: Chris
 * Date: 2019/10/26
 */

/**
 * 每日任务列表
 *  already 已完成数
 *  gold 任务金币
 *  icon  任务图标链接
 *  remark 任务描述
 *  state  任务状态 0去完成 1立即领取 2已完成
 *  taskId  任务ID
 *  taskType  任务类型
 *  title  任务标题
 *  total  总数
 *  url  任务链接
 */
data class WorksDayData(
        val title: String,
        val icon: String,
        val url: String,
        val remark: String,
        val gold: Int,
        val state: Int,
        val taskType: String,
        val taskId: Int,
        val already: Int,
        val total: Int,
        //视频的锁的时间
        val lockTime:String,
        //服务器时间
        val serverTime:String

)


/**
 * 赚赚签到
 * day  连续签到天数
 * dayType 当日类型 0未签到 1已签到未翻倍 2已翻倍
 * signs 签到宝箱数据列表
 * */
data class WorksGoldList(
        var day: Int,
        var dayType: Int,
        val signs: List<WorksSignData>
)


/**
 * 赚赚签到
 * createdAt
 * dayCount
 * gold
 * id
 * isSign 是否已经签到
 * */
data class WorksSignData(
        val createdAt: String,
        val dayCount: Int,
        val gold: Int,
        val id: Int,
        var isSign: Boolean = false
)


/**
 * 任领取金币列表
 *  使用GetGoldBean就可
 * */
//data class GoldTaskBean(
//        val gold: Int,
//        val totalGold: Int,
//        val money: String
//)
