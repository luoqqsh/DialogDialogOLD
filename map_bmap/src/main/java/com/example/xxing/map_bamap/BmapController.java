package com.example.xxing.map_bamap;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

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
import x.com.hl_map.listener.OnLocationListener;
import x.com.hl_map.listener.OnMapReadyListener;

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
    public void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState, final OnCameraChangeListener onCameraChangeListener, OnMapReadyListener onMapReadyListener) {
        mMapView = new MapView(context, getMapOption());
        mMapView.onCreate(context, savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mMapView, params);
        mBaiduMap = mMapView.getMap();
        if (onMapReadyListener != null) {
            onMapReadyListener.onReady();
        }
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

    @Override
    public void addOtherMap(String map, boolean isOpen) {

    }

    private BaiduMapOptions getMapOption() {
        BaiduMapOptions options = new BaiduMapOptions();
        options.scaleControlEnabled(HHconfig.isScaleControlsEnabled);// 设置是否显示比例尺控件
        options.zoomControlsEnabled(HHconfig.isZoomControlsEnabled);// 设置是否显示缩放控件
        options.compassEnabled(HHconfig.isCompassEnabled);
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
    public void moveToPosition(LatLngBean latLngBean, float zoom) {
        MapStatusUpdate msu;
        if (zoom == 0) {
            msu = MapStatusUpdateFactory.newLatLng(new LatLng(latLngBean.getLat(), latLngBean.getLng()));
        } else {
            msu = MapStatusUpdateFactory.newLatLngZoom(new LatLng(latLngBean.getLat(), latLngBean.getLng()), zoom);
        }
        mBaiduMap.setMapStatus(msu);
    }

    @Override
    public void moveToPosition(LatLngBean latLngBean1, LatLngBean latLngBean2, int padding) {

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
                    Overlay line = mBaiduMap.addOverlay(new PolylineOptions().points(latLngs));
                    setLineOption(line, markerBean);
                    return line;
                case CIRCLE:
                    if (!markerBean.isRaduis() || !markerBean.isPosition()) return null;
                    Overlay circle = mBaiduMap.addOverlay(new CircleOptions().center(new LatLng(markerBean.getLat(), markerBean.getLng())).radius(markerBean.getRadius()));
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
                    Overlay marker = mBaiduMap.addOverlay(new MarkerOptions().position(new LatLng(markerBean.getLat(), markerBean.getLng())).icon(bitmapDescriptor));
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
                    marker.remove();
                    break;

            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void routeSearch(RouteSearchType routeSearchType, LatLngBean from, LatLngBean to, String city, OnLocationListener locationListener) {
        if (locationListener == null) return;
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
        PlanNode stNode, enNode;
        if (TextUtils.isEmpty(from.getAddress()) || TextUtils.isEmpty(to.getAddress())) {
            stNode = PlanNode.withLocation(new LatLng(from.getLat(), from.getLng()));
            enNode = PlanNode.withLocation(new LatLng(to.getLat(), to.getLng()));
        } else {
            stNode = PlanNode.withCityNameAndPlaceName(from.getCity(), from.getAddress());
            enNode = PlanNode.withCityNameAndPlaceName(to.getCity(), to.getAddress());
        }

        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
        switch (routeSearchType) {
            case CAR:
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case BIKE:
                mSearch.bikingSearch((new BikingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case BUS:
                mSearch.transitSearch(new TransitRoutePlanOption().from(stNode).to(enNode));
                break;
            case WALK:
                mSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
                break;
        }
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
