package x.com.hl_map.bean;

public enum ErrorBean {
    NODATA(-1, "没有数据"),
    LATLNGERROR(-2, "经纬度异常"),
    OTHER(-3, "其他错误"),
    NULL(-4, "参数为空");
    private int code;
    private String value;

    ErrorBean(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
