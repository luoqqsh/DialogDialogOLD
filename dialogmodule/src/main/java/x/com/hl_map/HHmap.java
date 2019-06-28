package x.com.hl_map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.LineType;
import x.com.hl_map.bean.MapType;
import x.com.hl_map.listener.OnCameraChangeListener;

public class HHmap {
    private MapController mapController;
    private MapType mapType;
    private Context context;

    public HHmap(Context context, MapType mapType, MapController mapController) {
        this.mapType = mapType;
        this.context = context;
        this.mapController = mapController;
        mapController.initMap(context.getApplicationContext());
    }

    public void addMapView(ViewGroup viewGroup, Bundle bundle) {
        addMapView(viewGroup, bundle, null);
    }

    public void addMapView(ViewGroup viewGroup, Bundle bundle, OnCameraChangeListener onCameraChangeListener) {
        mapController.addMapView(context, viewGroup, bundle, onCameraChangeListener);
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

    public Object addMarker(LineBean markerBean) {
        return mapController.addMarker(markerBean);
    }

    public void setMarker(Object marker, LineBean markerBean) {
        mapController.setMarker(marker, markerBean);
    }

    public boolean removeMarker(Object marker, LineType lineType) {
        return mapController.removeMarker(marker,lineType);
    }
}
