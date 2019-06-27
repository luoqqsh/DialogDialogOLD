package x.com.hl_map.bean;

public enum LocationType {
    GPS("gps"),
    AMAP("autonavi");

    private String type;

    LocationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
