package x.com.hl_rx.task;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class TaskSimpleManager {

    private Disposable mDisposable;

    public TaskSimpleManager() {
    }

    public TaskSimpleManager start() {
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(String s) {
                        runOnUIThread();
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

    private Observable<String> getObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) {
                runOnBackgroundThread();
                observableEmitter.onNext("");
                observableEmitter.onComplete();
            }
        });
    }

    public abstract void runOnBackgroundThread();

    public abstract void runOnUIThread();
}