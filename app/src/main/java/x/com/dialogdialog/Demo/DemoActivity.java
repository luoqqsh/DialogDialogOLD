package x.com.dialogdialog.Demo;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.dialogdialog.R;

public class DemoActivity extends BaseActivity {
    private LinearLayout ll_add;
    private ViewPager vp_add;
    private ViewPager2 vp_add2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        ll_add = findViewById(R.id.ll_add);
        vp_add = findViewById(R.id.vp_add);
        vp_add2 = findViewById(R.id.vp_add2);
    }
}
