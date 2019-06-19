package com.example.xxing.amap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import x.com.hl_map.MapController;
import x.com.hl_map.MarkerBean;

public class AmapController extends MapController {
    private AMap aMap;
    private MapView mMapView;

    @Override
    public void initMap() {

    }

    @Override
    public void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState) {
        mMapView = new MapView(context);
        mMapView.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mMapView, params);
        aMap = mMapView.getMap();

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
}
