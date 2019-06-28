package x.com.hl_map;

import x.com.hl_map.bean.LocationMode;

public class HHconfig {
    /**
     * 地图
     */
    //显示比例尺
    public static boolean isScaleControlsEnabled = true;
    //显示指南针
    public static boolean isCompassEnabled = false;
    //显示缩放控件
    public static boolean isZoomControlsEnabled = false;
    //定位按钮
    public static boolean isMyLocationButtonEnabled = false;

    /**
     * 定位
     */
    //单次定位
    public static boolean isOnceLocation = false;
    //定位模式
    public static LocationMode locationMode = LocationMode.Hight_Accuracy;
    //定位时间间隔,最少1000，单位ms
    public static int Interval = 1000;
    //缓存机制，定位信息缓存，GPS不缓存
    public static boolean isLocationCacheEnable = false;
    //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
    public static int HttpTimeOut = 20000;
    //设置是否允许模拟位置,默认为true，允许模拟位置
    public static boolean isMockEnable = false;
    //设置是否返回地址信息（默认返回地址信息）
    public static boolean isNeedAddress = true;
    //坐标类型
    //GCJ02：国测局坐标；
    //BD09ll：百度经纬度坐标；
    //BD09：百度墨卡托坐标；
    //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
    public static String coorType = "bd09ll";
    //是否启用GPS，使用高精度和仅用设备两种定位模式的，参数必须设置为true
    public static boolean isOpenGPS = true;
}
