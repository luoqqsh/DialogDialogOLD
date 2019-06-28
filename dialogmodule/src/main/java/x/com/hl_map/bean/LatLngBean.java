package x.com.hl_map.bean;

public class LatLngBean {
    private double lat;//纬度
    private double lng;//经度
    private String address;//地址
    private String city = "厦门";//城市

    public LatLngBean(double lat, double lng) {
        this(lat, lng, "");
    }

    public LatLngBean(double lat, double lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
