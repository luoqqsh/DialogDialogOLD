package com.example.xxing.map_bamap;


import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import x.com.hl_map.bean.LatLngBean;

public class BmapUtil {
    /**
     * 判断位置所在区域,纬度和经度均为高德坐标系。
     *
     * @param lat 纬度
     * @param lng 经度
     * @return 返回true代表当前位置在大陆、港澳地区，反之不在。
     */
    public static boolean isAMapDataAvailable(double lat, double lng) {
        return false;
    }

    /**
     * 其他地图转高德地图坐标
     *
     * @param context 上下文
     * @param lat     纬度
     * @param lng     经度
     * @return 转换后的坐标
     */
    public static LatLngBean other2Gaode(Context context, double lat, double lng) {
        CoordinateConverter converter = new CoordinateConverter();
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(new LatLng(lat, lng));
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return new LatLngBean(desLatLng.latitude, desLatLng.longitude);
    }
}
