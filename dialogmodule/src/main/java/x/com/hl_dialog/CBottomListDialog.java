package x.com.hl_dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import x.com.hl_base.dialog.BaseNDialog;
import x.com.hl_base.interfaces.OnClickItemListener;
import x.com.hl_tools.hl_screen;

public class CBottomListDialog extends BaseNDialog {

    private List<String> dataList;
    private LinearLayout nDialogBottomList;
    private View nDialogBottomScroll;
    private OnClickItemListener onClickItemListener;
    private boolean isShowCancelBtn = true;
    private boolean isFullWidth = true;

    private String title = "";
    private int titlePosition = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private int titleSize = 20;
    private int titleColor = R.color.black;

    @Override
    protected void initUI(View mView, Bundle bundle) {
        nDialogBottomList = mView.findViewById(R.id.nDialogBottomList);
        View nDialogBottomLine1 = mView.findViewById(R.id.nDialogBottomLine1);
        View nDialogBottomLine2 = mView.findViewById(R.id.nDialogBottomLine2);
        nDialogBottomScroll = mView.findViewById(R.id.nDialogBottomScroll);

        View nDialogBottomCancel = mView.findViewById(R.id.nDialogBottomCancel);
        if (isShowCancelBtn) {
            nDialogBottomCancel.setVisibility(View.VISIBLE);
            nDialogBottomLine1.setVisibility(View.VISIBLE);
            nDialogBottomCancel.setOnClickListener(v -> dismiss());
        } else {
            nDialogBottomCancel.setVisibility(View.GONE);
            nDialogBottomLine2.setVisibility(View.GONE);
        }
        if (isFullWidth) {
            nDialogBottomLine1.setVisibility(View.GONE);
            nDialogBottomLine2.setVisibility(View.GONE);
            if (nDialogBottomCancel.getVisibility() == View.VISIBLE) {
                nDialogBottomCancel.setBackgroundColor(mActivitys.get().getResources().getColor(R.color.white));
            }
        }
        setOrRefreshDataList(dataList);
    }

    public void setOrRefreshDataList(List<String> dataList) {
        nDialogBottomList.removeAllViews();
        if (!TextUtils.isEmpty(title)) {
            nDialogBottomList.addView(addTitleView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (dataList.size() == 1) {
            View itemView = getListItemView(dataList.get(0), 0);
            nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (isFullWidth) {
                nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                nDialogBottomScroll.setBackground(mActivitys.get().getResources().getDrawable(R.drawable.bg_bottom_list_full_dialog));
            }
            itemView.setOnClickListener(v -> {
                if (onClickItemListener != null) {
                    if (onClickItemListener.onItemClick(dataList.get(0), 0)) {
                        dismiss();
                    }
                }
            });
        } else {
            boolean isShouldSetPadding = false;
            int height = (hl_screen.dp2px(mActivitys.get().getApplicationContext(), 50) * dataList.size());
            int maxHeight = hl_screen.getScreenHeight(mActivitys.get().getApplicationContext()) * 3 / 5;
            if (height > maxHeight) {
                isShouldSetPadding = true;
                int dp4 = hl_screen.dp2px(mActivitys.get().getApplicationContext(), 4);
                nDialogBottomScroll.setPadding(0, dp4, 0, dp4);

                ViewGroup.LayoutParams lp = nDialogBottomScroll.getLayoutParams();
                lp.height = maxHeight;
                nDialogBottomScroll.setLayoutParams(lp);
            } else {
                nDialogBottomScroll.setPadding(0, 0, 0, 0);
            }
            for (int i = 0; i < dataList.size(); i++) {
                View itemView;
                if (isShouldSetPadding) {
                    itemView = getListItemView(dataList.get(i), i);
                    nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else {
                    if (i == 0) {
                        itemView = getListItemView(dataList.get(i), i);
                        nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    } else if (i == dataList.size() - 1) {
                        itemView = getListItemView(dataList.get(i), i);
                        nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (isFullWidth) {
                            nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            nDialogBottomScroll.setBackground(mActivitys.get().getResources().getDrawable(R.drawable.bg_bottom_list_full_dialog));
                        }
                    } else {
                        itemView = getListItemView(dataList.get(i), i);
                        nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                }

                final int finalI = i;
                itemView.setOnClickListener(v -> {
                    if (onClickItemListener != null) {
                        if (onClickItemListener.onItemClick(dataList.get(finalI), finalI)) {
                            dismiss();
                        }
                    }
                });
            }
        }
    }


    public void addTitle(String text, int position, int textSize, int textColor) {
        title = text;
        titlePosition = position;
        titleSize = textSize;
        titleColor = textColor;
    }

    private View addTitleView() {
        TextView textView = new TextView(mActivitys.get());
        textView.setText(title);
        textView.setPadding(hl_screen.dp2px(mActivitys.get(), 12), 0, hl_screen.dp2px(mActivitys.get(), 12), 0);
        textView.setGravity(titlePosition);
        textView.setSingleLine();
        textView.setMaxLines(1);
        textView.setTextColor(mActivitys.get().getResources().getColor(titleColor));
        textView.setTextSize(titleSize);
        textView.setHeight(hl_screen.dp2px(mActivitys.get(), 50));
        return textView;
    }

    @Override
    public float getWidthScale() {
        return isFullWidth ? 1 : 0.9f;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    private Map<Integer, Integer> positionColorMap;

    public void setItemTextColor(int position, int positionColor) {
        if (positionColorMap == null) {
            positionColorMap = new HashMap<>();
        }
        positionColorMap.put(position, positionColor);
    }

    public void setFullWidth(boolean fullWidth) {
        isFullWidth = fullWidth;
    }

    public void setShowCancelBtn(boolean showCancelBtn) {
        isShowCancelBtn = showCancelBtn;
    }

    private View getLine() {
        TextView textView = new TextView(mActivitys.get());
        textView.setHeight(1);
        textView.setBackgroundColor(Color.parseColor("#c1c1c1"));
        return textView;
    }

    private View getListItemView(String text, int position) {
        TextView textView = new TextView(mActivitys.get());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(17);
        if (positionColorMap != null && positionColorMap.containsKey(position)) {
            textView.setTextColor(positionColorMap.get(position));
        } else {
            textView.setTextColor(Color.parseColor("#000000"));
        }
        textView.setHeight(hl_screen.dp2px(mActivitys.get(), 50));
        return textView;
    }

    public void setDataList(List<String> dataList) {
        if (dataList == null || dataList.size() == 0) {
            throw new NullPointerException("BottomListDialog create imageList should not be null or size==0");
        }
        this.dataList = dataList;
    }

    public void setDataList(String[] dataList) {
        if (dataList == null || dataList.length == 0) {
            throw new NullPointerException("BottomListDialog create imageList should not be null or size==0");
        }
        this.dataList = Arrays.asList(dataList);
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getDialogAnim() {
        return R.style.bottomMenuAnim;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ndialog_bottom_list;
    }
}
