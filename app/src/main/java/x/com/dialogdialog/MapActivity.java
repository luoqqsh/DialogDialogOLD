package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.xxing.amap.AmapController;
import com.example.xxing.amap.AmapLocation;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.dialogdialog.Base.Config;
import x.com.hl_log.ViseLog;
import x.com.hl_map.HHLocation;
import x.com.hl_map.HHmap;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.MapType;
import x.com.hl_map.bean.MarkerBean;
import x.com.hl_map.listener.OnCameraChangeListener;
import x.com.hl_map.listener.OnLocationListener;

public class MapActivity extends BaseActivity {
    private HHmap hHmap;
    private HHLocation hhLocation;
    private Object marker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        LinearLayout ll_mapview = findViewById(R.id.ll_mapview);
        hHmap = new HHmap(this, MapType.AMAP, new AmapController());
        hHmap.addMapView(ll_mapview, savedInstanceState, new OnCameraChangeListener() {
            @Override
            public void onCameraChange(LocationBean latLngs, boolean isFinish) {
                if (isFinish) {

                }
            }
        });
        hHmap.moveToPosition(Config.testLat, Config.testLng, 17);

        hhLocation = new HHLocation(getApplicationContext(), MapType.AMAP, new AmapLocation());
        hhLocation.setOnLocationListener(new OnLocationListener() {
            @Override
            public void onCurrentLocation(LocationBean latLngs) {
            }

            @Override
            public void onError(int code, String errorInfo) {

            }
        });
        hhLocation.startLocation();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hHmap.onDestroy();
        hhLocation.stopLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        hHmap.onResume();
        hhLocation.startLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        hHmap.onPause();
        hhLocation.stopLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hHmap.onSaveInstanceState(outState);
    }

    public void add(View view) {
        MarkerBean markerBean = new MarkerBean();
        markerBean.setPosition(Config.testLat, Config.testLng);
        markerBean.setImgSource(R.mipmap.map_icon_p);
        marker = hHmap.addMarker(markerBean);
    }

    public void set(View view) {
        MarkerBean markerBean = new MarkerBean();
        markerBean.setPosition(Config.testLat, Config.testLng);
        markerBean.setImgSource(R.mipmap.fx_gengduo);
        hHmap.setMarker(marker, markerBean);
    }

    public void remove(View view) {
        ViseLog.d(hHmap.removeMarker(marker));

    }
}
