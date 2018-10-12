package x.com.dialogdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import x.com.hl_dialog.DFLoadingDialog;
import x.com.hl_dialog.DFNDialog;


public class NormalDialogActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        TextView ewfe = findViewById(R.id.ewfe);
        ewfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DialogUtil.showLoadDialog();
                a.show(getSupportFragmentManager(), "fff");
            }
        });
        //dialogfragment
//        DFNDialog a = DFNDialog.newInstance(3, "e3");
//        a.setMessage("dddddddddd", 4);
//        a.show(getSupportFragmentManager(), "fff");
//        a.dismiss();

        ViewPager vp = findViewById(R.id.viewpager);
        Fragment fragmenttest = new Fragmentdialogtest();
        Fragment fragmenttest2 = new Fragmentdialogtest();
        List<Fragment>fragments=new ArrayList<>();
        fragments.add(fragmenttest);
        fragments.add(fragmenttest2);
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        vp.setAdapter(pagerAdapter);
        //iphone
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
//        //sex性别选择
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
        //share分享
//        a = new NDialog(this, NDialog.nDialog_Share, 1)
//                .setDialoglocation(NDialog.DIALOG_LOCATION_BOTTOM)
//                .setDialogAnimation(NDialog.DIALOG_ANIM_SLID_BOTTOM)
//                .setTouchOutSideCancelable(false, false)
//                .setBtnClickListener(true, "微信", "朋友圈", "QQ",
//                        "新浪微博", "取消", new NDialog.onDialogbtnClickListener() {
//                            @Override
//                            public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                                switch (whichBtn) {
//                                    case 1:
//                                        Toast.makeText(context, "微信", Toast.LENGTH_SHORT).show();
//                                        break;
//                                    case 2:
//                                        Toast.makeText(context, "朋友圈", Toast.LENGTH_SHORT).show();
//                                        break;
//                                    case 3:
//                                        Toast.makeText(context, "QQ", Toast.LENGTH_SHORT).show();
//                                        break;
//                                    case 4:
//                                        Toast.makeText(context, "新浪微博", Toast.LENGTH_SHORT).show();
//                                        break;
//                                    case 5:
//                                        Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
//                                        break;
//
//                                }
//                            }
//                        }).create();
//        a.show();
        //拍照选项
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
        //检查更新框
//        a = new NDialog(this, NDialog.nDialog_Update, 1)
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
//        a.show();


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
}
