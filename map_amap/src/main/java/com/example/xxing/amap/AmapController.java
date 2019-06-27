package com.example.xxing.amap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_map.MapController;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.MarkerBean;
import x.com.hl_map.listener.OnCameraChangeListener;

public class AmapController extends MapController {
    private AMap aMap;
    private MapView mMapView;

    @Override
    public void initMap() {

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
        mUiSettings.setScaleControlsEnabled(true);//显示比例尺
        mUiSettings.setMyLocationButtonEnabled(false);//不显示定位按钮
        mUiSettings.setZoomControlsEnabled(false);//不显示缩放控件
        mUiSettings.setCompassEnabled(false);//显示指南针
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
    public void moveToPosition(double lat, double lng, float zoom) {
        if (zoom == 0) {
            aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        } else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        }
    }

    @Override
    public Object addMarker(MarkerBean markerBean) {
        if (markerBean != null) {
            return aMap.addMarker(getMarkerOptions(markerBean));
        }
        return null;
    }


    @Override
    public void setMarker(Object marker, MarkerBean markerBean) {
        if (marker != null) {
            Marker m = (Marker) marker;
            m.setMarkerOptions(getMarkerOptions(markerBean));
        }
    }

    @Override
    public boolean removeMarker(Object marker) {
        if (marker != null) {
            Marker m = (Marker) marker;
            if (!m.isRemoved()) {
                m.remove();
            }
            return m.isRemoved();
        }
        return false;
    }

    @Override
    public Object addLine(LineBean lineBean) {
        switch (lineBean.getType()) {
            case LINE:
                return aMap.addPolyline(getLineOptions(lineBean));
            case CIRCLE:
                return aMap.addCircle(getCircleOptions(lineBean));
        }
        return null;
    }

    @Override
    public void setLine(Object line, LineBean lineBean) {
        if (line != null) {
            switch (lineBean.getType()) {
                case LINE:
                    Polyline m = (Polyline) line;
                    m.setOptions(getLineOptions(lineBean));
                    break;
                case CIRCLE:
                    Circle circle = (Circle) line;
                    break;
            }
        }
    }

    @Override
    public void removeLine(Object line, MapController.LineType lineType) {
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
        polylineOptions.addAll(latLngList).width(lineBean.getWidth()).color(lineBean.getColor1());
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
        circleOptions.radius(lineBean.getRadius()).strokeColor(lineBean.getColor1()).fillColor(lineBean.getColor2());
        return circleOptions;
    }


}
