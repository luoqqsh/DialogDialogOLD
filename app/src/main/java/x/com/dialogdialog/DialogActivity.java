package x.com.dialogdialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import x.com.base.dialog.NDialog;
import x.com.base.interfaces.HOnListener;
import x.com.base.interfaces.OnSimpleListener;
import x.com.dialog.CBottomListDialog;
import x.com.dialog.CFileListDialog;
import x.com.dialog.CProgressDialog;
import x.com.dialog.CTipDialog;
import x.com.dialog.CWaitingDialog;
import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.fliepick.bean.CPickType;
import x.com.fliepick.bean.FileBean;
import x.com.fliepick.media.CSystemPickDialog;
import x.com.fliepick.media.MediaConfig;
import x.com.log.ViseLog;
import x.com.media.U_mediaList;
import x.com.rxHttp.task.TaskDelayBManager;
import x.com.util.U_file;

public class DialogActivity extends BaseActivity {
    @Override
    protected int getBackgroundColorResource() {
        return R.color.white;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setLeftButtonImage(R.mipmap.hl_back_black, v -> finish());
        mTitle.setTitleText("对话框");
    }

    //加载弹窗
    public void loadingDialog(View view) {
        CWaitingDialog cWaitingDialog = new CWaitingDialog();
        cWaitingDialog.showDialog(mActivity);
    }

    public void transDialog(View view) {
        //透明弹窗
        new NDialog(mActivity, R.layout.ndialog_trans, 0, false).createTrans(150);
    }

    //普通消息弹窗
    public void tipDialog(View view) {
        //显示iPhone样式弹窗
        CTipDialog c = new CTipDialog(CTipDialog.nDialog_Iphone2)
                .setTouchOutSideCancelable(true, true)
                .setMessage("我是鬼，我来抓你了！", Gravity.CENTER)
                .setBtnClickListener("好", "不好", (dialog, whichBtn) -> {
                    switch (whichBtn) {
                        case 1:
                            Toast.makeText(mActivity, "点击了好", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(mActivity, "点击了不好", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                });
        c.setOnCancelListener(new OnSimpleListener() {
            @Override
            public void onListen() {
                ViseLog.d("cancel");
            }
        });
        c.setOnDismissListener(new OnSimpleListener() {
            @Override
            public void onListen() {
                ViseLog.d("dismiss");
            }
        });
        c.showDialog(mActivity);
    }

    //系统自带选择图片、视频弹窗
    public void systemDialog(View view) {
        CSystemPickDialog.builder().setNeedCrop(true).setOnSingleListener(it -> {
            ViseLog.d(it);
        }).startPick(mActivity, CPickType.images);
    }

    //进度弹窗
    public void progressDialog(View view) {
        CProgressDialog p = new CProgressDialog();
        p.setStartProgress(60);
        p.showDialog(mActivity);
        new TaskDelayBManager() {
            @Override
            public void onListen(Long index) {
                ViseLog.d(index);
                p.showProgress(Integer.parseInt(index.toString()));
            }
        }.loop(1, 1000);
    }

    //自定义多选弹窗
    public void diyDialog(View view) {
        U_mediaList.getLocalImgFromLoader(mActivity, mediaFolderMap -> {
            ViseLog.d("mediaFolderMap");
            ViseLog.d(mediaFolderMap);
            U_mediaList.getMediaFromLoaderByPage(mActivity, MediaConfig.imgType, "", 1, 20, CPickType.images, new HOnListener<List<FileBean>>() {
                @Override
                public void onListen(List<FileBean> fileBeans) {
                    ViseLog.d(fileBeans);
                }
            });
        });
//        CMediaPickDialog2.builder().addTip(mActivity, "123")
//                .setNeedPreview(false)
//                .setShowCamera(false)
//                .setOnMoreSelectListener(info -> {
//                    ViseLog.d(info);
//                })
//                .startSelectImg(mActivity);
    }

    //底部列表弹窗
    public void bottomDialog(View view) {
        CBottomListDialog cBottomListDialog = new CBottomListDialog();
        cBottomListDialog.setDataList("选取照片", "选取视频");
        cBottomListDialog.setShowCancelBtn(true);
        cBottomListDialog.setOnClickItemListener((text, position) -> {
            ViseLog.d(text + " " + position);
            return true;
        });
        cBottomListDialog.showDialog(mActivity);
    }

    public void fileDialog(View view) {
        CFileListDialog c = new CFileListDialog();
        c.setPath(U_file.SDROOT);
        c.showDialog(mActivity);
    }
}
