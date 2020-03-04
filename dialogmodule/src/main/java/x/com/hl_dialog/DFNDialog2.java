package x.com.hl_dialog;


import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import x.com.hl_base.hl_fragment.BaseDialog;

/**
 * @author : Created by ChenJiaLiang on 2016/8/24.
 * Email : 576507648@qq.com
 */
public class DFNDialog2 extends BaseDialog {

    private TextView mTitleTv;
    private TextView mTextTv;
    private TextView mBtnLeft;
    private TextView mBtnRight;
    private View titleLayout;
    private ViewGroup mContentLayout;
    private int rightTextColor;
    private int leftTextColor;
    private View customContentLayout;
    private CharSequence textCs;
    private int textGravity;
    private CharSequence titleCs;
    private CharSequence btnLeftText;
    private CharSequence btnRightText;
    private MovementMethod textMovement;

    @Override
    protected void initUI(View layout) {
        titleLayout = layout.findViewById(R.id.de_title_layout);
        mTitleTv = layout.findViewById(R.id.de_title);
        mTextTv = layout.findViewById(R.id.de_text);
        mBtnLeft = layout.findViewById(R.id.de_btn_left);
        mBtnRight = layout.findViewById(R.id.de_btn_right);
        mContentLayout = layout.findViewById(R.id.de_content_layout);


        if (leftTextColor != 0) mBtnLeft.setTextColor(leftTextColor);
        if (rightTextColor != 0) mBtnRight.setTextColor(rightTextColor);
        if (customContentLayout != null) {
            mContentLayout.removeAllViews();
            mContentLayout.addView(customContentLayout);
        }
        if (textCs != null) {
            mTextTv.setText(textCs);
            if (textGravity != 0) {
                mTextTv.setGravity(textGravity);
            } else {
                if (textCs != null && textCs.length() > 20) {
                    mTextTv.setGravity(Gravity.START);
                } else {
                    mTextTv.setGravity(Gravity.CENTER);
                }
            }
        }
        mTextTv.setMovementMethod(textMovement);

        if (titleCs != null) {
            mTitleTv.setText(titleCs);
            titleLayout.setVisibility(TextUtils.isEmpty(titleCs) ? View.GONE : View.VISIBLE);
        } else {
            titleLayout.setVisibility(View.GONE);
        }
        if (btnLeftText != null) {
            mBtnLeft.setText(btnLeftText);
        }
        if (btnRightText != null) {
            mBtnRight.setText(btnRightText);
        }

    }


    public DFNDialog2 setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
        if (mBtnLeft != null) mBtnLeft.setTextColor(leftTextColor);
        return this;
    }

    public DFNDialog2 setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        if (mBtnRight != null) mBtnRight.setTextColor(rightTextColor);
        return this;
    }

    public DFNDialog2 setContentLayout(View contentLayout) {
        this.customContentLayout = contentLayout;
        if (mContentLayout != null) {
            mContentLayout.removeAllViews();
            mContentLayout.addView(contentLayout);
        }
        return this;
    }

    public DFNDialog2 setText(CharSequence text) {
        return setText(text, null);
    }

    public DFNDialog2 setText(CharSequence text, MovementMethod movement) {
        this.textCs = text;
        this.textMovement = movement;
        if (mTextTv != null) {
            mTextTv.setText(text);
            if (text != null && text.length() > 20) {
                mTextTv.setGravity(Gravity.START);
            } else {
                mTextTv.setGravity(Gravity.CENTER);
            }
            mTextTv.setMovementMethod(movement);
        }
        return this;
    }

    public DFNDialog2 setText(CharSequence text, int gravity) {
        this.textCs = text;
        this.textGravity = gravity;
        if (mTextTv != null) {
            mTextTv.setText(text);
            mTextTv.setGravity(gravity);
        }
        return this;
    }

    public DFNDialog2 setTitle(CharSequence text) {
        this.titleCs = text;
        if (mTitleTv != null) {
            mTitleTv.setText(text);
            titleLayout.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        }
        return this;
    }

    public DFNDialog2 setBtn(CharSequence leftText, CharSequence rightText) {
        this.btnLeftText = leftText;
        this.btnRightText = rightText;
        if (mBtnLeft != null) {
            mBtnLeft.setText(leftText);
        }
        if (mBtnRight != null) {
            mBtnRight.setText(rightText);
        }
        return this;
    }

    public DFNDialog2 setBtnLeft(CharSequence leftText) {
        this.btnLeftText = leftText;
        if (mBtnLeft != null) {
            mBtnLeft.setText(leftText);
        }
        return this;
    }

    public DFNDialog2 setBtnRight(CharSequence rightText) {
        this.btnRightText = rightText;
        if (mBtnRight != null) {
            mBtnRight.setText(rightText);
        }
        return this;
    }

    public int getLayoutId() {
        return R.layout.dialog_ensure;
    }


    public void show(FragmentActivity mActivity) {
        showDialog(mActivity);
    }


}
