package x.com.hl_base.hl_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import x.com.hl_dialog.R;
import x.com.hl_view.RotateImageView;

/**
 * 新的base类
 */
public abstract class HLBaseFragment extends Fragment {
    public FragmentActivity mActivity;
    public View mView;
    private LinearLayout base_loading_fail;
    private LinearLayout base_loading_no_data;
    private LinearLayout base_loading;
    private RotateImageView loading_view;

    /**
     * 是否页面对用户可见时才执行 initUIAndData
     * 特别注意，setUserVisibleHint 貌似仅在 类似viewpager pagerAdapter中的Fragment才会调用.....
     */
    public boolean isInitDataAfterVisibleToUser() {
        return false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            if (isTitleContentOverLap()) {
                mView = inflater.inflate(R.layout.activity_base, container, false);
            } else {
                mView = inflater.inflate(R.layout.activity_base_overlap, container, false);
            }
            base_loading_fail = mView.findViewById(R.id.base_loading_fail);
            base_loading_no_data = mView.findViewById(R.id.base_loading_no_data);
            base_loading = mView.findViewById(R.id.base_loading);
            mActivity = getActivity();
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initContentView();
        initUI(savedInstanceState);
        initData();
    }

    private void initContentView() {
        ViewGroup baseContentLayout = mView.findViewById(R.id.base_content_layout);
        if (getLayoutId() != 0) {
            LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            View contentView = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
            lpContent.weight = 1;
            contentView.setLayoutParams(lpContent);
            baseContentLayout.addView(contentView);
        } else if (getLayoutView() != null) {
            baseContentLayout.addView(getLayoutView());
        }
    }

    public abstract void initUI(Bundle savedInstanceState);

    public void initData() {
    }

    public abstract int getLayoutId();

    public View getLayoutView() {
        return null;
    }

    /**
     * 基础布局，标题栏和内容是否重叠--标题栏在内容之上
     */
    public boolean isTitleContentOverLap() {
        return false;
    }

    public void setLoading() {
        if (base_loading.getChildCount() == 0) {
            View loadingFail = View.inflate(mActivity, getLoadingLayoutId(), null);
            loading_view = loadingFail.findViewById(R.id.loading_view);
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
