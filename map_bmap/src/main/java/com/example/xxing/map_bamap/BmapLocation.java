package com.example.xxing.map_bamap;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import x.com.hl_log.ViseLog;
import x.com.hl_map.HHconfig;
import x.com.hl_map.LocationController;
import x.com.hl_map.bean.ErrorType;
import x.com.hl_map.bean.LatLngBean;
import x.com.hl_map.bean.LocationBean;
import x.com.hl_map.bean.LocationFrom;
import x.com.hl_map.listener.OnLocationListener;

public class BmapLocation extends LocationController {
    private LocationClient mLocationClient;
    private BDAbstractLocationListener mLocationListener;

    @Override
    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }

    @Override
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    @Override
    public void initLocation(Context context, final OnLocationListener onLocationListener) {
        mLocationListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                ViseLog.d(bdLocation);
                if (onLocationListener != null) {
                    if (bdLocation != null) {
                        LocationBean locationBean = new LocationBean(LocationFrom.LOCATION, new LatLngBean(bdLocation.getLatitude(), bdLocation.getLongitude()));
                        locationBean.setZoom(locationBean.getZoom());
                        locationBean.setAddress(locationBean.getAddress());
                        locationBean.setBearing(locationBean.getBearing());
                        locationBean.setSpeed(locationBean.getSpeed());
                        locationBean.setDescription(locationBean.getDescription());
                        onLocationListener.onCurrentLocation(locationBean);
                    } else {
                        onLocationListener.onError(ErrorType.LOCATIONERROR.getCode(), ErrorType.LOCATIONERROR.getValue());
                    }
                }
            }

        };
        //初始化定位
        mLocationClient = new LocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        //Battery_Saving 低功耗，只是用网络定位
        //Device_Sensors 只是用设备GPS定位
        //Hight_Accuracy 两者都用
        switch (HHconfig.locationMode) {
            case Battery_Saving:
                option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
                break;
            case Device_Sensors:
                option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
                break;
            case Hight_Accuracy:
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                break;
        }
        option.setCoorType(HHconfig.coorType);
        if (HHconfig.isOnceLocation) {
            option.setScanSpan(0);
        } else {
            option.setScanSpan(HHconfig.Interval);
        }
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(HHconfig.isOpenGPS);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(HHconfig.isMockEnable);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void releaseLocationListener() {
        if (mLocationClient != null && mLocationListener != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
        }
    }
}
