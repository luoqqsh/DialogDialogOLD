package x.com.hl_map.bean;

public class LocationBean {
    private double lat;
    private double lng;
    private String address;
    private float zoom;

    public LocationBean(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LocationBean(double lat, double lng, String address) {
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

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
