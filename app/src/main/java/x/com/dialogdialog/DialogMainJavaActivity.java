package x.com.dialogdialog;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jiujie.glide.GlideUtil;
import com.xing.hhplayer.common.TvListBean.TvData;
import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TvListBean.TypeClarity;
import com.xing.hhplayer.common.TvListBean.TypeClass;
import com.xing.hhplayer.common.TvListBean.TypeState;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.hl_base.DtmfSendService;
import x.com.hl_base.hl_interface.HOnListener;
import x.com.hl_dialog.DFListFileDialog;
import x.com.hl_log.ViseLog;
import x.com.hl_rx.TaskDelayBManager;
import x.com.hl_rx.TaskManager;
import x.com.hl_tools.hl_drawable;
import x.com.hl_tools.hl_file;
import x.com.hl_tools.hl_gson;
import x.com.hl_tools.hl_img;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_wallpaper;

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
        initTvList();
        TvDataList a=new TvDataList(TypeClass.中央台,"");
        startService(new Intent(this, DtmfSendService.class));
        hl_permissions.applyPermission(mActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {

            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {

            }
        });
    }

    private void test() {
        new TaskManager<Bitmap>() {
            @Override
            public Bitmap runOnBackgroundThread() {
                return GlideUtil.instance().getImageBitmap(mActivity, "http://s.bizhijingling.com/uploadfile/coustom/2019/1735/20191029090827981.jpg");

            }

            @Override
            public void runOnUIThread(Bitmap bitmap) {
                ViseLog.d(bitmap);
            }
        }.start();
    }

    private void initTvList() {
        ImageView imageView = findViewById(R.id.imgv_test);
        imageView.setBackground(hl_drawable.getRoundRectDrawable(0, hl_img.getColorFromString("#5bc0de"), true, 10));

        TvDataList tvDataList = new TvDataList(TypeClass.中央台, "中央电视台", "rtsp://27.148.240.138/PLTV/88888888/224/3221227012/10000100000000060000000001740935_0.smil", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList2 = new TvDataList(TypeClass.中央台, "美国电视台", "http://183.56.168.47/cdn-out.iqy.sc96655.com/tslive/c204_scc_20190107_yvwl5h02_original_t10/c204_scc_20190107_yvwl5h02_original_t103.m3u8", TypeClarity.标清, TypeState.流畅);
        TvDataList tvDataList3 = new TvDataList(TypeClass.中央台, "美国电视台", "http://183.56.168.47/cdn-out.iqy.sc96655.com/tslive/c204_scc_20190107_yvwl5h02_original_t10/c204_scc_20190107_yvwl5h02_original_t103.m3u8", TypeClarity.标清, TypeState.流畅);
        tvDataList.addUrl("http://183.56.168.47/cdn-out.iqy.sc96655.com/tslive/c204_scc_20190107_yvwl5h02_original_t10/c204_scc_20190107_yvwl5h02_original_t10.m3u8", TypeClarity.高清, TypeState.流畅);
        tvDataList.addUrl("rtsp://27.148.240.138/PLTV/88888888/224/3221226716/10000100000000060000000001664269_0.smil", TypeClarity.H8k, TypeState.卡顿);
        tvDataList.addUrl("rtsp://27.148.240.138/PLTV/88888888/224/3221226716/10000100000000060000000001664269_0.smil", TypeClarity.H8k, TypeState.卡顿);
        tvDataList.addUrl("rtsp://27.148.240.138/PLTV/88888888/224/3221226716/10000100000000060000000001664269_0.smil", TypeClarity.H8k, TypeState.卡顿);
        ArrayList<TvDataList> tvDataLists = new ArrayList<>();
        tvDataLists.add(tvDataList);
        tvDataLists.add(tvDataList3);
        tvDataLists.add(tvDataList);
        tvDataLists.add(tvDataList3);
        tvDataLists.add(tvDataList2);
        tvDataLists.add(tvDataList2);
        TvData tvUrl = new TvData(1, "20190719", "itv", tvDataLists);
        String json = hl_gson.instance().toJson(tvUrl);

//        ViseLog.d(json);
        ViseLog.d(tvUrl);
    }

    private void init() {
        findViewById(R.id.java).setVisibility(View.GONE);
//        findViewById(R.id.tongzhi).setOnClickListener(view -> startActivity(new Intent(mActivity, NotificationActivity.class)));
        findViewById(R.id.media).setOnClickListener(view -> startActivity(new Intent(mActivity, HHMedia3Activity.class)));
        findViewById(R.id.x5web).setOnClickListener(v -> startActivity(new Intent(mActivity, H5Activity.class)));
        findViewById(R.id.log).setOnClickListener(v -> startActivity(new Intent(mActivity, LogActivity.class)));
        findViewById(R.id.open).setOnClickListener(v -> startActivity(new Intent(mActivity, GLActivity.class)));
        findViewById(R.id.mapping).setOnClickListener(v -> startActivity(new Intent(mActivity, MappingActivity.class)));
        findViewById(R.id.tongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, NotificationActivity.class);
                intent.putExtra("aaa", true);
                startActivity(intent);
            }
        });
        findViewById(R.id.wallpaperset).setOnClickListener(v -> {
            ViseLog.d("asd");
            new TaskDelayBManager() {
                @Override
                public void onListen(Long index) {
                    Bitmap imageBitmap = GlideUtil.instance().getImageBitmap(mActivity, "http://s.bizhijingling.com/uploadfile/2019/1107/thumb_show_20191107171530891.jpg");
                    ViseLog.d(imageBitmap);
                    hl_wallpaper.setSystemPicWallpaper(mActivity, imageBitmap);
                }
            }.start();
        });
        findViewById(R.id.close).setOnClickListener(v -> {
            test();
            ViseLog.d("aaa", 1, 3, 5);
        });

        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DFListFileDialog df = DFListFileDialog.newInstance(hl_file.QQFILE);
                df.setOnSelectFileListener(new HOnListener<File>() {
                    @Override
                    public void onListen(File file) {
                        ViseLog.d(file.getAbsolutePath());
                    }
                });
                df.showFDialog(mActivity);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
