package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xxing.amap.AmapController;
import com.example.xxing.amap.AmapLocation;
import com.example.xxing.map_bamap.BmapSearch;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.dialogdialog.BaseApp.Config;
import x.com.hl_log.ViseLog;
import x.com.hl_map.HHLocation;
import x.com.hl_map.HHSearch;
import x.com.hl_map.HHmap;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.LineType;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.LocationType;
import x.com.hl_map.bean.MapType;
import x.com.hl_map.listener.OnCameraChangeListener;
import x.com.hl_map.listener.OnLocationListener;
import x.com.hl_map.listener.OnMapReadyListener;

public class HHMapActivity extends BaseActivity {
    private HHmap hHmap;
    private HHLocation hhLocation;
    private Object marker;
    @Override
    public void initUI(Bundle savedInstanceState) {
        LinearLayout ll_mapview = findViewById(R.id.ll_mapview);
        hHmap = new HHmap(this, new AmapController());
        hHmap.addMapView(ll_mapview, savedInstanceState, new OnCameraChangeListener() {
            @Override
            public void onCameraChange(LocationBean latLngs) {
                if (latLngs.getFrom() == LocationFrom.MOVEMAP_FINISH) {
                    ViseLog.d(latLngs);
                }
            }
        }, new OnMapReadyListener() {
            @Override
            public void onReady() {
                ViseLog.d("onready");
                hHmap.moveToPosition(new LatLngBean(Config.testLat1, Config.testLng1), new LatLngBean(Config.testLat2, Config.testLng2), 100);
                hHmap.addMarker(new LineBean().setMarker(new LatLngBean(Config.testLat1, Config.testLng1), R.mipmap.map_icon_p));
                hHmap.addMarker(new LineBean().setMarker(new LatLngBean(Config.testLat2, Config.testLng2), R.mipmap.map_icon_p));
            }
        });


//        hHmap.searchRoute(RouteSearchType.CAR, new LatLngBean(Config.testLat1, Config.testLng1), new LatLngBean(Config.testLat2, Config.testLng2), Config.city, new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs) {
//                ViseLog.d(latLngs);
//                LineBean lineBean = new LineBean().setLine(latLngs.getPois(), 10);
//                lineBean.setColorList(latLngs.getColorList());
//                hHmap.addMarker(lineBean);
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//
//            }
//        });
        hhLocation = new HHLocation(getApplicationContext(), new AmapLocation());
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
        hHmap.addGoogleMap(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mapview;
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
        LineBean markerBean = new LineBean(LineType.MARKER);
        markerBean.setPosition(Config.testLat1, Config.testLng1);
        markerBean.setImgSource(R.mipmap.map_icon_p);
        marker = hHmap.addMarker(markerBean);
    }

    public void set(View view) {
        LineBean markerBean = new LineBean(LineType.MARKER);
        markerBean.setPosition(Config.testLat1, Config.testLng1);
        markerBean.setImgSource(R.mipmap.fx_gengduo);
        hHmap.setMarker(marker, markerBean);
    }

    public void remove(View view) {
        ViseLog.d(hHmap.removeMarker(marker, LineType.MARKER));
    }

    public void search(View view) {
        HHSearch hhSearch = new HHSearch(this, MapType.AMAP, new BmapSearch());
        hhSearch.address2Point("大同路", Config.city, new OnLocationListener() {
            @Override
            public void onCurrentLocation(LocationBean latLngs) {
                ViseLog.d(latLngs);
            }

            @Override
            public void onError(int code, String errorInfo) {
                ViseLog.d(code);

            }
        });
        hhSearch.point2address(Config.testLat1, Config.testLng1, 1000, LocationType.AMAP, new OnLocationListener() {
            @Override
            public void onCurrentLocation(LocationBean latLngs) {
                ViseLog.d(latLngs);
            }

            @Override
            public void onError(int code, String errorInfo) {
                ViseLog.d(errorInfo);
            }
        });

//        hhSearch.showTips("火车站", Config.city, true, new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs) {
//                ViseLog.d(latLngs);
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//            }
//        });
//        hhSearch.poiSearch("火车站", "", Config.city, new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs) {
//                ViseLog.d(latLngs);
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//            }
//        });
    }
}
