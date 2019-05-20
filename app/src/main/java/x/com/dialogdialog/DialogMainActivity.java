package x.com.dialogdialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Map;

import x.com.hl_dialog.DFLoadingDialog;
import x.com.hl_dialog.DFNDialog;
import x.com.hl_dialog.DFUpdateDialog;
import x.com.hl_dialog.NDialog;
import x.com.hl_dialog.NDialogBuilder;
import x.com.hl_dialog.ProgressDialog.PDialog1Builder;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_orientation;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_sharedP;
import x.com.hl_view.DanMu.DanMuView;
import x.com.hl_view.DanMu.OnSimpleListener;

public class DialogMainActivity extends BaseActivity {
    private Context mContext;
    private DanMuView danMuView;
    private LottieAnimationView animation_view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        permissions();
        findViewById(R.id.tongzhi).setOnClickListener(view -> startActivity(new Intent(mContext, NotificationActivity.class)));
        findViewById(R.id.media).setOnClickListener(view -> startActivity(new Intent(mContext, HHMediaActivity.class)));
        findViewById(R.id.x5web).setOnClickListener(v -> startActivity(new Intent(mContext, H5Activity.class)));
        findViewById(R.id.log).setOnClickListener(v -> startActivity(new Intent(mContext, LogActivity.class)));
        findViewById(R.id.open).setOnClickListener(v -> startActivity(new Intent(mContext, GLActivity.class)));
        findViewById(R.id.close).setOnClickListener(v -> {
            hl_orientation orientation = new hl_orientation();
            orientation.orientationEnable(this, null);
        });

        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DFNDialog b = DFNDialog.newInstance();
                b.setMessage("dddddddddd", 4);
                if (!b.isAdded()) {
                    b.show(getSupportFragmentManager(), "normalDialog");
                }
            }
        });
    }

    private void danmu() {
        //弹幕使用
//        danMuView = findViewById(R.id.danmu);
        danMuView.setText("hfeqwdqw");//设置文本
        danMuView.setTextColor(0xffff0000);//设置文本颜色
        danMuView.setTextSizeScaleOfWidth(0.6f);//设置文字大小0-1，大于1会超出屏幕范围
        danMuView.setBackColor(0xff00ff00);//设置背景颜色
        danMuView.setDirection(0);//设置方向，0-从右往左，1-从左往右
        danMuView.setSpeed(15);//设置速度1-10,不建议超出这个范围
        danMuView.start();//开始播放
//        danMuView.stop();//停止
        danMuView.setOnCompleteListener(new OnSimpleListener() {
            @Override
            public void onListen() {
            }
        });
    }

    private void permissions() {
        //检查权限
        //需要在activity重写onRequestPermissionsResult回调方法。不处理回调不写

        hl_permissions.applyWriteStoragePermission(DialogMainActivity.this,
                new hl_permissions.RequestPermissionCallBack() {
                    @Override
                    public void requestPermissionSuccess() {
                        ViseLog.d("true2222");
                    }

                    @Override
                    public void requestPermissionFail(Map<String, Boolean> failPermission) {
                        ViseLog.d(failPermission);
                    }


                });

        //权限列表弹窗
//        DFPermissionListDialog i = DFPermissionListDialog.newInstance(new hl_permissionsOther.OnPermissionListener() {
//            @Override
//            public void result(boolean isSuccess) {
//                ViseLog.d(isSuccess);
//            }
//        });
//        i.show(getSupportFragmentManager(), "fff");
    }

    private void updates() {
        //检查更新框样式
//        Dialog g = new NDialog(this, NDialog.nDialog_Update, 1, true)
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
        DFUpdateDialog.newInstance("版本更新内容", 1,
                "http://imtt.dd.qq.com/16891/337A49BBE7A8A0B42E2312893903BBB3.apk?fsname=com.coolsnow.screenshot_5.6.0_56000.apk"
        ).show(getSupportFragmentManager(), "checkUpNew");
    }

    private void dialogs() {
        //显示加载框
        DFLoadingDialog a = new DFLoadingDialog();
        a.show(getSupportFragmentManager(), "loadingDialog");
        a.dismissAllowingStateLoss();
        //隐藏加载框
        a.dismiss();

        //透明弹窗
        new NDialog(mContext, NDialog.ndialog_Trans, 0, false).createTrans(150);

        //显示普通弹窗
        DFNDialog b = DFNDialog.newInstance();
        b.setMessage("dddddddddd", 4);
        if (!b.isAdded()) {
            b.show(getSupportFragmentManager(), "normalDialog");
        }
        //显示iPhone样式弹窗
        Dialog c = new NDialog(this, NDialog.nDialog_Iphone, 0.8f)
                .setTouchOutSideCancelable(false, false)
                .setMessage("我是鬼，我来抓你了！", NDialog.MSG_LAYOUT_CENTER)
                .setBtnClickListener(true, "好", "不好", new NDialog.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                Toast.makeText(context, "点击了好", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(context, "点击了不好", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create();
        c.show();

        //sex性别选择样式
        Dialog d = new NDialog(this, NDialog.nDialog_Sex, 1.0f)
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
        Dialog e = new NDialog(this, R.layout.dialog_select, 1)
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
        Dialog f = new NDialog(this, NDialog.nDialog_Photo, 1)
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

        //加载进度框，10秒后弹出出错提示
        Dialog h = new PDialog1Builder(this, 0, 1.0f).setMessage("www").settime(1000).setTouchOutSideCancelable(false)
                .setonInputCodeListener(new PDialog1Builder.onProgressFinishListener() {
                    @Override
                    public void onProgressFinish() {
                        Dialog errordialog = new NDialogBuilder(DialogMainActivity.this, 0, 1.0f)
                                .setTouchOutSideCancelable(false)
                                .setMessage("345tret", NDialogBuilder.MSG_LAYOUT_LEFT)
                                .setDialogAnimation(NDialogBuilder.DIALOG_ANIM_NORMAL)
                                .setBtnClickListener(true, "", new NDialogBuilder.onDialogbtnClickListener() {
                                    @Override
                                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                                    }
                                })
                                .create();
                        errordialog.show();
                    }
                }).create();
        h.show();
    }
}
