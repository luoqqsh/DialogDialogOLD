package com.example.xxing.map_qmap;


import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlayOptions;
import com.tencent.tencentmap.mapsdk.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import x.com.hl_map.HHconfig;
import x.com.hl_map.MapController;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.LineType;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.MapType;
import x.com.hl_map.bean.RouteSearchType;
import x.com.hl_map.listener.OnCameraChangeListener;
import x.com.hl_map.listener.OnLocationListener;
import x.com.hl_map.listener.OnMapReadyListener;

public class QmapController extends MapController {
    private TencentMap qMap;
    private MapView mMapView;
    private Context context;
    private TileOverlay tileOverlay;
    private UrlTileProvider tileProvider;

    @Override
    public void initMap(Context context) {
        this.context = context;
    }

    @Override
    public void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState, final OnCameraChangeListener onCameraChangeListener, OnMapReadyListener onMapReadyListener) {
        mMapView = new MapView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mMapView, params);
        qMap = mMapView.getMap();
        if (onMapReadyListener != null) {
            onMapReadyListener.onReady();
        }
        if (onCameraChangeListener != null) {
            qMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    LocationBean latLngs = new LocationBean(MapType.QQMAP,LocationFrom.MOVEMAP, new LatLngBean(cameraPosition.target.latitude, cameraPosition.target.longitude));
                    latLngs.setZoom(cameraPosition.zoom);
                    onCameraChangeListener.onCameraChange(latLngs);
                }

                @Override
                public void onCameraChangeFinished(CameraPosition cameraPosition) {
                    LocationBean latLngs = new LocationBean(MapType.QQMAP,LocationFrom.MOVEMAP_FINISH, new LatLngBean(cameraPosition.target.latitude, cameraPosition.target.longitude));
                    latLngs.setZoom(cameraPosition.zoom);
                    onCameraChangeListener.onCameraChange(latLngs);
                }
            });
        }
        setMapOption();
    }

    @Override
    public void addOtherMap(final String map, boolean isOpen) {
        if (isOpen) {
            if (tileProvider == null) {
                tileProvider = new UrlTileProvider(256, 256) {
                    @Override
                    public URL getTileUrl(int x, int y, int zoom) {
                        try {
                            return new URL(String.format(map, x, y, zoom));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }
            tileOverlay = qMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider)
                    .diskCacheDir("/storage/emulated/0/qmap/cache"));
        } else {
            if (tileOverlay != null) {
                tileOverlay.remove();
            }
        }
    }


    private void setMapOption() {
        UiSettings mUiSettings = qMap.getUiSettings();
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
    }

    @Override
    public void moveToPosition(LatLngBean latLngBean, float zoom) {
        if (zoom == 0) {
            qMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLngBean.getLat(), latLngBean.getLng())));
        } else {
            qMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngBean.getLat(), latLngBean.getLng()), zoom));
        }
    }

    @Override
    public void moveToPosition(LatLngBean latLngBean1, LatLngBean latLngBean2, int padding) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        b.include(new LatLng(latLngBean1.getLat(), latLngBean1.getLng()));
        b.include(new LatLng(latLngBean2.getLat(), latLngBean2.getLng()));
        qMap.animateCamera(CameraUpdateFactory.newLatLngBounds(b.build(), padding));
    }

    @Override
    public Object addMarker(LineBean markerBean) {
        if (markerBean != null) {
            switch (markerBean.getType()) {
                case LINE:
                    if (!markerBean.isPositionList()) return null;
                    List<LatLng> latLngs = new ArrayList<>();
                    for (int i = 0; i < markerBean.getLatlngs().size(); i++) {
                        latLngs.add(new LatLng(markerBean.getLatlngs().get(i).getLat(), markerBean.getLatlngs().get(i).getLng()));
                    }
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngs);
                    if (markerBean.isColorList()) {
//                        polylineOptions.colorValues(markerBean.getColorList());
                    }
                    Polyline line = qMap.addPolyline(polylineOptions);
                    setLineOption(line, markerBean);
                    return line;
                case CIRCLE:
                    if (!markerBean.isRaduis() || !markerBean.isPosition()) return null;
                    Circle circle = qMap.addCircle(new CircleOptions().center(new LatLng(markerBean.getLat(), markerBean.getLng())).radius(markerBean.getRadius()));
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
                    Marker marker = qMap.addMarker(new MarkerOptions(new LatLng(markerBean.getLat(), markerBean.getLng())).icon(bitmapDescriptor));
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
                    return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void routeSearch(final RouteSearchType routeSearchType, final LatLngBean from, final LatLngBean to, final String city, final OnLocationListener onLocationListener) {
        if (onLocationListener == null) return;
//        final RouteSearch routeSearch = new RouteSearch(context);
//        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
//            @Override
//            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
//                if (i == 1000) {
//                } else {
//                    onLocationListener.onError(i, ErrorType.OTHER.getValue());
//                }
//                ViseLog.d(busRouteResult);
//            }
//
//            @Override
//            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
//                ViseLog.d(driveRouteResult);
//                if (i == 1000) {
//                    LocationBean locationBean = new LocationBean(LocationFrom.DRIVEROUTE, null);
//                    if (driveRouteResult.getPaths().size() > 0) {
//                        List<LatLngBean> latLngBeans = new ArrayList<>();
//                        List<Integer> colorList = new ArrayList<>();
//                        List<DriveStep> items = driveRouteResult.getPaths().get(0).getSteps();
//                        for (int j = 0; j < items.size(); j++) {
//                            List<LatLonPoint> steps = items.get(j).getPolyline();
//                            for (int k = 0; k < steps.size(); k++) {
//                                latLngBeans.add(new LatLngBean(steps.get(k).getLatitude(), steps.get(k).getLongitude()));
//                            }
//                        }
//                        locationBean.setPois(latLngBeans);
//                        locationBean.setColorList(colorList);
//                        onLocationListener.onCurrentLocation(locationBean);
//                    } else {
//                        onLocationListener.onError(ErrorType.NODATA.getCode(), ErrorType.NODATA.getValue());
//                    }
//                } else {
//                    onLocationListener.onError(i, ErrorType.OTHER.getValue());
//                }
//            }
//
//            @Override
//            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
//                if (i == 1000) {
//                } else {
//                    onLocationListener.onError(i, ErrorType.OTHER.getValue());
//                }
//                ViseLog.d(walkRouteResult);
//            }
//
//            @Override
//            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
//                if (i == 1000) {
//                } else {
//                    onLocationListener.onError(i, ErrorType.OTHER.getValue());
//                }
//                ViseLog.d(rideRouteResult);
//            }
//        });
//        switch (routeSearchType) {
//            case CAR:
//                if (TextUtils.isEmpty(from.getAddress()) || TextUtils.isEmpty(to.getAddress())) {
//                    RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(
//                            new RouteSearch.FromAndTo(new LatLonPoint(from.getLat(), from.getLng()), new LatLonPoint(to.getLat(), to.getLng())),
//                            RouteSearch.DRIVEING_PLAN_AVOID_CONGESTION_NO_HIGHWAY, null, null, "");
//                    routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
//                } else {
////                    RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(
////                            new RouteSearch.FromAndTo(from.getAddress(),to.getAddress()),
////                            RouteSearch.DRIVEING_PLAN_AVOID_CONGESTION_NO_HIGHWAY, null, null, "");
////                    routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
//                }
//
//                break;
//            case BUS:
//                RouteSearch.BusRouteQuery busRouteQuery = new RouteSearch.BusRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(from.getLat(), from.getLng()), new LatLonPoint(to.getLat(), to.getLng())), RouteSearch.BUS_DEFAULT, city, 1);
//                routeSearch.calculateBusRouteAsyn(busRouteQuery);
//                break;
//            case BIKE:
//                RouteSearch.RideRouteQuery rideRouteQuery = new RouteSearch.RideRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(from.getLat(), from.getLng()), new LatLonPoint(to.getLat(), to.getLng())));
//                routeSearch.calculateRideRouteAsyn(rideRouteQuery);
//                break;
//            case WALK:
//                RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(from.getLat(), from.getLng()), new LatLonPoint(to.getLat(), to.getLng())));
//                routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
//                break;
//        }
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
                            latLngs.add(new LatLng(lineBean.getLatlngs().get(i).getLat(), lineBean.getLatlngs().get(i).getLng()));
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
