package x.com.hl_dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_base.dialog.BaseNDialog;
import x.com.hl_bean.CSelectType;
import x.com.hl_model.MediaFileBean;

public class CSelectDialog extends BaseNDialog {
    protected List<MediaFileBean> mDataList = new ArrayList<>();//返回数据
    protected List<MediaFileBean> initDataList = new ArrayList<>();//已选择数据
    private @CSelectType
    int chooseType;//选择类别 0.文件 1.图片 2.视频 3.音乐
    private String tips;//提示语
    private int maxCheckCount = 1;// 默认单选 最大选择数--  <=0:自由多选，1：单选，>1：最大多选

    @Override
    protected int getLayoutId() {
        return R.layout.ndialog_select;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        if (!TextUtils.isEmpty(tips)) {
            TextView nDialogTip = mView.findViewById(R.id.nDialogTip);
            nDialogTip.setText(tips);
        }
        switch (chooseType) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }



    public CSelectDialog setInitDataList(List<MediaFileBean> initDataList) {
        this.initDataList = initDataList;
        return this;
    }

    public CSelectDialog setChooseType(int chooseType) {
        this.chooseType = chooseType;
        return this;
    }

    public CSelectDialog setTips(String tips) {
        this.tips = tips;
        return this;
    }

    public CSelectDialog setMaxCheckCount(int maxCheckCount) {
        this.maxCheckCount = maxCheckCount;
        return this;
    }
}
