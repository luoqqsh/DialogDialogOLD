package x.com.hl_base;

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

import x.com.hl_dialog.R;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_leak;
import x.com.hl_tools.hl_screen;
import x.com.hl_view.RotateImageView;

public abstract class HLBaseActivity extends KBaseActivity {
    public FragmentActivity mActivity;
    public HLTitleView mTitle;
    private LinearLayout base_loading_fail;
    private LinearLayout base_loading_no_data;
    private LinearLayout base_loading;
    private RotateImageView loading_view;

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
        ViewGroup base_layout = findViewById(R.id.base_content_layout);
        int backgroundColor = getBackgroundColor();
        if (backgroundColor != 0) {
            base_layout.setBackgroundColor(backgroundColor);
        } else if (getBackgroundColorResource() != 0) {
            base_layout.setBackgroundResource(getBackgroundColorResource());
        }
        initTitleView();//加载标题
        initContentView();//加载内容
        //加载状态
        base_loading_fail = findViewById(R.id.base_loading_fail);
        base_loading_no_data = findViewById(R.id.base_loading_no_data);
        base_loading = findViewById(R.id.base_loading);
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
            ViewGroup baseTitleLayout = findViewById(R.id.base_title_layout);
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
        ViewGroup baseContentLayout = findViewById(R.id.base_content_layout);
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
    }

    public void showBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
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
    }

    public void setLoading() {
        if (base_loading.getChildCount() == 0) {
            View loadingFail = View.inflate(mActivity, getLoadingLayoutId(), null);
            loading_view = loadingFail.findViewById(R.id.loadingView);
            base_loading.addView(loadingFail, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        base_loading_fail.setVisibility(View.GONE);
        base_loading.setVisibility(View.VISIBLE);
        base_loading_no_data.setVisibility(View.GONE);
        loading_view.start();
    }

    public void setLoadingFail() {
        if (base_loading_fail.getChildCount() == 0) {
            View loadingFail = View.inflate(mActivity, getLoadingFailLayoutId(), null);
            base_loading_fail.addView(loadingFail, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        base_loading_fail.setVisibility(View.VISIBLE);
        base_loading.setVisibility(View.GONE);
        base_loading_no_data.setVisibility(View.GONE);
        if (loading_view != null) {
            loading_view.stop();
        }
    }

    public void setLoadingEnd() {
        base_loading_fail.setVisibility(View.GONE);
        base_loading.setVisibility(View.GONE);
        base_loading_no_data.setVisibility(View.GONE);
        if (loading_view != null) {
            loading_view.stop();
        }
    }

    public void setLoadingNoData() {
        if (base_loading_no_data.getChildCount() == 0) {
            View loadingFail = View.inflate(mActivity, getLoadingNoDataId(), null);
            base_loading_no_data.addView(loadingFail, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        base_loading_fail.setVisibility(View.GONE);
        base_loading.setVisibility(View.GONE);
        base_loading_no_data.setVisibility(View.VISIBLE);
        if (loading_view != null) {
            loading_view.stop();
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
}