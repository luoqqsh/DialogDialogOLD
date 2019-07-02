package x.com.hl_map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.LineType;
import x.com.hl_map.bean.RouteSearchType;
import x.com.hl_map.listener.OnCameraChangeListener;
import x.com.hl_map.listener.OnLocationListener;
import x.com.hl_map.listener.OnMapReadyListener;

public class HHmap {
    private MapController mapController;
    private Context context;

    public HHmap(Context context,MapController mapController) {
        this.context = context;
        this.mapController = mapController;
        mapController.initMap(context.getApplicationContext());
    }

    public void addMapView(ViewGroup viewGroup, Bundle bundle) {
        addMapView(viewGroup, bundle, null,null);
    }

    public void addMapView(ViewGroup viewGroup, Bundle bundle, OnCameraChangeListener onCameraChangeListener) {
        addMapView(viewGroup, bundle, onCameraChangeListener, null);
    }

    //谷歌地图需要初始化位置的，使用这个方法
    public void addMapView(ViewGroup viewGroup, Bundle bundle, OnCameraChangeListener onCameraChangeListener, OnMapReadyListener onMapReadyListener) {
        mapController.addMapView(context, viewGroup, bundle, onCameraChangeListener, onMapReadyListener);
    }

    public void addGoogleMap(boolean isOpen) {
        String url = "http://mt2.google.cn/vt/lyrs=m@209712068&scale=2&hl=zh-CN&gl=cn&x=%d&y=%d&z=%d";
        mapController.addOtherMap(url, isOpen);
    }

    public void addOtherMap(String url, boolean isOpen) {
        mapController.addOtherMap(url, isOpen);
    }

    public void onDestroy() {
        mapController.onDestroy();
    }

    public void onResume() {
        mapController.onResume();
    }

    public void onPause() {
        mapController.onPause();
    }

    public void onSaveInstanceState(Bundle outState) {
        mapController.onSaveInstanceState(outState);
    }

    public void moveToPosition(LatLngBean latLngBean, float zoom) {
        mapController.moveToPosition(latLngBean, zoom);
    }

    public void moveToPosition(LatLngBean latLngBean1, LatLngBean latLngBean2, int padding) {
        mapController.moveToPosition(latLngBean1, latLngBean2, padding);
    }

    public Object addMarker(LineBean markerBean) {
        return mapController.addMarker(markerBean);
    }

    public void setMarker(Object marker, LineBean markerBean) {
        mapController.setMarker(marker, markerBean);
    }

    public boolean removeMarker(Object marker, LineType lineType) {
        return mapController.removeMarker(marker, lineType);
    }

    public void searchRoute(RouteSearchType routeSearchType, LatLngBean from, LatLngBean to, String city, OnLocationListener onLocationListener) {
        mapController.routeSearch(routeSearchType, from, to, city, onLocationListener);
    }
}
