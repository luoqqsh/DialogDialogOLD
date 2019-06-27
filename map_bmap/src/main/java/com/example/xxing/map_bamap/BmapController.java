package com.example.xxing.map_bamap;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_map.MapController;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.MarkerBean;
import x.com.hl_map.listener.OnCameraChangeListener;

public class BmapController extends MapController {
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    @Override
    public void initMap(Context context) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(context);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    @Override
    public void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState, final OnCameraChangeListener onCameraChangeListener) {
        mMapView = new MapView(context, getMapOption());
        mMapView.onCreate(context, savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mMapView, params);
        mBaiduMap = mMapView.getMap();
        if (onCameraChangeListener != null) {
            mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
                @Override
                public void onMapStatusChangeStart(MapStatus mapStatus) {
                }

                @Override
                public void onMapStatusChangeStart(MapStatus status, int reason) {
                }

                @Override
                public void onMapStatusChange(MapStatus mapStatus) {
                    LocationBean latLngs = new LocationBean(LocationFrom.MOVEMAP, new LatLngBean(mapStatus.target.latitude, mapStatus.target.longitude));
                    latLngs.setZoom(mapStatus.zoom);
                    onCameraChangeListener.onCameraChange(latLngs);
                }

                @Override
                public void onMapStatusChangeFinish(MapStatus mapStatus) {
                    LocationBean latLngs = new LocationBean(LocationFrom.MOVEMAP_FINISH, new LatLngBean(mapStatus.target.latitude, mapStatus.target.longitude));
                    latLngs.setZoom(mapStatus.zoom);
                    onCameraChangeListener.onCameraChange(latLngs);
                }
            });
        }
    }

    private BaiduMapOptions getMapOption() {
        BaiduMapOptions options = new BaiduMapOptions();
        options.overlookingGesturesEnabled(false);// 设置是否允许俯视手势，默认允许
        options.rotateGesturesEnabled(false);// 设置是否允许旋转手势，默认允许
        options.scaleControlEnabled(false);// 设置是否显示比例尺控件
        options.scrollGesturesEnabled(true);// 设置是否允许拖拽手势，默认允许
        options.zoomControlsEnabled(true);// 设置是否显示缩放控件
        return options;
    }

    @Override
    public void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        MapView.setMapCustomEnable(false);
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void moveToPosition(double lat, double lng, float zoom) {
        MapStatusUpdate msu;
        if (zoom == 0) {
            msu = MapStatusUpdateFactory.newLatLng(new LatLng(lat, lng));
        } else {
            msu = MapStatusUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom);
        }
        mBaiduMap.setMapStatus(msu);
    }

    @Override
    public Object addMarker(MarkerBean markerBean) {
        if (markerBean != null) {
            return mBaiduMap.addOverlay(getMarkerOptions(markerBean));
        }
        return null;
    }


    @Override
    public void setMarker(Object marker, MarkerBean markerBean) {
        if (marker != null) {
            Marker m = (Marker) marker;
//            m.set(getMarkerOptions(markerBean));
        }
    }

    @Override
    public boolean removeMarker(Object marker) {
        if (marker != null) {
            Marker m = (Marker) marker;
//            if (!m.isRemoved()) {
//                m.remove();
//            }
//            return m.isRemoved();
        }
        return false;
    }

    @Override
    public Object addLine(LineBean lineBean) {
        switch (lineBean.getType()) {
            case LINE:
                return mBaiduMap.addOverlay(getLineOptions(lineBean));
            case CIRCLE:
                return mBaiduMap.addOverlay(getCircleOptions(lineBean));
        }
        return null;
    }

    @Override
    public void setLine(Object line, LineBean lineBean) {
        if (line != null) {
            switch (lineBean.getType()) {
                case LINE:
                    Polyline m = (Polyline) line;
//                    m.setOptions(getLineOptions(lineBean));
                    break;
                case CIRCLE:
                    Circle circle = (Circle) line;
                    break;
            }
        }
    }

    @Override
    public void removeLine(Object line, LineType lineType) {
        if (line != null) {
            switch (lineType) {
                case LINE:
                    Polyline m = (Polyline) line;
                    m.remove();
                    break;
                case CIRCLE:
                    Circle circle = (Circle) line;
                    circle.remove();
                    break;
            }
        }
    }

    private MarkerOptions getMarkerOptions(MarkerBean markerBean) {
        MarkerOptions markerOptions = new MarkerOptions();
        if (markerBean.getLat() != 0 && markerBean.getLng() != 0) {
            markerOptions.position(new LatLng(markerBean.getLat(), markerBean.getLng()));
        } else {
            Log.e("hl_error", "经纬度异常");
            return null;
        }
        if (markerBean.getImgSource() != 0) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(markerBean.getImgSource()));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.fromPath(markerBean.getImg()));
        }
        return markerOptions;
    }

    private PolylineOptions getLineOptions(LineBean lineBean) {
        PolylineOptions polylineOptions = new PolylineOptions();
        List<LatLng> latLngList = new ArrayList<>();
        for (int i = 0; i < lineBean.getLatlngs().size(); i++) {
            latLngList.add(new LatLng(lineBean.getLatlngs().get(i).getLat(), lineBean.getLatlngs().get(i).getLng()));
        }
        polylineOptions.points(latLngList).width(lineBean.getWidth()).color(lineBean.getColor1());
        return polylineOptions;
    }

    private CircleOptions getCircleOptions(LineBean lineBean) {
        CircleOptions circleOptions = new CircleOptions();
        if (lineBean.getLat() != 0 && lineBean.getLng() != 0) {
            circleOptions.center(new LatLng(lineBean.getLat(), lineBean.getLng()));
        } else {
            Log.e("hl_error", "经纬度异常");
            return null;
        }
        circleOptions.radius(lineBean.getRadius()).fillColor(lineBean.getColor2());
        return circleOptions;
    }


}
