package x.com.hl_map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

public abstract class MapController {
    public ViewGroup mapViewGroup;

    public abstract void initMap();

    public abstract void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState);

    public abstract void onDestroy();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onSaveInstanceState(Bundle outState);

    public abstract void moveToPosition(double lat, double lon, float zoom);

    public abstract void addMarker(double lat, double lon, String bitmapSource);

    public abstract void addMarker(double lat, double lon, int bitmapSource);
}
