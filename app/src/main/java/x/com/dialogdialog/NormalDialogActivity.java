package x.com.dialogdialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import x.com.hl_dialog.DFPermissionListDialog;
import x.com.hl_dialog.NDialog;
import x.com.hl_tools.hl_contacts;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_permissionsOther;

public class NormalDialogActivity extends BaseActivity implements hl_permissionsOther.OnPermissionListener {
    private Dialog a;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView ewfe = findViewById(R.id.ewfe);
        ewfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //权限弹窗
//                DFPermissionListDialog a = DFPermissionListDialog.newInstance(new hl_permissionsOther.OnPermissionListener() {
//                    @Override
//                    public void result(boolean isSuccess) {
//                        ViseLog.d(isSuccess);
//                    }
//                });
//                a.show(getSupportFragmentManager(), "fff");
                //检查权限
                if (!hl_permissionsOther.checksetsystem(NormalDialogActivity.this)) {
                    hl_permissionsOther.gotosetsystem(NormalDialogActivity.this);
                }
                //检查权限
//                ViseLog.d(hl_permissions.checkpermission(NormalDialogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
                hl_permissions.getInstance().applyPermission(NormalDialogActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new hl_permissions.RequestPermissionCallBack() {
                            @Override
                            public void requestPermissionSuccess() {
                                ViseLog.d("true2222");
                            }

                            @Override
                            public void requestPermissionFail() {
                                ViseLog.d("false1111");
                            }
                        });

            }
        });
        TextView qqq = findViewById(R.id.qqq);
        qqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalDialogActivity.this, CheckUpActivity.class);
                startActivity(intent);
            }
        });
        //显示加载框
//        DFNDialog a = DFNDialog.newInstance();
//        a.setMessage("dddddddddd", 4);
//        a.show(getSupportFragmentManager(), "fff");
//        a.dismiss();


        //iphone样式
//        a = new NDialog(this, NDialog.NDialog_Iphone, 0.8f)
//                .setTouchOutSideCancelable(false, false)
//                .setMessage("我是鬼，我来抓你了！", NDialogBuilder.MSG_LAYOUT_CENTER)
//                .setBtnClickListener(true, "好", "不好", new NDialog.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Toast.makeText(context, "点击了好", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 2:
//                                Toast.makeText(context, "点击了不好", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }).create();
//        a.show();
//        //sex性别选择样式
//        a = new NDialog(this, NDialog.nDialog_Sex, 1.0f)
//                .setDialoglocation(NDialog.DIALOG_LOCATION_BOTTOM)
//                .setDialogAnimation(NDialog.DIALOG_ANIM_SLID_BOTTOM)
//                .setTouchOutSideCancelable(false, false)
//                .setBtnClickListener(true, "女", "男", "取消", new NDialog.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Toast.makeText(context, "女", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 2:
//                                Toast.makeText(context, "男", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }).create();
//        a.show();
////
//        share分享样式
//        a = new NDialog(this,R.layout.dialog_select, 1)
//                .setBtnClickListener(true, "微信", "朋友圈",  new NDialog.onDialogbtnClickListener() {
//                            @Override
//                            public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                                switch (whichBtn) {
//                                    case 1:
//                                        Toast.makeText(context, "微信", Toast.LENGTH_SHORT).show();
//                                        break;
//                                    case 2:
//                                        Toast.makeText(context, "朋友圈", Toast.LENGTH_SHORT).show();
//                                        break;
//
//
//                                }
//                            }
//                        }).create();
//        a.show();
        //拍照选项样式
//        a = new NDialog(this, NDialog.nDialog_Photo, 1)
//                .setDialoglocation(NDialog.DIALOG_LOCATION_BOTTOM)
//                .setDialogAnimation(NDialog.DIALOG_ANIM_SLID_BOTTOM)
//                .setTouchOutSideCancelable(false, false)
//                .setBtnClickListener(true, "拍照", "相册", "取消", new NDialog.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Toast.makeText(context, "拍照", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 2:
//                                Toast.makeText(context, "相册", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 3:
//                                Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }).create();
//        a.show();
        //检查更新框样式
//        a2 = new NDialog(this, NDialog.nDialog_Update, 1)
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
//        a2.show();


//        a = new PDialog1Builder(this, 0, 1.0f).setMessage("www").settime(1000).setTouchOutSideCancelable(false)
//                .setonInputCodeListener(new PDialog1Builder.onProgressFinishListener() {
//                    @Override
//                    public void onProgressFinish() {
//                        Dialog errordialog = new NDialogBuilder(NormalDialogActivity.this, 0, 1.0f)
//                                .setTouchOutSideCancelable(false)
//                                .setMessage("345tret", NDialogBuilder.MSG_LAYOUT_LEFT)
//                                .setDialogAnimation(NDialogBuilder.DIALOG_ANIM_NORMAL)
//                                .setBtnClickListener(true, "", new NDialogBuilder.onDialogbtnClickListener() {
//                                    @Override
//                                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                                    }
//                                })
//                                .create();
//                        errordialog.show();
//                    }
//                }).create();
//        a.show();

//        a = new EasyCheckDialogBuilder(this, 0, 1.0f).setTouchOutSideCancelable(false)
//                .setMessage("wefwefwefwef", Gravity.LEFT).isForce(1)
//                .setBtnClickListener(true, new EasyCheckDialogBuilder.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                EasyCheckDialogBuilder.setp(60);
//                                Log.d("ww", "1");
//                                break;
//                            case 2:
//                                Log.d("ww", "2");
//                                break;
//                        }
//                    }
//                }).create();
//        a.show();
//        EasyCheckDialogBuilder.setp(60);

        //listdialog
//        List<String> names = new ArrayList<>();
//        names.add("你拍一");
//        names.add("我拍二");
//        names.add("然后忘了");
//        ListDialog dialog = new ListDialog(this, R.style.transparentFrameWindowStyle, names);
//        dialog.setItemColor(R.color.colorAccent, R.color.colorPrimary);
//        dialog.setItemListener(new ListDialog.SelectDialogListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(NormalDialogActivity.this, names.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
//        dialog.show();
    }

    @Override
    public void result(boolean isSuccess) {

    }
}
