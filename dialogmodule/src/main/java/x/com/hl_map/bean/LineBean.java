package x.com.hl_map.bean;

import java.io.Serializable;
import java.util.List;

import x.com.hl_map.MapController;

public class LineBean implements Serializable {
    private double lat;
    private double lng;
    private String img;
    private int imgSource;
    private int color1 = 0xff000000;//颜色，边框颜色。默认黑色
    private int color2 = 0x50000000;//颜色，填充颜色，默认半透明黑色
    private MapController.LineType type;//线类型
    private List<LocationBean> latlngs;//线的坐标集
    private int width = 10;//线的宽度，默认为10.
    private int radius = 1000;//圆的半径，默认1000

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

    public List<LocationBean> getLatlngs() {
        return latlngs;
    }

    public void setLatlngs(List<LocationBean> latlngs) {
        this.latlngs = latlngs;
    }

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public MapController.LineType getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }
}
