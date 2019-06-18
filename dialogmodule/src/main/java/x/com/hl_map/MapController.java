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

public abstract class MapController {

    public ViewGroup mapViewGroup;

    public abstract void initMap(Context context);

    public void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState) {
        addMapView(context, viewGroup, savedInstanceState, null);
    }

    public abstract void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState, OnCameraChangeListener onCameraChangeListener);

    public abstract void addOtherMap(String map, boolean isOpen);

    public abstract void onDestroy();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onSaveInstanceState(Bundle outState);

    public abstract void moveToPosition(LatLngBean latLngBean, float zoom);

    public abstract void moveToPosition(LatLngBean latLngBean1, LatLngBean latLngBean2, int padding);

    public abstract Object addMarker(LineBean markerBean);

    public abstract void setMarker(Object marker, LineBean markerBean);

    public abstract boolean removeMarker(Object line, LineType lineType);

    public abstract void routeSearch(RouteSearchType routeSearchType, LatLngBean from, LatLngBean to, String city, OnLocationListener onLocationListener);

}
