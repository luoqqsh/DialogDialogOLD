package x.com.dialogdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import x.com.dialogmobile.CheckUpdate.EasyCheckDialogBuilder;
import x.com.dialogmobile.NDialogBuilder;
import x.com.dialogmobile.PhotoDialogBuilder;
import x.com.dialogmobile.ProgressDialog.PDialog1Builder;
import x.com.dialogmobile.SexDialogBuilder;
import x.com.dialogmobile.ShareDialogBuilder;

public class NormalDialogActivity extends AppCompatActivity {
    private Dialog a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ewfe = findViewById(R.id.ewfe);
        ewfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyCheckDialogBuilder.setp(60);
            }
        });

//        a = new NDialogBuilder(this, 0, 1.0f)
//                .setTouchOutSideCancelable(false)
//                .setMessage("我是鬼，我来抓你了！", NDialogBuilder.MSG_LAYOUT_CENTER)
//                .setBtnClickListener(true, "好", "不好", new NDialogBuilder.onDialogbtnClickListener() {
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
////        a.show();
//
//
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
//        PDialog1Builder.stop();

//        a = new ShareDialogBuilder(this, 0, 1.0f).setTouchOutSideCancelable(false)
//                .setDialoglocation(ShareDialogBuilder.DIALOG_LOCATION_BOTTOM)
//                .setBtnClickListener(true, new ShareDialogBuilder.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Log.d("ww", "1");
//                                break;
//                            case 2:
//                                Log.d("ww", "2");
//                                break;
//                            case 3:
//                                Log.d("ww", "3");
//                                break;
//
//                            case 4:
//                                Log.d("ww", "4");
//                                break;
//                            default:
//                                Log.d("ww", "5");
//                        }
//                    }
//                }).create();
//        a.show();
//        PDialog1Builder.stop();
//        a = new SexDialogBuilder(this, 0, 1.0f).setTouchOutSideCancelable(false).setDialoglocation(SexDialogBuilder.DIALOG_LOCATION_BOTTOM)
//                .setBtnClickListener(true, new SexDialogBuilder.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Log.d("ww", "1");
//                                break;
//                            case 2:
//                                Log.d("ww", "2");
//                                break;
//                        }
//                    }
//                }).create();
//        a.show();

//        a = new PhotoDialogBuilder(this, 0, 1.0f).setTouchOutSideCancelable(false).setDialoglocation(SexDialogBuilder.DIALOG_LOCATION_BOTTOM)
//                .setBtnClickListener(true, new PhotoDialogBuilder.onDialogbtnClickListener() {
//                    @Override
//                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
//                        switch (whichBtn) {
//                            case 1:
//                                Log.d("ww", "1");
//                                break;
//                            case 2:
//                                Log.d("ww", "2");
//                                break;
//                        }
//                    }
//                }).create();
//        a.show();
        a = new EasyCheckDialogBuilder(this, 0, 1.0f).setTouchOutSideCancelable(false)
                .setMessage("wefwefwefwef", Gravity.LEFT).isForce(1)
                .setBtnClickListener(true, new EasyCheckDialogBuilder.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case 1:
                                Log.d("ww", "1");
                                break;
                            case 2:
                                Log.d("ww", "2");
                                break;
                        }
                    }
                }).create();
        a.show();
//        EasyCheckDialogBuilder.setp(60);
    }

}
