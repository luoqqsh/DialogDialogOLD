package x.com.hl_map.bean;

import java.io.Serializable;
import java.util.List;

import x.com.hl_map.MapController;

public class LineBean implements Serializable {
    private double lat;
    private double lng;
    private String img;
    private int imgSource;
    private int color;
    private MapController.LineType type;
    private List<LatLngs> latlngs;

    public LineBean(MapController.LineType type) {
        this.type = type;
    }

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

    public List<LatLngs> getLatlngs() {
        return latlngs;
    }

    public void setLatlngs(List<LatLngs> latlngs) {
        this.latlngs = latlngs;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public MapController.LineType getType() {
        return type;
    }
}
