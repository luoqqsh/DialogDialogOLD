package x.com.dialogdialog;

import android.os.Bundle;

import x.com.dialogdialog.BaseApp.BaseActivity;

public class InformationActivity extends BaseActivity {
    @Override
    public void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.green;
    }
}
