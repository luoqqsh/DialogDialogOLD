package x.com.hl_fileselect.media;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import x.com.hl_base.app.AppInit;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_toast;

public class MediaConfig implements Serializable {
    private static final long serialVersionUID = 6357206378404061331L;
    private int minSelectCount = 1;
    private int maxSelectCount = 1;
    private String tip;
    private boolean isShowCamera = true;
    private boolean isNeedPreview = true;
    private List<MediaFileBean> mSelectList;
    private MediaSelectListener<MediaFileBean> onSingleListener;
    private MediaSelectListener<List<MediaFileBean>> onMoreSelectListener;

    public int getMinSelectCount() {
        return minSelectCount;
    }

    public void setMinSelectCount(int minSelectCount) {
        this.minSelectCount = minSelectCount;
    }

    public int getMaxSelectCount() {
        return maxSelectCount;
    }

    public void setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public List<MediaFileBean> getSelectList() {
        if (mSelectList == null) {
            mSelectList = new ArrayList<>();
        }
        return mSelectList;
    }

    public void setSelectList(List<MediaFileBean> mSelectList) {
        this.mSelectList = mSelectList;
    }

    public void setOnSingleListener(MediaSelectListener<MediaFileBean> onSingleListener) {
        this.onSingleListener = onSingleListener;
    }

    public void setOnMoreSelectListener(MediaSelectListener<List<MediaFileBean>> onMoreSelectListener) {
        this.onMoreSelectListener = onMoreSelectListener;
    }

    public void setShowCamera(boolean showCamera) {
        isShowCamera = showCamera;
    }

    public void setNeedPreview(boolean needPreview) {
        isNeedPreview = needPreview;
    }

    public boolean isSingleSelect() {
        return minSelectCount == 1 && maxSelectCount == 1;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public boolean isNeedPreview() {
        return isNeedPreview;
    }

    public boolean callback() {
        if (getSelectList().size() == 0) {
            hl_toast.show(AppInit.getAppContext(), "您没有选择");
            return false;
        }
        if (getSelectList().size() < minSelectCount) {
            hl_toast.show(AppInit.getAppContext(), "最少选择" + minSelectCount);
            return false;
        }
        if (getSelectList().size() > maxSelectCount) {
            hl_toast.show(AppInit.getAppContext(), "最多选择" + maxSelectCount);
            return false;
        }
        if (isSingleSelect()) {
            if (onSingleListener != null) {
                onSingleListener.onListen(mSelectList.get(0));
            }
        }
        if (onMoreSelectListener != null) {
            onMoreSelectListener.onListen(mSelectList);
        }
        return true;
    }
}
