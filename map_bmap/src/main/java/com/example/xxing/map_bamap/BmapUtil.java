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
}
