package x.com.hl_rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 子线程使用
 */
public abstract class TaskDelayBManager {

    private Disposable mDisposable;

    public TaskDelayBManager() {
    }

    public TaskDelayBManager loop(long delay, long period) {
        doWork(getLoopObservable(delay, period));
        return this;
    }

    public TaskDelayBManager loop(long period) {
        doWork(getLoopObservable(period));
        return this;
    }

    public TaskDelayBManager delay(long time) {
        doWork(getDelayObservable(time));
        return this;
    }

    private void doWork(Observable<Long> observable) {
        observable
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(Long index) {
//                        aLong 0,1,2,3,4,5,6,7,8...
                        onListen(index);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private Observable<Long> getDelayObservable(long delay) {
        if (delay < 0) delay = 0;
        return Observable.timer(delay, TimeUnit.MILLISECONDS);
    }

    private Observable<Long> getLoopObservable(long delay, long period) {
        if (delay < 0) delay = 0;
        if (period <= 0) period = 1;
        return Observable.interval(delay, period, TimeUnit.MILLISECONDS);
    }

    private Observable<Long> getLoopObservable(long period) {
        if (period <= 0) period = 1;
        return Observable.interval(period, TimeUnit.MILLISECONDS);
    }

    public abstract void onListen(Long index);
}