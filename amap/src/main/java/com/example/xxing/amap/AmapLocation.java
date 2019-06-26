package com.example.xxing.amap;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import x.com.hl_log.ViseLog;
import x.com.hl_map.LocationController;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.listener.OnLocationListener;

public class AmapLocation extends LocationController {
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;

    @Override
    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void initLocation(Context context, final OnLocationListener onLocationListener) {
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                ViseLog.d(aMapLocation);
                if (aMapLocation.getErrorCode() == 0) {
                    if (onLocationListener != null) {
                        LocationBean locationBean = new LocationBean(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        onLocationListener.onCurrentLocation(locationBean);
                    }
                } else {
                    Log.e("hl_error", aMapLocation.getErrorInfo());
                    if (onLocationListener != null) {
                        onLocationListener.onError(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }
}
