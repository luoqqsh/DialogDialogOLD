package x.com.hl_map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

public class HHmap {
    private MapController mapController;
    private MapType mapType;
    private Context context;

    public enum MapType {
        AMAP,
        BAIDUMAP,
        GOOGLEMAP,
        QQMAP
    }


    public HHmap(Context context, MapType mapType, MapController mapController) {
        this.mapType = mapType;
        this.context = context;
        this.mapController = mapController;
        mapController.initMap();
    }

    public void addMapView(ViewGroup viewGroup, Bundle bundle) {
        mapController.addMapView(context, viewGroup, bundle);
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

    public void moveToPosition(double lat, double lng, float zoom) {
        mapController.moveToPosition(lat, lng, zoom);
    }

    public void addMarker(double lat, double lng, int bitmapSource) {
        mapController.addMarker(lat, lng, bitmapSource);
    }

    public void addMarker(double lat, double lng, String bitmapSource) {
        mapController.addMarker(lat, lng, bitmapSource);
    }
}
