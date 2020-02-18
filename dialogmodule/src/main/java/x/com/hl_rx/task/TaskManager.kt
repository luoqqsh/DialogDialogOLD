package x.com.hl_rx.task

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * runOnBackgroundThread 返回不能是 null
 * 线程切换使用
 */
abstract class TaskManager<T> {
    private var mDisposable: Disposable? = null
    fun start(): TaskManager<T> {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<T> {
                    override fun onSubscribe(disposable: Disposable) {
                        mDisposable = disposable
                    }

                    override fun onNext(t: T) {
                        runOnUIThread(t)
                        cancel()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        cancel()
                    }

                    override fun onComplete() {
                        cancel()
                    }
                })
        return this
    }

    fun cancel() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
            mDisposable = null
        }
    }

    // t == null 时，observableEmitter.onNext会调用失败，Error
    private val observable: Observable<T>
        private get() = Observable.create { observableEmitter ->
            val t: T? = runOnBackgroundThread()
            if (t == null) { // t == null 时，observableEmitter.onNext会调用失败，Error
                object : TaskDelayManager() {
                    override fun onListen(index: Long) {
                        runOnUIThread(null)
                        observableEmitter.onComplete()
                        cancel()
                    }
                }.start()
            } else {
                observableEmitter.onNext(t)
                observableEmitter.onComplete()
            }
        }

    abstract fun runOnBackgroundThread(): T
    abstract fun runOnUIThread(t: T?)
}