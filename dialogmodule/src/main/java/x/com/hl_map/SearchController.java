package x.com.hl_map;

import android.content.Context;

import x.com.hl_map.bean.LocationType;
import x.com.hl_map.listener.OnLocationListener;

public abstract class SearchController {
    /**
     * 地址转坐标
     *
     * @param context  上下文
     * @param address  地址
     * @param cityCode 查询城市，中文或者中文全拼，citycode、adcode
     */
    public abstract void address2Point(Context context, String address, String cityCode, OnLocationListener onLocationListener);

    /**
     * 坐标转地址
     *
     * @param context            上下文
     * @param lat                纬度
     * @param lng                经度
     * @param scope              距离
     * @param type               坐标类型
     * @param onLocationListener 回调
     */
    public abstract void point2address(Context context, double lat, double lng, float scope, LocationType type, OnLocationListener onLocationListener);

    public abstract void poiSearch(Context context, String keyword, String type, String cityCode, int page, int pagesize, OnLocationListener onLocationListener);

    public abstract void showTips(Context context, String address, String cityCode, boolean isCityLimit, OnLocationListener onLocationListener);

}
