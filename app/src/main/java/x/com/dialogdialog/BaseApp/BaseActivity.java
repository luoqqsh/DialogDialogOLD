package x.com.dialogdialog.BaseApp;

import android.os.Bundle;

import x.com.dialogdialog.R;
import x.com.hl_base.HLBaseActivity;

public abstract class BaseActivity extends HLBaseActivity {

    @Override
    public void initUI(Bundle savedInstanceState) {
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.yellow;
    }

    @Override
    protected boolean isOpenSlideBack() {
        return false;
    }

    @Override
    public boolean isShowTitle() {
        return false;
    }

    @Override
    public boolean isTitleBarTextDark() {
        return true;
    }

}
