package x.com.hl_map;

import android.content.Context;

import x.com.hl_map.listener.OnLocationListener;

public abstract class LocationController {

    public abstract void startLocation();

    public abstract void stopLocation();

    public abstract void initLocation(Context context, OnLocationListener onLocationListener);
}
