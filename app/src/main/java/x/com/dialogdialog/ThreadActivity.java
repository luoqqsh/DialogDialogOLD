package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import x.com.base.activity.HKBaseActivity;
import x.com.base.interfaces.OnSimpleListener;
import x.com.base.interfaces.OnTSimpleListener;
import x.com.fliepick.bean.FileBean;
import x.com.liveEventBus.LiveEventBus;
import x.com.log.ViseLog;
import x.com.rxHttp.task.TaskDelayBManager;

public class ThreadActivity extends HKBaseActivity {
    @Override
    protected int getBackgroundColorResource() {
        return R.color.white;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_thread;
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setLeftButtonImage(R.mipmap.hl_back_black, v -> finish());
        mTitle.setTitleText("线程协程");
    }

    public void taskManager(View view) {
        KTaskManager(new OnTSimpleListener<Object>() {
            @Override
            public Object onBackGround() {
                ViseLog.d("asdas");
                return new FileBean();
            }

            @Override
            public void onUI(Object o) {
                ViseLog.d(o);
            }
        });
        KTaskManagerB(new OnSimpleListener() {
            @Override
            public void onListen() {
                ViseLog.d("afafafaf");
            }
        });
    }

    public void liveEventBus(View view) {
        //接收
        LiveEventBus.get("KEY_TEST_OBSERVE", Integer.class).observe(mActivity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

            }
        });

        //发送
        LiveEventBus.get("KEY_TEST_OBSERVE").post("fff");
    }

    public void TaskDelayBManager(View view) {
        TaskDelayBManager task = new TaskDelayBManager() {
            @Override
            public void onListen(Long index) {
            }
        }.delay(1);
    }
}
