package x.com.hl_base.hl_activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.Method;

import x.com.hl_base.MyLifeObserver8;
import x.com.hl_dialog.R;
import x.com.hl_livedatabus.LiveEventBus;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_leak;
import x.com.hl_tools.hl_orientation2;
import x.com.hl_tools.hl_screen;
import x.com.hl_view.RotateImageView;

public abstract class HLBaseActivity extends KBaseActivity {
    public FragmentActivity mActivity;
    public HLTitleView mTitle;
    private LinearLayout baseLoadingLayout;
    private LinearLayout baseNoData;
    private LinearLayout baseLoading;
    private RotateImageView loadingView;
    private hl_orientation2 ori;
    private ViewGroup baseContentLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        doBeforeCreate();
        super.onCreate(savedInstanceState);
        mActivity = this;
        //在dialog对话框添加一个监听，如果Activity关掉，则调用对话框dismiss方法，是否有必要？
        if (isOverlap()) {
            setContentView(R.layout.activity_base_overlap);
        } else {
            setContentView(R.layout.activity_base);
        }
        baseContentLayout = findViewById(R.id.baseContentLayout);
        int backgroundColor = getBackgroundColor();
        if (backgroundColor != 0) {
            baseContentLayout.setBackgroundColor(backgroundColor);
        } else if (getBackgroundColorResource() != 0) {
            baseContentLayout.setBackgroundResource(getBackgroundColorResource());
        }
        initTitleView();//加载标题
        initContentView();//加载内容
        //加载状态
        baseLoadingLayout = findViewById(R.id.baseLoadingLayout);
        baseNoData = findViewById(R.id.baseNoData);
        baseLoading = findViewById(R.id.baseLoading);
        //沉浸式
        ImmersionBar immersionBar = ImmersionBar.with(mActivity);
        immersionBar.fitsSystemWindows(false);
        immersionBar.statusBarDarkFont(isTitleBarTextDark());
//        immersionBar.transparentBar();//加了这个，导航栏会和布局内容重叠
        onStatusBarMoreSetting(immersionBar);
        immersionBar.init();
        if (!isShowStatusBar()) {
            hideNavigationBar();
        }
        getLifecycle().addObserver(new MyLifeObserver8(getClass().getSimpleName()));
        initUI(savedInstanceState);
        initData();
    }

    private void initTitleView() {
        if (isShowTitle()) {
            ViewGroup baseTitleLayout = findViewById(R.id.baseTitleLayout);
            if (getTitleLayoutId() == 0) {//没有加载默认的
                View baseTitleLayoutInside = View.inflate(mActivity, R.layout.activity_view_title, null);
                baseTitleLayout.addView(baseTitleLayoutInside, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mTitle = new HLTitleView(baseTitleLayoutInside);
            } else {
                View baseTitleLayoutInside = View.inflate(mActivity, getTitleLayoutId(), null);
                baseTitleLayout.addView(baseTitleLayoutInside, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
    }

    private void initContentView() {
        baseContentLayout = findViewById(R.id.baseContentLayout);
        if (getLayoutId() != 0) {
            View contentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
            LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lpContent.weight = 1;
            contentView.setLayoutParams(lpContent);
            baseContentLayout.addView(contentView);
        } else if (getLayoutView() != null) {
            baseContentLayout.addView(getLayoutView());
        }
        if (!isShowTitle() && isShowStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                baseContentLayout.setPadding(0, hl_screen.getStatusBarHeight(mActivity), 0, 0);
            }
        }
    }

    protected void onStatusBarMoreSetting(ImmersionBar immersionBar) {

    }

    public void hideStatusBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }


    public void hideBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
        if (!isShowTitle()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                baseContentLayout.setPadding(0, 0, 0, 0);
            }
        }
    }

    public void showBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
        if (!isShowTitle()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                baseContentLayout.setPadding(0, hl_screen.getStatusBarHeight(mActivity), 0, 0);
            }
        }
    }

    public void hideNavigationBar() {
        if (ImmersionBar.hasNavigationBar(this)) {
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
        } else {
//            Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
        }
    }

    public void showNavigationBar() {
        if (ImmersionBar.hasNavigationBar(this)) {
            BarParams barParams = ImmersionBar.with(this).getBarParams();
            if (barParams.fullScreen) {
                ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.colorPrimary).init();
            } else {
                ImmersionBar.with(this).fullScreen(true).transparentNavigationBar().init();
            }
        } else {
//            Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isShowStatusBar() {
        return true;
    }

    public boolean isShowTitle() {
        return true;
    }

    public boolean isTitleBarTextDark() {
        return false;
    }

    public boolean isOverlap() {
        return false;
    }

    public abstract void initUI(Bundle savedInstanceState);

    public void initData() {
    }

    public int getLayoutId() {
        return 0;
    }

    public View getLayoutView() {
        return null;
    }

    public int getTitleLayoutId() {
        return 0;
    }

    protected int getBackgroundColorResource() {
        return 0;
    }

    protected int getBackgroundColor() {
        return 0;
    }

    public void doBeforeCreate() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hl_device.INSTANCE.isHuaWeiPhone()) {
            hl_leak.fixHuaWeiLeak(this);
        }
        if (ori != null) {
            ori.stopWatch();
        }
    }

    public void setLoading() {
        if (baseLoading.getChildCount() == 0) {
            View loadingFail = View.inflate(mActivity, getLoadingLayoutId(), null);
            loadingView = loadingFail.findViewById(R.id.loadingView);
            baseLoading.addView(loadingFail, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        baseLoadingLayout.setVisibility(View.GONE);
        baseLoading.setVisibility(View.VISIBLE);
        baseNoData.setVisibility(View.GONE);
        loadingView.start();
    }

    public void setLoadingFail() {
        if (baseLoadingLayout.getChildCount() == 0) {
            View loadingFail = View.inflate(mActivity, getLoadingFailLayoutId(), null);
            baseLoadingLayout.addView(loadingFail, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        baseLoadingLayout.setVisibility(View.VISIBLE);
        baseLoading.setVisibility(View.GONE);
        baseNoData.setVisibility(View.GONE);
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    public void setLoadingEnd() {
        baseLoadingLayout.setVisibility(View.GONE);
        baseLoading.setVisibility(View.GONE);
        baseNoData.setVisibility(View.GONE);
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    public void setLoadingNoData() {
        if (baseNoData.getChildCount() == 0) {
            View loadingFail = View.inflate(mActivity, getLoadingNoDataId(), null);
            baseNoData.addView(loadingFail, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        baseLoadingLayout.setVisibility(View.GONE);
        baseLoading.setVisibility(View.GONE);
        baseNoData.setVisibility(View.VISIBLE);
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    public int getLoadingLayoutId() {
        return R.layout.base_loading;
    }

    public int getLoadingFailLayoutId() {
        return R.layout.base_loading_fail;
    }

    public int getLoadingNoDataId() {
        return R.layout.base_loading_nodata;
    }

    //该方法是将activity设置为不透明，需求冲突的话，需另寻方案
    public void fixRotateBug26() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            try {
                Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
                method.setAccessible(true);
                method.invoke(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 启动旋转监听
     */
    public void startOriListener() {
        ori = new hl_orientation2(mActivity);
        ori.setOnOrientationListener(new hl_orientation2.OnOrientationListener() {
            @Override
            public void changedToLandScape(boolean fromPort) {
                if (fromPort) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    changeToLand();
                    LiveEventBus.get("aaa").post("SCREEN_ORIENTATION_SENSOR_LANDSCAPE");
                }
            }

            @Override
            public void changedToPortrait(boolean fromLand) {
                if (fromLand) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    changeToPort();
                    LiveEventBus.get("aaa").post("SCREEN_ORIENTATION_SENSOR_PORTRAIT");
                }
            }
        });
        ori.startWatch();
    }

    public void changeToLand() {

    }

    public void changeToPort() {

    }
}