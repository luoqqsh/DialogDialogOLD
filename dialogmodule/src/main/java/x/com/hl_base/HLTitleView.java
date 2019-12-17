package x.com.hl_base;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import x.com.hl_dialog.R;
import x.com.hl_tools.hl_screen;
import x.com.hl_tools.hl_view;

public class HLTitleView {
    private View titleView;
    private TextView title_text, title_text2;//标题
    private View title_custom_line;//线
    private RelativeLayout mDefaultTitle;//整个标题layout
    private LinearLayout title_right_image_btn;
    private LinearLayout title_left_btn;
    private LinearLayout title_right_text_btn;
    private TextView title_right_text;
    private ImageView title_right_btn_image;
    private ImageView title_left_btn_image;
    private int titleHeight;
    private Context mContext;

    public HLTitleView(View titleView) {
        this.titleView = titleView;
        init();
    }

    private void init() {
        if (titleView != null) {
            mContext = titleView.getContext();
            mDefaultTitle = titleView.findViewById(R.id.base_default_title);
            title_text = titleView.findViewById(R.id.title_text);
            title_text2 = titleView.findViewById(R.id.title_text2);
            title_custom_line = titleView.findViewById(R.id.title_custom_line);
            //左边返回
            title_left_btn_image = titleView.findViewById(R.id.title_left_btn_image);
            title_left_btn = titleView.findViewById(R.id.title_left_btn);
            //右边文字
            title_right_text = titleView.findViewById(R.id.title_right_text);
            title_right_text_btn = titleView.findViewById(R.id.title_right_text_btn);
            //右边图
            title_right_btn_image = titleView.findViewById(R.id.title_right_btn_image);
            title_right_image_btn = titleView.findViewById(R.id.title_right_btn);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hl_view.setViewHeight(titleView, getTitleHeight(mContext) + hl_screen.getStatusBarHeight(mContext));
            }
        }
    }

    //返回
    public void setLeftButtonImage(int drawId, View.OnClickListener clickListener) {
        if (title_left_btn_image != null && drawId > 0) {
            title_left_btn_image.setImageResource(drawId);
            title_left_btn.setVisibility(View.VISIBLE);
            title_left_btn.setOnClickListener(clickListener);
        } else {
            title_left_btn.setVisibility(View.GONE);
        }
    }

    //右边
    public void setRightButtonText(String text, View.OnClickListener clickListener) {
        if (title_right_text != null) {
            title_right_text.setText(text);
        }
        if (title_right_text_btn != null) {
            title_right_text_btn.setVisibility(View.VISIBLE);
            title_right_text_btn.setOnClickListener(clickListener);
        }
    }

    public void setRightButtonImg(int imageResource, View.OnClickListener clickListener) {
        if (title_right_btn_image != null) {
            title_right_btn_image.setImageResource(imageResource);
        }
        if (title_right_image_btn != null) {
            title_right_image_btn.setVisibility(View.VISIBLE);
            title_right_image_btn.setOnClickListener(clickListener);
        }
    }

    //标题
    public void setTitleText(String titleText) {
        if (title_text != null) {
            title_text.setText(titleText);
        }
    }

    public void setTitleTextColor(int titleTextColor) {
        if (title_text != null) title_text.setTextColor(titleTextColor);
    }

    //标题2
    public void setTitle2Text(String titleText) {
        if (title_text2 != null) {
            title_text2.setText(titleText);
            title_text2.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleText2Color(int titleTextColor) {
        if (title_text2 != null) title_text2.setTextColor(titleTextColor);
    }

    //线
    public void setTitleLineColor(int lineColor) {
        if (title_custom_line != null) {
            title_custom_line.setBackgroundColor(lineColor);
            title_custom_line.setVisibility(View.VISIBLE);
        }
    }

    //背景
    public void setTitleBackGroundColor(int titleBackGroundColor) {
        if (mDefaultTitle != null) mDefaultTitle.setBackgroundColor(titleBackGroundColor);
    }

    public void setHideTitle(boolean isHideTitle) {
        titleView.setVisibility(isHideTitle ? View.GONE : View.VISIBLE);
    }

    public int getTitleHeight(Context context) {
//                getDimension 获取绝对尺寸（"像素"单位，只是float）,
//                getDimensionPixelSize是将getDimension获取的小数部分四舍五入，
//                getDimensionPixelOffset是强制转换成int，即舍去小数部分
        if (titleHeight != 0) {
            return titleHeight;
        }
        titleHeight = context.getResources().getDimensionPixelOffset(R.dimen.height_of_title);
        return titleHeight;
    }
}
