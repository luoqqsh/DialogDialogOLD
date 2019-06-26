package x.com.hl_map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import x.com.hl_map.bean.LineBean;
import x.com.hl_map.bean.MarkerBean;
import x.com.hl_map.listener.OnCameraChangeListener;

public abstract class MapController {
    public enum LineType {
        LINE,
        CIRCLE,
    }

    public ViewGroup mapViewGroup;

    public abstract void initMap();

    public void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState) {
        addMapView(context, viewGroup, savedInstanceState, null);
    }

    public abstract void addMapView(Context context, ViewGroup viewGroup, Bundle savedInstanceState, OnCameraChangeListener onCameraChangeListener);

    public abstract void onDestroy();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onSaveInstanceState(Bundle outState);

    public abstract void moveToPosition(double lat, double lon, float zoom);

    public abstract Object addMarker(MarkerBean markerBean);

    public abstract void setMarker(Object marker, MarkerBean markerBean);

    public abstract boolean removeMarker(Object marker);

    public abstract Object addLine(LineBean markerBean);

    public abstract void setLine(Object marker, LineBean markerBean);

    public abstract void removeLine(Object line, MapController.LineType lineType);
}
