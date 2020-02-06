package x.com.hl_rx;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * runOnBackgroundThread 返回不能是 null
 * 线程切换使用
 */
public abstract class TaskManager<T> {

    private Disposable mDisposable;

    public TaskManager() {
    }

    public TaskManager<T> start() {
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(T t) {
                        runOnUIThread(t);
                        cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
        return this;
    }

    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private Observable<T> getObservable() {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final ObservableEmitter<T> observableEmitter) {
                T t = runOnBackgroundThread();
                if (t == null) {
                    // t == null 时，observableEmitter.onNext会调用失败，Error
                    new TaskDelayManager() {
                        @Override
                        public void onListen(Long index) {
                            runOnUIThread(null);
                            observableEmitter.onComplete();
                            cancel();
                        }
                    }.start();
                } else {
                    observableEmitter.onNext(t);
                    observableEmitter.onComplete();
                }
            }
        });
    }

    public abstract T runOnBackgroundThread();

    public abstract void runOnUIThread(T t);
}