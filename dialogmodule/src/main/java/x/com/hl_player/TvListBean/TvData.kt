package x.com.hl_player.TvListBean

class TvData(val version: Int//版本
             , val date: String//日期，格式yyyyMMddHHmmss
             , val name: String//名称
             , val links: List<TvDataList>)
