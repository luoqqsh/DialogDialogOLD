package x.com.hl_base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import x.com.hl_dialog.R;
import x.com.hl_view.SlideLayout;

public abstract class HLSlideActivity extends AppCompatActivity {
    private SlideLayout swipeBackLayout;

    protected boolean isOpenSlideBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isOpenSlideBack()) {
            swipeBackLayout = new SlideLayout(this);
            swipeBackLayout.attachToActivity(this);
        }
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    public void setDrawerScrollEnable(boolean isEnable) {
        if (swipeBackLayout != null)
            swipeBackLayout.setIsCanScroll(isEnable);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (swipeBackLayout != null)
            overridePendingTransition(R.anim.slide_right_in, R.anim.no_anim);
    }

    @Override
    public void finish() {
        super.finish();
        if (swipeBackLayout != null)
            overridePendingTransition(0, R.anim.slide_right_out);
    }
}
