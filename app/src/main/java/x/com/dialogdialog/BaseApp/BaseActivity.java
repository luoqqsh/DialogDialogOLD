package x.com.dialogdialog.BaseApp;

import android.os.Bundle;

import x.com.dialogdialog.R;
import x.com.hl_base.HLBaseActivity;

public abstract class BaseActivity extends HLBaseActivity {

    @Override
    public void initUI(Bundle savedInstanceState) {
        getLifecycle().addObserver(new MyLifeObserver8(getClass().getSimpleName()));
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.yellow;
    }
}
