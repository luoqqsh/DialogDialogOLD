package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.xxing.amap.AmapController;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_log.ViseLog;
import x.com.hl_map.HHmap;
import x.com.hl_map.bean.MarkerBean;

public class MapActivity extends BaseActivity {
    private LinearLayout ll_mapview;
    private HHmap hHmap;
    private Object marker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        ll_mapview = findViewById(R.id.ll_mapview);
        hHmap = new HHmap(this, HHmap.MapType.AMAP, new AmapController());
        hHmap.addMapView(ll_mapview, savedInstanceState);
        hHmap.moveToPosition(24, 118, 0);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hHmap.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        hHmap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        hHmap.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hHmap.onSaveInstanceState(outState);
    }

    public void add(View view) {
        MarkerBean markerBean = new MarkerBean();
        markerBean.setPosition(24, 118);
        markerBean.setImgSource(R.mipmap.map_icon_p);
        marker = hHmap.addMarker(markerBean);
    }

    public void set(View view) {
        MarkerBean markerBean = new MarkerBean();
        markerBean.setPosition(24, 118);
        markerBean.setImgSource(R.mipmap.fx_gengduo);
        hHmap.setMarker(marker, markerBean);
    }

    public void remove(View view) {
        ViseLog.d(hHmap.removeMarker(marker));

    }
}
