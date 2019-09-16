package x.com.hl_map.bean;

public enum LocationMode {
    /**
     * 定位选项
     */
    Battery_Saving(0),//Battery_Saving 低功耗，只是用网络定位
    Device_Sensors(1),//Device_Sensors 只是用设备GPS定位
    Hight_Accuracy(2);//Hight_Accuracy 两者都用

    private int type;

    LocationMode(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
