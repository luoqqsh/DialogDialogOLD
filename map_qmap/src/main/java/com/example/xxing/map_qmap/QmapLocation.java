package com.example.xxing.map_qmap;


import android.content.Context;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import x.com.hl_map.HHconfig;
import x.com.hl_map.LocationController;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.listener.OnLocationListener;

public class QmapLocation extends LocationController {
    //声明AMapLocationClient类对象
    private TencentLocationManager mLocationManager;
    private TencentLocationListener mLocationListener;

    @Override
    public void startLocation() {
        if (mLocationManager != null) {
//            mLocationManager.startLocation();
        }
    }

    @Override
    public void stopLocation() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    public void initLocation(Context context, final OnLocationListener onLocationListener) {
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mLocationListener = new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
                if (onLocationListener != null) {
//                    if (i == errpr) {
                    LocationBean locationBean = new LocationBean(LocationFrom.LOCATION, new LatLngBean(tencentLocation.getLatitude(), tencentLocation.getLongitude()));
                    locationBean.setZoom(locationBean.getZoom());
                    locationBean.setAddress(locationBean.getAddress());
                    locationBean.setBearing(locationBean.getBearing());
                    locationBean.setSpeed(locationBean.getSpeed());
                    locationBean.setDescription(locationBean.getDescription());
                    onLocationListener.onCurrentLocation(locationBean);
//                    } else {
//                        Log.e("hl_error", i + "  " + s);
//                        onLocationListener.onError(i, s);
//                    }
                }
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {

            }
        };
        //初始化定位
//        mLocationManager = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
//        mLocationManager.setLocationListener(mLocationListener);
        TencentLocationRequest option = TencentLocationRequest.create();
        switch (HHconfig.locationMode) {
            case Battery_Saving:
//                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
                break;
            case Device_Sensors:
//                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
                break;
            case Hight_Accuracy:
//                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                break;
        }
        //是否单次定位
//        option.setOnceLocation(HHconfig.isOnceLocation);
        //定位时间间隔，默认2000ms，最低1000ms
        option.setInterval(HHconfig.Interval);
        //缓存机制，定位信息缓存，GPS不缓存
//        option.setLocationCacheEnable(HHconfig.isLocationCacheEnable);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        option.setHttpTimeOut(HHconfig.HttpTimeOut);
        //设置是否允许模拟位置,默认为true，允许模拟位置
//        option.setMockEnable(HHconfig.isMockEnable);
        //设置是否返回地址信息（默认返回地址信息）
//        option.setNeedAddress(HHconfig.isNeedAddress);
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
//        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
//        mLocationManager.setLocationOption(option);
    }

    @Override
    public void releaseLocationListener() {
        if (mLocationManager != null && mLocationListener != null) {
                mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
