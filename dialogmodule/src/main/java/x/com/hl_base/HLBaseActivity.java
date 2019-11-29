package x.com.hl_base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_leak;

/**
 * 沉浸式base
 */
public abstract class HLBaseActivity extends AppCompatActivity {
    public FragmentActivity mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hl_device.INSTANCE.isHuaWeiPhone()) {
            hl_leak.fixHuaWeiLeak(this);
        }
    }
}
