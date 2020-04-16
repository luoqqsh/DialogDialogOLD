package x.com.dialogdialog;


import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.io.File;
import java.util.Map;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.dialogdialog.bbz.HttpMethodConfig;
import x.com.hl_base.interfaces.HOnListener;
import x.com.hl_dialog.DFListFileDialog;
import x.com.hl_livedatabus.LiveEventBus;
import x.com.hl_log.ViseLog;
import x.com.hl_rx.download.DownloadListener;
import x.com.hl_rx.download.FileDownloadManager;
import x.com.hl_rx.jiu.SimpleSubscriber;
import x.com.hl_rx.test.TvListData;
import x.com.hl_tools.hl_file;
import x.com.hl_tools.hl_media;
import x.com.hl_tools.hl_permissions;

public class DialogMainJavaActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.brown;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        init();
        hl_permissions.applyPermission(mActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {

            }
        });
        liveEventBus();
        HttpMethodConfig.instance().getTvList(new SimpleSubscriber<TvListData>() {
            @Override
            public void onSuccess(TvListData result) {
                ViseLog.d(result);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private void liveEventBus() {
        //要接收的数据，接收普通消息
        LiveEventBus
                .get("KEY_TEST_OBSERVE", Integer.class)
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer s) {
                        Toast.makeText(mActivity, mActivity.getLocalClassName() + s.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        //发送普通消息
        LiveEventBus.get("KEY_TEST_OBSERVE", Integer.class).post(432423);

    }

    private void init() {
        findViewById(R.id.mainDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DFListFileDialog df = DFListFileDialog.newInstance(hl_file.QQFILE2);
                df.setOnSelectFileListener(new HOnListener<File>() {
                    @Override
                    public void onListen(File file) {
                        ViseLog.d(file.getAbsolutePath());
                    }
                });
                df.showFDialog(mActivity);
            }
        });
        findViewById(R.id.mainJava).setVisibility(View.GONE);
        findViewById(R.id.mainPlayer).setOnClickListener(view -> startActivity(new Intent(mActivity, HHMedia3Activity.class)));
        findViewById(R.id.mainWeb).setOnClickListener(v -> startActivity(new Intent(mActivity, H5Activity.class)));
        findViewById(R.id.mainLog).setOnClickListener(v -> startActivity(new Intent(mActivity, LogActivity.class)));
        findViewById(R.id.mainOpenGL).setOnClickListener(v -> startActivity(new Intent(mActivity, GLActivity.class)));
        findViewById(R.id.mainConfusion).setOnClickListener(v -> startActivity(new Intent(mActivity, MappingActivity.class)));
        findViewById(R.id.mainNotice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, NotificationActivity.class);
                intent.putExtra("aaa", true);
                startActivity(intent);
            }
        });
        findViewById(R.id.wallpaperset).setOnClickListener(v -> {
            LiveEventBus.get("KEY_TEST_OBSERVE", Integer.class).post(432423);

        });
        findViewById(R.id.close).setOnClickListener(v -> {

        });
        ImageView mainImg = findViewById(R.id.mainImg);
        findViewById(R.id.mainLocal).setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, 100);
//            String path = "http://s.bizhijingling.com/uploadfile/coustom/2019/309/20190613170125729.mp4";
//            String path2 = "http://cstvpull.live.wscdns.com/live/xiamen1.flv";
            String path3 = "/storage/emulated/0/DCIM/Camera/2022712031179.mp4";
//            GlideUtil.instance().setDefaultImage(mActivity, path3, mainImg);
//            Glide.with(mActivity).load(path3).into(mainImg);
            mainImg.setImageBitmap(hl_media.getVideoThumb(path3));
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = mActivity.getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String videoPath = cursor.getString(columnIndex);
            cursor.close();
            ViseLog.d(videoPath);
        }
    }
}
