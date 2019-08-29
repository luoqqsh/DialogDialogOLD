package x.com.dialogdialog;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_base.HOnListener;
import x.com.hl_dialog.DFListFileDialog;
import x.com.hl_dialog.DFNDialog;
import x.com.hl_fileselect.FileSelectorView;
import x.com.hl_log.ViseLog;
import x.com.hl_player.TvListBean.TvData;
import x.com.hl_player.TvListBean.TvDataList;
import x.com.hl_player.TvListBean.TypeClarity;
import x.com.hl_player.TvListBean.TypeClass;
import x.com.hl_player.TvListBean.TypeState;
import x.com.hl_tools.hl_drawable;
import x.com.hl_tools.hl_file;
import x.com.hl_tools.hl_img;
import x.com.hl_tools.hl_permissions;

public class DialogMainJavaActivity extends BaseActivity {
    private DFNDialog b;
    private String aa;
    private FileSelectorView fileSelectorView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.color.brown);
        init();
        initTvList();
        test();
        hl_permissions.applyPermission(mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {

            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {

            }
        });
    }

    private void test() {
        
    }

    private void initTvList() {
        ImageView imageView = findViewById(R.id.imgv_test);
        imageView.setBackground(hl_drawable.getRoundRectDrawable(0, hl_img.getColorFromString("#5bc0de"), true, 10));

        TvDataList tvDataList = new TvDataList(TypeClass.中央台, "中央电视台", "rtsp://27.148.240.138/PLTV/88888888/224/3221227012/10000100000000060000000001740935_0.smil", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList2 = new TvDataList(TypeClass.中央台, "美国电视台", "http://183.56.168.47/cdn-out.iqy.sc96655.com/tslive/c204_scc_20190107_yvwl5h02_original_t10/c204_scc_20190107_yvwl5h02_original_t103.m3u8", TypeClarity.标清, TypeState.流畅);
        tvDataList.addUrl("http://183.56.168.47/cdn-out.iqy.sc96655.com/tslive/c204_scc_20190107_yvwl5h02_original_t10/c204_scc_20190107_yvwl5h02_original_t10.m3u8", TypeClarity.高清, TypeState.流畅);
        tvDataList.addUrl("rtsp://27.148.240.138/PLTV/88888888/224/3221226716/10000100000000060000000001664269_0.smil", TypeClarity.H8k, TypeState.卡顿);
        ArrayList<TvDataList> tvDataLists = new ArrayList<>();
        tvDataLists.add(tvDataList);
        tvDataLists.add(tvDataList2);
        TvData tvUrl = new TvData(1, "20190719", "itv", tvDataLists);
        Gson gson = new Gson();
        String json = gson.toJson(tvUrl);
        ViseLog.d(json);
    }

    private void init() {
        findViewById(R.id.java).setVisibility(View.GONE);
        findViewById(R.id.tongzhi).setOnClickListener(view -> startActivity(new Intent(mContext, NotificationActivity.class)));
        findViewById(R.id.media).setOnClickListener(view -> startActivity(new Intent(mContext, HHMedia2Activity.class)));
        findViewById(R.id.x5web).setOnClickListener(v -> startActivity(new Intent(mContext, H5Activity.class)));
        findViewById(R.id.log).setOnClickListener(v -> startActivity(new Intent(mContext, LogActivity.class)));
        findViewById(R.id.open).setOnClickListener(v -> startActivity(new Intent(mContext, GLActivity.class)));
        findViewById(R.id.mapping).setOnClickListener(v -> startActivity(new Intent(mContext, MappingActivity.class)));
        findViewById(R.id.close).setOnClickListener(v -> {
//            hl_orientation orientation = new hl_orientation();
//            orientation.orientationEnable(this, null);
            b.dismiss();
        });

        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                b = DFNDialog.newInstance();
//                b.setMessage("dddddddddd", 4);
//                if (!b.isAdded()) {
//                    b.show(getSupportFragmentManager(), "normalDialog");
//                }

                DFListFileDialog df = DFListFileDialog.newInstance(hl_file.QQFILE);
                df.setOnSelectFileListener(new HOnListener<File>() {
                    @Override
                    public void onListen(File file) {
                        ViseLog.d(file.getAbsolutePath());
                    }
                });
                df.showFDialog(mContext);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
