package com.example.xxing.amap;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import x.com.hl_map.HHconfig;
import x.com.hl_map.LocationController;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.listener.OnLocationListener;

public class AmapLocation extends LocationController {
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    private AMapLocationListener mLocationListener;

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
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (onLocationListener != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        LocationBean locationBean = new LocationBean(LocationFrom.LOCATION, new LatLngBean(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                        locationBean.setZoom(locationBean.getZoom());
                        locationBean.setAddress(locationBean.getAddress());
                        locationBean.setBearing(locationBean.getBearing());
                        locationBean.setSpeed(locationBean.getSpeed());
                        locationBean.setDescription(locationBean.getDescription());
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        Log.e("hl_error", aMapLocation.getErrorCode() + "  " + aMapLocation.getErrorInfo());
                        onLocationListener.onError(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption option = new AMapLocationClientOption();
        switch (HHconfig.locationMode) {
            case Battery_Saving:
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
                break;
            case Device_Sensors:
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
                break;
            case Hight_Accuracy:
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                break;
        }
        //是否单次定位
        option.setOnceLocation(HHconfig.isOnceLocation);
        //定位时间间隔，默认2000ms，最低1000ms
        option.setInterval(HHconfig.Interval);
        //缓存机制，定位信息缓存，GPS不缓存
        option.setLocationCacheEnable(HHconfig.isLocationCacheEnable);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(HHconfig.HttpTimeOut);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        option.setMockEnable(HHconfig.isMockEnable);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(HHconfig.isNeedAddress);
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
//        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        mLocationClient.setLocationOption(option);
    }

    @Override
    public void releaseLocationListener() {
        if (mLocationClient != null && mLocationListener != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
        }

    }
}
