package x.com.hl_map;


import android.content.Context;

import x.com.hl_map.bean.MapType;
import x.com.hl_map.listener.OnLocationListener;

public class HHLocation {
    private Context context;
    private LocationController locationController;
    private MapType mapType;

    public HHLocation(Context context, MapType mapType, LocationController locationController) {
        this.locationController = locationController;
        this.mapType = mapType;
        this.context = context;
    }

    public void setOnLocationListener(OnLocationListener onLocationListener) {
        locationController.initLocation(context.getApplicationContext(), onLocationListener);
    }

    public void startLocation() {
        locationController.startLocation();
    }

    public void stopLocation() {
        locationController.stopLocation();
    }

    public void releaseLocationListener() {
        locationController.releaseLocationListener();
    }
}
