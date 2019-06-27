package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.xxing.amap.AmapController;
import com.example.xxing.amap.AmapLocation;
import com.example.xxing.amap.AmapSearch;
import com.example.xxing.map_bamap.BmapController;
import com.example.xxing.map_bamap.BmapLocation;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.dialogdialog.Base.Config;
import x.com.hl_log.ViseLog;
import x.com.hl_map.HHLocation;
import x.com.hl_map.HHSearch;
import x.com.hl_map.HHmap;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
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
        hHmap = new HHmap(this, MapType.BAIDUMAP, new BmapController());
        hHmap.addMapView(ll_mapview, savedInstanceState, new OnCameraChangeListener() {
            @Override
            public void onCameraChange(LocationBean latLngs) {
                if (latLngs.getFrom() == LocationFrom.MOVEMAP_FINISH) {
                    ViseLog.d(latLngs);
                }
            }
        });
        hHmap.moveToPosition(Config.testLat, Config.testLng, 17);

        hhLocation = new HHLocation(getApplicationContext(), MapType.BAIDUMAP, new BmapLocation());
        hhLocation.setOnLocationListener(new OnLocationListener() {
            @Override
            public void onCurrentLocation(LocationBean latLngs) {
                ViseLog.d(latLngs);
            }

            @Override
            public void onError(int code, String errorInfo) {

            }
        });
        hhLocation.startLocation();

        HHSearch hhSearch = new HHSearch(this, MapType.AMAP, new AmapSearch());
//        hhSearch.address2Point("大同路", Config.city, new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs) {
//                ViseLog.d(latLngs);
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//                ViseLog.d(code);
//
//            }
//        });
//        hhSearch.point2address(Config.seaLat, Config.seaLng, 1000, LocationType.AMAP, new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs) {
//                ViseLog.d(latLngs);
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//                ViseLog.d(code);
//            }
//        });

        hhSearch.showTips("火车站", Config.cityCode, true, new OnLocationListener() {
            @Override
            public void onCurrentLocation(LocationBean latLngs) {
            }

            @Override
            public void onError(int code, String errorInfo) {
            }
        });
        hhSearch.poiSearch("火车站", "", Config.cityCode, new OnLocationListener() {
            @Override
            public void onCurrentLocation(LocationBean latLngs) {
            }

            @Override
            public void onError(int code, String errorInfo) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hHmap.onDestroy();
        hhLocation.stopLocation();
        hhLocation.releaseLocationListener();
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
