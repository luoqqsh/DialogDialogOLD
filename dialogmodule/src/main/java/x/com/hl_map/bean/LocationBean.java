package x.com.hl_map.bean;

import java.util.List;

public class LocationBean {
    private int errorCode;
    private String errorInfo;
    private LocationFrom from;

    private LatLngBean latLngBean;
    private String address;//地址，当类型为AMapLocation.LOCATION_TYPE_GPS时也可以返回地址信息(需要网络通畅，第一次有可能没有地址信息返回）
    private String description;//获取位置语义信息
    private float zoom;//缩放
    private List<LatLngBean> pois;

    private float bearing;//方向角。定位类型为AMapLocation.LOCATION_TYPE_GPS时，方向角指的是运动方向。
    //定位类型不是AMapLocation.LOCATION_TYPE_GPS时，方向角指的是手机朝向
    //AMapLocationClientOption.setSensorEnable(boolean),是否通过手机传感器返回方向角
    private double altitude;//海拔高度，单位米。
    // 当定位类型不是AMapLocation.LOCATION_TYPE_GPS时，可以通过 AMapLocationClientOption.setSensorEnable(boolean) 控制是否返回海拔高度
    // 当设置为true时会通过手机传感器获取海拔高度,如果手机没有对应的传感器会返回0.0
    private float speed;//获取当前速度(单位：米/秒)
    //，当定位类型不是AMapLocation.LOCATION_TYPE_GPS时，可以通过 AMapLocationClientOption.setSensorEnable(boolean) 控制是否返回速度值，
    // 当设置为true时会通过手机传感器获取速度,如果手机没有对应的传感器会返回0.0

    private String province;//省份
    private String city;//城市
    private String district;//区
    private String cityCode;//城市代码，厦门0592
    private String adCode;//行政区编码，厦门350203
    private String country;//国家
    private String poiName;//获取兴趣点名称
    private String street;//街道（road已过时，使用street）
    private String streetNum;//门牌号

    private String aoiName;//兴趣面名称
    private String conScenario;//室内外置信度 室内：且置信度取值在[1 ～ 100]，值越大在在室内的可能性越大 室外：且置信度取值在[-100 ～ -1] ,值越小在在室内的可能性越大 无法识别室内外：置信度返回值为 0
    private String coordType;//坐标系类型,国外定位时返回的是WGS84坐标系

    private String buildingId;//支持室内定位的建筑物ID信息
    private String floor;//获取室内定位的楼层信息

    private float accuracy;//定位经度，单位米
    private String gpsAccuracyStatus;//获取卫星信号强度，仅在卫星定位时有效,值为 #GPS_ACCURACY_BAD，#GPS_ACCURACY_GOOD，#GPS_ACCURACY_UNKNOWN
    private String locationQualityReport;//获取定位质量
    private String locationType;//获取定位结果来源
    private String provider;//获取定位提供者
    private String satellites;//获取当前可用卫星数量, 仅在卫星定位时有效
    private String locationDetail;//获取定位信息描述
    private int trustedLevel;//获取定位结果的可信度 只有在定位成功时才有意义 非常可信 AMapLocation.TRUSTED_LEVEL_HIGH 可信度一般AMapLocation.TRUSTED_LEVEL_NORMAL 可信度较低 AMapLocation.TRUSTED_LEVEL_LOW 非常不可信 AMapLocation.TRUSTED_LEVEL_BAD


    public LocationBean(LocationFrom from, LatLngBean latLngBean) {
        this(from, latLngBean, "");
    }

    public LocationBean(LocationFrom from, LatLngBean latLngBean, String address) {
        this.latLngBean = latLngBean;
        this.address = address;
        this.from = from;
    }

    public double getLat() {
        return latLngBean.getLat();
    }

    public double getLng() {
        return latLngBean.getLng();
    }

    public String getAddress() {
        return address;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationFrom getFrom() {
        return from;
    }

    public LatLngBean getLatLngBean() {
        return latLngBean;
    }

    public List<LatLngBean> getPois() {
        return pois;
    }

    public void setPois(List<LatLngBean> pois) {
        this.pois = pois;
    }
}
