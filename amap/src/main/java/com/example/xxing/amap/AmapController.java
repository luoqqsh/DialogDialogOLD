package com.example.xxing.amap;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import x.com.hl_map.MapController;

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
    public void addMarker(double lat, double lng, String bitmapSource) {
        Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromPath(bitmapSource)));
    }

    @Override
    public void addMarker(double lat, double lng, int bitmapSource) {
        aMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(bitmapSource)));
    }
}
