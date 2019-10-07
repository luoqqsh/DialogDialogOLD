package x.com.dialogdialog.BaseApp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 生命周期感知
 */
public class MyLifeObserver implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onStart(){
        
    }
}
