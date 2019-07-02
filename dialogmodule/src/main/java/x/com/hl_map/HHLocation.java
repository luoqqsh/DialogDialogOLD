package x.com.hl_map;

import android.content.Context;

import x.com.hl_map.listener.OnLocationListener;

public class HHLocation {
    private Context context;
    private LocationController locationController;

    public HHLocation(Context context, LocationController locationController) {
        this.locationController = locationController;
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
