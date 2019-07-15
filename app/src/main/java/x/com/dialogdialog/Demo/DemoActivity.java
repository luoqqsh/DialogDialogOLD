package x.com.dialogdialog.Demo;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.dialogdialog.R;

public class DemoActivity extends BaseActivity {
    private LinearLayout ll_add;
    private ViewPager vp_add;
    private ViewPager2 vp_add2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ll_add = findViewById(R.id.ll_add);
        vp_add = findViewById(R.id.vp_add);
        vp_add2 = findViewById(R.id.vp_add2);
    }
}
