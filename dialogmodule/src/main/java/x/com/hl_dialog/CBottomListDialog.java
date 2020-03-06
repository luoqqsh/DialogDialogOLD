package x.com.hl_dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import x.com.hl_base.hl_dialog.BaseNDialog;
import x.com.hl_base.hl_interface.OnClickItemListener;
import x.com.hl_tools.hl_screen;

public class CBottomListDialog extends BaseNDialog {

    private List<String> dataList;
    private LinearLayout nDialogBottomList;
    private View nDialogBottomScroll;
    private OnClickItemListener onClickItemListener;

    @Override
    protected void initUI(View layout, Bundle bundle) {
        nDialogBottomList = layout.findViewById(R.id.nDialogBottomList);
        nDialogBottomScroll = layout.findViewById(R.id.nDialogBottomScroll);
        View nDialogBottomLine = layout.findViewById(R.id.nDialogBottomLine);
        layout.findViewById(R.id.nDialogBottomCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setOrRefreshDataList(dataList);
        ViewGroup.LayoutParams lp = nDialogBottomLine.getLayoutParams();
        lp.height = (int) hl_screen.dp2px(mActivitys.get().getApplicationContext(), 12);
        nDialogBottomLine.setLayoutParams(lp);
    }

    public void setOrRefreshDataList(List<String> dataList) {
        nDialogBottomList.removeAllViews();
        if (dataList.size() == 1) {
            View itemView = getListItemView(dataList.get(0), ItemType.Single, 0);
            nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemListener != null) {
                        if (onClickItemListener.onItemClick(dataList.get(0), 0)) {
                            dismiss();
                        }
                    }
                }
            });
        } else {
            boolean isShouldSetPadding = false;
            int height = (int) (hl_screen.dp2px(mActivitys.get().getApplicationContext(), 50) * dataList.size());
            int maxHeight = hl_screen.getScreenHeight(mActivitys.get().getApplicationContext()) * 3 / 5;
            if (height > maxHeight) {
                isShouldSetPadding = true;
                int dp4 = (int) hl_screen.dp2px(mActivitys.get().getApplicationContext(), 4);
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
                    itemView = getListItemView(dataList.get(i), ItemType.Center, i);
                    nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else {
                    if (i == 0) {
                        itemView = getListItemView(dataList.get(i), ItemType.Top, i);
                        nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    } else if (i == dataList.size() - 1) {
                        itemView = getListItemView(dataList.get(i), ItemType.Bottom, i);
                        nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    } else {
                        itemView = getListItemView(dataList.get(i), ItemType.Center, i);
                        nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                }

                final int finalI = i;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickItemListener != null) {
                            if (onClickItemListener.onItemClick(dataList.get(finalI), finalI)) {
                                dismiss();
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public float getWidthScale() {
        return 0.9f;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    private Map<Integer, Integer> positionColorMap;

    /**
     * 必须在create前设置
     */
    public void setItemTextColor(int position, int positionColor) {
        if (positionColorMap == null) {
            positionColorMap = new HashMap<>();
        }
        positionColorMap.put(position, positionColor);
    }

    private enum ItemType {
        Top, Bottom, Center, Single
    }

    private View getLine() {
        TextView textView = new TextView(mActivitys.get());
        textView.setHeight(1);
        textView.setBackgroundColor(Color.parseColor("#c1c1c1"));
        return textView;
    }

    private View getListItemView(String text, ItemType type, int position) {
        TextView textView = new TextView(mActivitys.get());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        if (positionColorMap != null && positionColorMap.containsKey(position)) {
            textView.setTextColor(positionColorMap.get(position));
        } else {
            textView.setTextColor(Color.parseColor("#2b84fd"));
        }
        textView.setHeight((int) hl_screen.dp2px(mActivitys.get(), 50));
        return textView;
    }

    public void create(List<String> dataList) {
        if (dataList == null || dataList.size() == 0) {
            throw new NullPointerException("BottomListDialog create imageList should not be null or size==0");
        }
        this.dataList = dataList;
    }

    public void create(String[] dataList) {
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
