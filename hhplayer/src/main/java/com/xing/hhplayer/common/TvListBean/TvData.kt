package com.xing.hhplayer.common.TvListBean

import java.util.*

class TvData(val version: Int//版本
             , val date: String//日期，格式yyyyMMddHHmmss
             , val name: String//名称
             , val links: ArrayList<TvDataList>)
