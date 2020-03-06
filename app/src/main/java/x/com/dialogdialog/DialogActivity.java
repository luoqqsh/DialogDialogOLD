package x.com.dialogdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import x.com.hl_base.hl_activity.HLBaseActivity;
import x.com.hl_base.hl_dialog.NDialog;
import x.com.hl_base.hl_interface.OnClickItemListener;
import x.com.hl_dialog.CBottomListDialog;
import x.com.hl_dialog.CTipDialog;
import x.com.hl_dialog.CWaitingDialog;
import x.com.hl_log.ViseLog;

public class DialogActivity extends HLBaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_dialogtools;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setTitleText("dialog");
        setLoadingEnd();
        mTitle.setTitle2Text("aaaaaa");
        mTitle.setRightButtonText("aaasss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HLNotification.show(mActivity, "提示", "提示信息", R.mipmap.ic_launcher).setOnNotificationClickListener(new OnSimpleListener() {
//                    @Override
//                    public void onListen() {
//                        ViseLog.showInfo("提示", "点击了消息");
//                    }
//                }).setOnDismissListener(new OnSimpleListener() {
//                    @Override
//                    public void onListen() {
//                        ViseLog.showInfo("消息溜走了");
//                    }
//                });
                CTipDialog c = new CTipDialog(CTipDialog.ndialog_Input)
                        .setTouchOutSideCancelable(true, true)
                        .setMessage("我是鬼，我来抓你了！", Gravity.CENTER);
                c.setBtnClickListener("好", "不好", new OnClickItemListener() {
                    @Override
                    public boolean onItemClick(String dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                Toast.makeText(mActivity, "点击了好", Toast.LENGTH_SHORT).show();
                                ViseLog.d(c.getInputText());
                                break;
                            case 2:
                                Toast.makeText(mActivity, "点击了不好", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                c.showDialog(mActivity);
            }
        });


//        KTaskManager(new OnTSimpleListener<Object>() {
//            @Override
//            public Object onBackGround() {
//                ViseLog.d("asdas");
//                return new MediaFileBean();
//            }
//
//            @Override
//            public void onUI(Object o) {
//                ViseLog.d(o);
//            }
//        });
//        KTaskManagerB(new OnSimpleListener() {
//            @Override
//            public void onListen() {
//                ViseLog.d("afafafaf");
//            }
//        });
    }

    @Override
    protected boolean isOpenSlideBack() {
        return false;
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    //    private void downloads() {
//        //下载工具,不支持断点续传
//        new DownloadHelper(this,
//                "http://imtt.dd.qq.com/16891/337A49BBE7A8A0B42E2312893903BBB3.apk?fsname=com.coolsnow.screenshot_5.6.0_56000.apk",
//                new DownloadHelper.DownloadCallBack() {
//                    @Override
//                    public void installCancel() {
//                        //取消安装
//                    }
//
//                    @Override
//                    public void downloadSuccess(File file) {
//                        //下载成功
//                    }
//
//                    @Override
//                    public void downloadFail() {
//                        //下载失败
//                    }
//                })
//                //设置是否强制升级
//                .setIsForce(false)
//                //配置通知参数
//                .setNotificationShow(true, "下载通知标题", R.mipmap.ic_launcher)
//                //配置弹框
//                .setDialogShow(true, "取消安装", "立即安装")
//                //配置下载路径
//                .setSavePath(Environment.getExternalStorageDirectory() + "/" + "checkUp")
//                //配置是否为版本升级，true则自动执行安装apk
//                .setCheckUp(true)
//                //配置存储地址 默认Environment.getExternalStorageDirectory() + "/" + "deanDownload";
//                .setSavePath(Environment.getExternalStorageDirectory() + "/" + "deanDownload");
//        //开启下载
//        //.start();
//        //停止下载
//        DownloadHelper.cancelAll();
//    }

    private void updates() {
        //检查更新框样式
//        Dialog g = new NDialog(mActivity, NDialog.nDialog_Update, 1, true)
//                .setTouchOutSideCancelable(false, false)
//                .setMessage("111", Gravity.LEFT)
//                .setBtnClickListener(true, "下次再说", "立即更新", new NDialog.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Toast.makeText(context, "下次再说", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 2:
//                                Toast.makeText(context, "立即更新", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }).create();
//        g.show();

        //检查更新样式2
//        DFUpdateDialog.newInstance("版本更新内容", 1,
//                "http://imtt.dd.qq.com/16891/337A49BBE7A8A0B42E2312893903BBB3.apk?fsname=com.coolsnow.screenshot_5.6.0_56000.apk"
//        ).show(getSupportFragmentManager(), "checkUpNew");
    }

    private void dialogs() {
        //显示加载框
        CWaitingDialog cWaitingDialog = new CWaitingDialog();
        cWaitingDialog.showDialog(mActivity);

        //透明弹窗
        new NDialog(mActivity, NDialog.ndialog_Trans, 0, false).createTrans(150);

        //显示iPhone样式弹窗
//        CTipDialog c = new CTipDialog(mActivity, CTipDialog.nDialog_Iphone)
//                .setTouchOutSideCancelable(true, true)
//                .setMessage("我是鬼，我来抓你了！", Gravity.CENTER)
//                .setBtnClickListener("好", "不好", new OnClickItemListener() {
//                    @Override
//                    public boolean onItemClick(String dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Toast.makeText(mActivity, "点击了好", Toast.LENGTH_SHORT).show();
////                                c.refresh("lajigui", Gravity.LEFT);
//                                break;
//                            case 2:
//                                Toast.makeText(mActivity, "点击了不好", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//        c.showDialog(mActivity);
        //底部弹窗1
        CBottomListDialog cBottomListDialog = new CBottomListDialog();
        cBottomListDialog.setDataList(new String[]{"选取照片", "选取视频"});
        cBottomListDialog.setShowCancelBtn(false);
        cBottomListDialog.addTitle("tete", Gravity.CENTER, 20, R.color.red);
        cBottomListDialog.setOnClickItemListener(new OnClickItemListener() {
            @Override
            public boolean onItemClick(String text, int position) {
                ViseLog.d(text + " " + position);
                return true;
            }
        });
        cBottomListDialog.showDialog(mActivity);
        //sex性别选择样式
        Dialog d = new NDialog(mActivity, NDialog.nDialog_Sex, 1.0f)
                .setDialoglocation(NDialog.DIALOG_LOCATION_BOTTOM)
                .setDialogAnimation(NDialog.DIALOG_ANIM_SLID_BOTTOM)
                .setTouchOutSideCancelable(false, false)
                .setBtnClickListener(true, "女", "男", "取消", new NDialog.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                Toast.makeText(context, "女", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(context, "男", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create();
        d.show();

        //share分享样式
        Dialog e = new NDialog(mActivity, R.layout.dialog_select, 1)
                .setBtnClickListener(true, "微信", "朋友圈", new NDialog.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                Toast.makeText(context, "微信", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(context, "朋友圈", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create();
        e.show();

        //拍照选项样式
        Dialog f = new NDialog(mActivity, NDialog.nDialog_Photo, 1)
                .setDialoglocation(NDialog.DIALOG_LOCATION_BOTTOM)
                .setDialogAnimation(NDialog.DIALOG_ANIM_SLID_BOTTOM)
                .setTouchOutSideCancelable(false, false)
                .setBtnClickListener(true, "拍照", "相册", "取消", new NDialog.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                Toast.makeText(context, "拍照", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(context, "相册", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create();
        f.show();
    }
}
