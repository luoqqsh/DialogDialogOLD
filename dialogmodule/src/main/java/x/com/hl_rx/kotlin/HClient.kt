package x.com.hl_rx.kotlin

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.Proxy
import java.util.concurrent.TimeUnit

class HClient() {
    // 网络请求超时时间值(s)
    private val DEFAULT_TIMEOUT: Long = 15

    // 请求地址
    private val BASE_URL: String = ""

    private val retrofit: Retrofit? = null

    init {
        // 创建一个OkHttpClient
        val builder = OkHttpClient.Builder()
        builder.proxy(Proxy.NO_PROXY)//不可被抓包
        // 设置网络请求超时时间
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        //信任所有证书,不安全有风险
        val sslParams1: HttpSSLUtils.SSLParams = HttpSSLUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
        // 失败后尝试重新请求
        builder.retryOnConnectionFailure(true)
        //        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addNetworkInterceptor(interceptor)
        // 添加签名参数
//        builder.addInterceptor(new BasicParamsInject().getInterceptor());
        // 添加签名参数
//        builder.addInterceptor(new BasicParamsInject().getInterceptor());
//        builder.addInterceptor(getInterceptor())
    }
}