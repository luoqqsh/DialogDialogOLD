package x.com.hl_map.bean;

import java.io.Serializable;
import java.util.List;

public class LineBean implements Serializable {
    private LineType type;//线类型
    private boolean isWidth = false;
    private boolean isPosition = false;
    private boolean isPositionList = false;
    private boolean isRaduis = false;
    private boolean isImg = false;
    private boolean isImgResource = false;
    private boolean isColor1 = false;
    private boolean isColor2 = false;

    //线
    private List<LatLngBean> latlngs;//线的坐标集
    private int width = 10;//线的宽度，默认为10.

    //圆
    private int radius = 1000;//圆的半径，默认1000
    private double lat;
    private double lng;

    //Marker-位置lat,lng-

    private String img;
    private int imgSource;
    private int color1 = 0xff000000;//颜色，边框颜色。默认黑色
    private int color2 = 0x60000000;//颜色，填充颜色，默认半透明黑色

    public LineBean(LineType type) {
        this.type = type;
    }

    public boolean isWidth() {
        return isWidth;
    }

    public boolean isPosition() {
        return isPosition;
    }

    public boolean isRaduis() {
        return isRaduis;
    }

    public boolean isImg() {
        return isImg;
    }

    public boolean isImgResource() {
        return isImgResource;
    }

    public boolean isColor1() {
        return isColor1;
    }

    public boolean isColor2() {
        return isColor2;
    }


    public void setPosition(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        isPosition = true;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
        isImg = true;
    }

    public int getImgSource() {
        return imgSource;
    }

    public void setImgSource(int imgSource) {
        this.imgSource = imgSource;
        isImgResource = true;
    }

    public List<LatLngBean> getLatlngs() {
        return latlngs;
    }

    public void setLatlngs(List<LatLngBean> latlngs) {
        this.latlngs = latlngs;
        isPositionList = true;
    }

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
        isColor1 = true;
    }

    public LineType getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        isWidth = true;

    }

    public void setRadius(int radius) {
        this.radius = radius;
        isRaduis = true;
    }

    public int getRadius() {
        return radius;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
        isColor2 = true;
    }


    public boolean isPositionList() {
        return isPositionList;
    }
}
