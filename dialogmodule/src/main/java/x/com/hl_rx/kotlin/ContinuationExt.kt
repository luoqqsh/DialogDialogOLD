package x.com.hl_rx.kotlin

import retrofit2.Call
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.*

/**
 * 协程扩展
 * @author wittyneko
 * @since 2019/5/6
 */

suspend fun <T> Call<T>.await(): T {
    return suspendCoroutine { continuation ->
        enqueue(object : RequestCallBack<T>() {
            override fun onSuccess(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null) continuation.resume(body)
                else continuation.resumeWithException(RuntimeException("response body is null"))
            }


        })
    }
}
//
//fun CoroutineScope.launchCatch(
//        context: CoroutineContext = EmptyCoroutineContext,
//        start: CoroutineStart = CoroutineStart.DEFAULT,
//        fail: (suspend CoroutineScope.(e: Throwable) -> Unit)? = null,
//        block: suspend CoroutineScope.() -> Unit) {
//    launch(context, start) {
//        try {
//            this.block()
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            withContext(Dispatchers.Main) {
//                ToastUtil.toast(R.string.app_network_error)
//            }
//            fail?.invoke(this, e)
//        }
//    }
//}
//
//fun LifecycleOwner.lifecycleCoroutine(dispatcher: CoroutineDispatcher = Dispatchers.Main) = run {
//    val job = Job()
//    val coroutineContext: CoroutineContext = dispatcher + job
//    lifecycle.addObserver(object : DefaultLifecycleObserver {
//        override fun onDestroy(owner: LifecycleOwner) {
//            job.cancel()
//            lifecycle.removeObserver(this)
//        }
//    })
//    coroutineContext
//}
//
///**
// * 协程作用域 绑定生命周期
// * @param owner 宿主Activity或Fragment
// * @param dispatcher 默认主线程 Dispatchers.Main
// */
//class LifecycleJob(owner: LifecycleOwner, dispatcher: CoroutineDispatcher = Dispatchers.Main)
//    : CoroutineScope, DefaultLifecycleObserver {
//    val job = Job()
//    override val coroutineContext: CoroutineContext = dispatcher + job
//
//    init {
//        owner.lifecycle.addObserver(this)
//    }
//
//    override fun onDestroy(owner: LifecycleOwner) {
//        job.cancel()
//        owner.lifecycle.removeObserver(this)
//    }
//}