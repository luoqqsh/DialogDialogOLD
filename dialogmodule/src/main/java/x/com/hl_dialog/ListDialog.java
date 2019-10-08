package x.com.hl_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import x.com.hl_log.ViseLog;
import x.com.hl_tools.model.PermissionModel;

public class ListDialog extends Dialog implements OnClickListener, OnItemClickListener {
    private SelectDialogListener mListener;
    private SelectDialogCancelListener mCancelListener;

    private Activity mActivity;
    private List<PermissionModel> mName;
    private String mTitle;
    private boolean mUseCustomColor = false;
    private int mFirstItemColor;
    private int mOtherItemColor;
    private DialogAdapter dialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.dialog_list, null);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        // 设置显示动画
        if (window != null) {
            window.setWindowAnimations(R.style.main_menu_animStyle);
//            window.setGravity(Gravity.CENTER);
//            WindowManager.LayoutParams wl = window.getAttributes();
//            wl.x = 0;
//            wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
//            // 以下这两句是为了保证按钮可以水平满屏
//            wl.width = LayoutParams.MATCH_PARENT;
//            wl.height = LayoutParams.WRAP_CONTENT;
//            // 设置显示位置
//            onWindowAttributesChanged(wl);
        }
        initViews();
    }

    private void initViews() {
        dialogAdapter = new DialogAdapter(mName);
        ListView dialogList = findViewById(R.id.dialog_list);
        Button mMBtnCancel = findViewById(R.id.mBtn_Cancel);
        TextView mTvTitle = findViewById(R.id.mTv_Title);
        dialogList.setOnItemClickListener(this);
        dialogList.setAdapter(dialogAdapter);
        mMBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelListener != null) {
                    mCancelListener.onCancelClick(v);
                }
                dismiss();
            }
        });

        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mTitle);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置item监听事件
     */
    public interface SelectDialogListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    /**
     * 取消事件监听接口
     */
    public interface SelectDialogCancelListener {
        void onCancelClick(View v);
    }

    /**
     * @param activity 调用弹出菜单的activity
     * @param theme    主题
     * @param names    菜单项名称
     */
    public ListDialog(Activity activity, int theme, List<PermissionModel> names) {
        super(activity, theme);
        mActivity = activity;
        this.mName = names;
        setCanceledOnTouchOutside(true);
    }

    /**
     * @param activity       调用弹出菜单的activity
     * @param theme          主题
     * @param cancelListener 取消事件
     * @param names          菜单项名称
     */
    public ListDialog(Activity activity, int theme, SelectDialogCancelListener cancelListener, List<PermissionModel> names) {
        super(activity, theme);
        mActivity = activity;
        mCancelListener = cancelListener;
        this.mName = names;
        // 设置是否点击外围不解散
        setCanceledOnTouchOutside(false);
    }

    /**
     * @param activity 调用弹出菜单的activity
     * @param theme    主题
     * @param names    菜单项名称
     * @param title    菜单标题文字
     */
    public ListDialog(Activity activity, int theme, List<PermissionModel> names, String title) {
        super(activity, theme);
        mActivity = activity;
        this.mName = names;
        mTitle = title;
        // 设置是否点击外围可解散
        setCanceledOnTouchOutside(true);
    }

    /**
     * @param activity       调用弹出菜单的activity
     * @param theme          主题
     * @param cancelListener 取消点击事件
     * @param names          数据
     * @param title          标题
     */
    public ListDialog(Activity activity, int theme, SelectDialogCancelListener cancelListener, List<PermissionModel> names, String title) {
        super(activity, theme);
        mActivity = activity;
        mCancelListener = cancelListener;
        this.mName = names;
        mTitle = title;
        // 设置是否点击外围可解散
        setCanceledOnTouchOutside(true);
    }

    public void refresh() {
        if (dialogAdapter != null) {
            dialogAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param listener 菜单项单击事件
     */
    public void setItemListener(SelectDialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onItemClick(parent, view, position, id);
        } else {
            ViseLog.w("未设置item点击事件,设置方法dialog.setItemListener(new ListDialog.SelectDialogListener() {...});");
        }
//        dismiss();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private class DialogAdapter extends BaseAdapter {
        private List<PermissionModel> mStrings;
        private ViewHolder viewholder;
        private LayoutInflater layoutInflater;

        DialogAdapter(List<PermissionModel> strings) {
            this.mStrings = strings;
            this.layoutInflater = mActivity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                viewholder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.dialog_listitem, null);
                viewholder.dialogItemButton = convertView.findViewById(R.id.dialog_item_bt);
                viewholder.dialog_item_apply = convertView.findViewById(R.id.dialog_item_apply);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.dialogItemButton.setText(mStrings.get(position).getName());
            if (!mUseCustomColor) {
                mFirstItemColor = mActivity.getResources().getColor(R.color.gray);
                mOtherItemColor = mActivity.getResources().getColor(R.color.gray);
            }
            if (1 == mStrings.size()) {
                viewholder.dialogItemButton.setTextColor(mFirstItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.shape_dialog_item_bg_only);
            } else if (position == 0) {
                viewholder.dialogItemButton.setTextColor(mFirstItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_top);
            } else if (position == mStrings.size() - 1) {
                viewholder.dialogItemButton.setTextColor(mOtherItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_buttom);
            } else {
                viewholder.dialogItemButton.setTextColor(mOtherItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_center);
            }
            viewholder.dialog_item_apply.setText(mStrings.get(position).isIsapply() ? "已授权" : "去设置");
            return convertView;
        }
    }

    public static class ViewHolder {
        TextView dialogItemButton;
        TextView dialog_item_apply;
    }

    /**
     * 设置列表项的文本颜色
     */
    public void setItemColor(int firstItemColor, int otherItemColor) {
        mFirstItemColor = firstItemColor;
        mOtherItemColor = otherItemColor;
        mUseCustomColor = true;
    }
}
