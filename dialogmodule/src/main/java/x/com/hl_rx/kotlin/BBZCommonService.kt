package x.com.hl_rx.kotlin

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query
import x.com.hl_rx.bean.HomeDataBean
import x.com.hl_rx.bean.HttpResult

interface BBZCommonService {
    @POST("api/homePage/newHomePageResult")
    fun getHomeData(@Query("device") device: String, @Query("userId") userId: String, @Query("steps") steps: String): Call<HttpResult<HomeDataBean>>

}