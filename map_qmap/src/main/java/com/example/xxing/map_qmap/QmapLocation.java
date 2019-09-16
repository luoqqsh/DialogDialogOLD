package com.example.xxing.map_qmap;


import android.content.Context;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import x.com.hl_map.HHconfig;
import x.com.hl_map.LocationController;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.bean.MapType;
import x.com.hl_map.listener.OnLocationListener;

public class QmapLocation extends LocationController {
    //声明AMapLocationClient类对象
    private TencentLocationManager mLocationManager;
    private TencentLocationListener mLocationListener;
    private TencentLocationRequest option;

    @Override
    public void startLocation() {
        if (mLocationManager != null && mLocationListener != null && option != null) {
            mLocationManager.requestLocationUpdates(option, mLocationListener);
        }
    }

    @Override
    public void stopLocation() {
        if (mLocationManager != null && mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    public void initLocation(Context context, final OnLocationListener onLocationListener) {
        mLocationManager = TencentLocationManager.getInstance(context);
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mLocationListener = new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
                if (onLocationListener != null) {
                    if (i == TencentLocation.ERROR_OK) {
                        LocationBean locationBean = new LocationBean(MapType.QQMAP,LocationFrom.LOCATION, new LatLngBean(tencentLocation.getLatitude(), tencentLocation.getLongitude()));
                        locationBean.setAddress(tencentLocation.getAddress());
                        locationBean.setBearing(tencentLocation.getBearing());
                        locationBean.setSpeed(tencentLocation.getSpeed());
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        Log.e("hl_error", i + "  " + s);
                        onLocationListener.onError(i, s);
                    }
                }
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {

            }
        };

        option = TencentLocationRequest.create();
        //定位时间间隔，默认2000ms，最低1000ms
        option.setInterval(HHconfig.Interval);
    }

    @Override
    public void releaseLocationListener() {
        if (mLocationManager != null && mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
