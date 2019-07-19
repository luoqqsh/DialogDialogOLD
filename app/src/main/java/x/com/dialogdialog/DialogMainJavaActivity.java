package x.com.dialogdialog;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_dialog.DFNDialog;
import x.com.hl_log.ViseLog;
import x.com.hl_player.base.TvBean;
import x.com.hl_player.base.TvInfo;
import x.com.hl_player.base.TvType;
import x.com.hl_player.base.TvUrl;
import x.com.hl_tools.hl_cachefile;
import x.com.hl_tools.hl_drawable;
import x.com.hl_tools.hl_img;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_sharedP;
import x.com.hl_tools.model.PermissionModel;

public class DialogMainJavaActivity extends BaseActivity {
    private DFNDialog b;
    private String aa;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.color.brown);
        init();
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
        List<PermissionModel> permissionModels = new ArrayList<>();
        ImageView imageView = findViewById(R.id.imgv_test);
        imageView.setBackground(hl_drawable.getRoundRectDrawable(0, hl_img.getColorFromString("#5bc0de"), true, 10));

        permissionModels.add(new PermissionModel("aa", false, "wwww"));
        permissionModels.add(new PermissionModel("a12a", false, "ww"));
        permissionModels.add(new PermissionModel("a333a", true, "dddd"));
        permissionModels.add(new PermissionModel("aa444", false, "czczxccxc"));


        hl_sharedP.setParam(this, "aaaas", permissionModels);


        List<PermissionModel> ccc = hl_sharedP.getParam(this, "aaaas123", new ArrayList<>());
        ViseLog.d(ccc);

        hl_cachefile.instance(this).saveObject("aaa", permissionModels);
        List<PermissionModel> aaa = hl_cachefile.instance(this).readObject("aaa");
        ViseLog.d(aaa);
        hl_cachefile.instance(this).clear();

        TvBean tvBean = new TvBean(TvType.中央台, "https://www.baidu.com/a.m3u8", "中央电视台", new TvInfo());
        List<TvBean> tvBeans = new ArrayList<>();
        tvBeans.add(tvBean);
        tvBeans.add(tvBean);
        tvBeans.add(tvBean);
        TvUrl tvUrl = new TvUrl(1, "20190719", tvBeans);
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
                b = DFNDialog.newInstance();
                b.setMessage("dddddddddd", 4);
                if (!b.isAdded()) {
                    b.show(getSupportFragmentManager(), "normalDialog");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
