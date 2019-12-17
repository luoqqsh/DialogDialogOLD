package x.com.hl_tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;


public class hl_view {
    private static final long DELAY = 500L;
    private static long lastClickTime = 0;

    //防止手抖
    public static boolean isNotFastClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > DELAY) {
            lastClickTime = currentTime;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 测量view的宽高
     *
     * @param view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 获得精确的宽度、高度
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.width,
                View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.height,
                View.MeasureSpec.EXACTLY);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算ListView的高度，父控件为LinearLayout
     *
     * @param lv
     */
    public static void measureLinearLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv
                    .getLayoutParams();
            params.height = totalHeight
                    + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * 计算ListView的高度，父控件为FrameLayout
     *
     * @param lv
     */
    public static void measureFrameLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lv
                    .getLayoutParams();
            params.height = totalHeight
                    + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * scrollview嵌套listview 计算高度
     * listView嵌套listView,设置子listView的高度,子listView的item必须是LinearLayout,因为其他的没重写onMeasure()方法
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 20;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    /**
     * scrollview嵌套expandablelistview 计算高度
     */
    public static void setExpandableListviewHeightBasedOnChildren(ExpandableListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 20;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * scrollview嵌套GridView 计算高度
     */
    public static void setGridViewHeightBasedOnChildren(GridView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        int numColumns = listView.getNumColumns();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 20;

        if (listAdapter.getCount() % numColumns == 0) {
            for (int i = 0, len = listAdapter.getCount() / numColumns; i < len; i++) { // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        } else {
            for (int i = 0, len = listAdapter.getCount() / numColumns; i <= len; i++) { // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getVerticalSpacing() * (listAdapter.getCount() / numColumns - 1));
        //	    listView.getVerticalSpacing()获取子项间分隔符占用的高度
        listView.setLayoutParams(params);
    }

    @SuppressLint("RestrictedApi")
    public static void showActionBar(Context context) {
        ActionBar ab = hl_context.getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.show();
        }
        hl_context.scanForActivity(context)
                .getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("RestrictedApi")
    public static void hideActionBar(Context context) {
        ActionBar ab = hl_context.getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.hide();
        }
        hl_context.scanForActivity(context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置view高度
     *
     * @param view   view
     * @param height 高度
     */
    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }
}
