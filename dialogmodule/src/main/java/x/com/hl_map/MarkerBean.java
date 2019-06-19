package x.com.hl_map;

import java.io.Serializable;

public class MarkerBean implements Serializable {
    private double lat;
    private double lng;
    private String img;
    private int imgSource;

    public double getLat() {
        return lat;
    }

    public void setPosition(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getImgSource() {
        return imgSource;
    }

    public void setImgSource(int imgSource) {
        this.imgSource = imgSource;
    }
}
