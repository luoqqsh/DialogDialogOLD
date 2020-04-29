package x.com.hl_fileselect.media;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_base.dialog.BaseNDialog;
import x.com.hl_bean.CSelectType;

public abstract class BaseSelectDialog extends BaseNDialog {
    protected List<MediaFileBean> mFileList = new ArrayList<>();//数据
    protected @CSelectType
    int chooseType;//选择类别 0.文件 1.图片 2.视频 3.音乐
    protected MediaConfig mediaConfig;

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
    }

    public void setMediaConfig(MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
    }
}
