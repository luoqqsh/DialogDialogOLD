package com.example.xxing.amap;

import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_map.HHconfig;
import x.com.hl_map.MapController;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.LineType;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.RouteSearchType;
import x.com.hl_map.listener.OnCameraChangeListener;

public class AmapController extends MapController {
    private AMap aMap;
    private MapView mMapView;
    private Context context;

    @Override
    public void initMap(Context context) {
        this.context = context;
    }

    @Override
    public void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState, final OnCameraChangeListener onCameraChangeListener) {
        mMapView = new MapView(context);
        mMapView.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mMapView, params);
        aMap = mMapView.getMap();
        if (onCameraChangeListener != null) {
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    LocationBean latLngs = new LocationBean(LocationFrom.MOVEMAP, new LatLngBean(cameraPosition.target.latitude, cameraPosition.target.longitude));
                    latLngs.setZoom(cameraPosition.zoom);
                    onCameraChangeListener.onCameraChange(latLngs);
                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    LocationBean latLngs = new LocationBean(LocationFrom.MOVEMAP_FINISH, new LatLngBean(cameraPosition.target.latitude, cameraPosition.target.longitude));
                    latLngs.setZoom(cameraPosition.zoom);
                    onCameraChangeListener.onCameraChange(latLngs);
                }
            });
        }
        setMapOption();
    }

    private void setMapOption() {
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setScaleControlsEnabled(HHconfig.isScaleControlsEnabled);//显示比例尺
        mUiSettings.setMyLocationButtonEnabled(HHconfig.isMyLocationButtonEnabled);//不显示定位按钮
        mUiSettings.setZoomControlsEnabled(HHconfig.isZoomControlsEnabled);//不显示缩放控件
        mUiSettings.setCompassEnabled(HHconfig.isCompassEnabled);//显示指南针
    }

    @Override
    public void onDestroy() {
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
    public void moveToPosition(LatLngBean latLngBean, float zoom) {
        if (zoom == 0) {
            aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLngBean.getLat(), latLngBean.getLng())));
        } else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngBean.getLat(), latLngBean.getLng()), zoom));
        }
    }

    @Override
    public void moveToPosition(LatLngBean latLngBean1, LatLngBean latLngBean2, float zoom) {

    }

    @Override
    public Object addMarker(LineBean markerBean) {
        if (markerBean != null) {
            switch (markerBean.getType()) {
                case LINE:
                    if (!markerBean.isPositionList()) return null;
                    List<LatLng> latLngs = new ArrayList<>();
                    for (int i = 0; i < markerBean.getLatlngs().size(); i++) {
                        latLngs.add(new LatLng(markerBean.getLat(), markerBean.getLng()));
                    }
                    Polyline line = aMap.addPolyline(new PolylineOptions().addAll(latLngs));
                    setLineOption(line, markerBean);
                    return line;
                case CIRCLE:
                    if (!markerBean.isRaduis() || !markerBean.isPosition()) return null;
                    Circle circle = aMap.addCircle(new CircleOptions().center(new LatLng(markerBean.getLat(), markerBean.getLng())).radius(markerBean.getRadius()));
                    setLineOption(circle, markerBean);
                    return circle;
                case MARKER:
                    BitmapDescriptor bitmapDescriptor = null;
                    if (markerBean.isImg()) {//图片路径
                        bitmapDescriptor = BitmapDescriptorFactory.fromPath(markerBean.getImg());
                    }
                    if (markerBean.isImgResource()) {//图片id
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(markerBean.getImgSource());
                    }
                    if (bitmapDescriptor == null || !markerBean.isPosition()) return null;
                    Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(markerBean.getLat(), markerBean.getLng())).icon(bitmapDescriptor));
                    setLineOption(marker, markerBean);
                    return marker;
            }
        }
        return null;
    }


    @Override
    public void setMarker(Object marker, LineBean markerBean) {
        setLineOption(marker, markerBean);
    }

    @Override
    public boolean removeMarker(Object line, LineType lineType) {
        if (line != null) {
            switch (lineType) {
                case LINE:
                    Polyline polyline = (Polyline) line;
                    polyline.remove();
                    break;
                case CIRCLE:
                    Circle circle = (Circle) line;
                    circle.remove();
                    break;
                case MARKER:
                    Marker marker = (Marker) line;
                    if (!marker.isRemoved()) {
                        marker.remove();
                    }
                    return marker.isRemoved();
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void routeSearch(RouteSearchType routeSearchType, LatLngBean from, LatLngBean to, LocationListener locationListener) {
        if (locationListener == null) return;
        RouteSearch routeSearch = new RouteSearch(context);
        if (TextUtils.isEmpty(from.getAddress()) || TextUtils.isEmpty(to.getAddress())) {
        } else {
        }
        RouteSearch.DriveRouteQuery query = null;
        switch (routeSearchType) {
            case CAR:
//                query = new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(), drivingMode, null, null, "");
                break;
        }
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
        routeSearch.calculateDriveRouteAsyn(query);
    }

    private void setLineOption(Object line, LineBean lineBean) {
        if (line != null) {
            switch (lineBean.getType()) {
                case LINE:
                    Polyline l = (Polyline) line;
                    if (lineBean.isWidth()) {//宽度
                        l.setWidth(lineBean.getWidth());
                    }
                    if (lineBean.isColor1()) {//颜色
                        l.setColor(lineBean.getColor1());
                    }
                    if (lineBean.isPositionList()) {//坐标集合
                        List<LatLng> latLngs = new ArrayList<>();
                        for (int i = 0; i < lineBean.getLatlngs().size(); i++) {
                            latLngs.add(new LatLng(lineBean.getLat(), lineBean.getLng()));
                        }
                        l.setPoints(latLngs);
                    }

                    break;
                case CIRCLE:
                    Circle c = (Circle) line;
                    if (lineBean.isPosition()) {//圆心坐标
                        c.setCenter(new LatLng(lineBean.getLat(), lineBean.getLng()));
                    }
                    if (lineBean.isRaduis()) {//半径
                        c.setRadius(lineBean.getRadius());
                    }
                    break;
                case MARKER:
                    Marker m = (Marker) line;
                    if (lineBean.isPosition()) {//位置
                        m.setPosition(new LatLng(lineBean.getLat(), lineBean.getLng()));
                    }
                    if (lineBean.isImg()) {//图片路径
                        m.setIcon(BitmapDescriptorFactory.fromPath(lineBean.getImg()));
                    }
                    if (lineBean.isImgResource()) {//图片id
                        m.setIcon(BitmapDescriptorFactory.fromResource(lineBean.getImgSource()));
                    }
                    break;
            }
        }
    }
}
