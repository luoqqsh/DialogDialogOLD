package com.xing.hhplayer.common.tvbean

import java.util.*

class TvData(var version: Int//版本
             , var date: String//日期，格式yyyyMMddHHmmss
             , var name: String//名称
             , var links: ArrayList<TvDataList>)
