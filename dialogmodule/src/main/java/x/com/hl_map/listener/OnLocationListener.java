package x.com.hl_map.listener;

import x.com.hl_map.bean.LocationBean;

public interface OnLocationListener {
    void onCurrentLocation(LocationBean latLngs);

    void onError(int code, String errorInfo);
}
