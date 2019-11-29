package x.com.dialogdialog.BaseApp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import x.com.hl_base.HLBaseActivity;
import x.com.hl_tools.hl_statusbar;

public abstract class BaseActivity extends HLBaseActivity {
    public FragmentActivity mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

//        getLifecycle().addObserver(new MyLifeObserver7());
        getLifecycle().addObserver(new MyLifeObserver8(getClass().getSimpleName()));

        hl_statusbar.setStatusBarColor(this, Color.GREEN);
        hl_statusbar.setNavigationBarColor(this, Color.RED);
//        hl_statusbar.setRootViewFitsSystemWindows(this, false);
        hl_statusbar.setStatusBarDarkTheme(this, true);
    }
}
