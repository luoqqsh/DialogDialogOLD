package x.com.hl_battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.text.TextUtils;

import x.com.hl_livedatabus.LiveEventBus;
import x.com.hl_log.ViseLog;

public class BatteryChangedReceiver extends BroadcastReceiver {
    private String batteryInfo = "";
    private int voltage = 0;// 当前电池的电压
    private int level = 0;// 电池当前的电量, 它介于0和 EXTRA_SCALE之间
    private int scale = 100;// 电池电量的最大值
    private int charge_counter = 0;// 充电次数
    private int temperature = 0;//电池温度
    private String healthStr = "未知";// 电池的健康状态
    private String plugedStr = "未知";// 当前手机使用的是哪里的电源
    private String statusStr = "未知";//当前状态
    private String technology = "未知";//电池技术
    private int max_charging_voltage = 0;//充电器支持的最大充电电压，单位微伏
    private int max_charging_current = 0;//充电器支持的最大充电电流，单位为微安
    private int batteryIcon = 0;//电池图标
    private int seq;//电池序列号

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;
        if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            batteryIcon = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1);
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            charge_counter = intent.getIntExtra("charge_counter", -1);
            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            seq = intent.getIntExtra("seq", 0);
            technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            max_charging_voltage = intent.getIntExtra("max_charging_voltage", -1);
            max_charging_current = intent.getIntExtra("max_charging_current", -1);
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_GOOD://好
                    healthStr = "正常";
                    break;
                case BatteryManager.BATTERY_HEALTH_COLD://过冷
                    healthStr = "过冷";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD://良好
                    healthStr = "良好";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT://过热
                    healthStr = "过热";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE://电压过高
                    healthStr = "电压过高";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNKNOWN://未知
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthStr = "未知";
                    break;
                default:
                    break;
            }
            int pluged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            switch (pluged) {
                case BatteryManager.BATTERY_PLUGGED_AC://电源充电
                    plugedStr = "电源充电";
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB://USB充电
                    plugedStr = "USB充电";
                    break;
                case BatteryManager.BATTERY_PLUGGED_WIRELESS://无线充电
                    plugedStr = "无线充电";
                    break;
            }
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:// 正在充电
                    statusStr = "正在充电";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING://放电中
                    statusStr = "放电中";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:// 充满
                    statusStr = "充满";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:// 没有充电
                    statusStr = "没有充电";
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:// 未知状态
                    statusStr = "未知";
                    break;
                default:
                    break;
            }
        } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_LOW)) {
            // 表示当前电池电量低
            ViseLog.d("low power");
        } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_OKAY)) {
            // 表示当前电池已经从电量低恢复为正常
            ViseLog.d("low normal");
        }
        batteryInfo = "电池电压:" + voltage / 1000.0f + "V\n" +
                "电池健康状态:" + healthStr + "\n" +
                "当前电量:" + level + "\n" +
                "最大电量:" + scale + "\n" +
                "充电方式:" + plugedStr + "\n" +
                "充电状态:" + statusStr + "\n" +
                "电池技术:" + technology + "\n" +
                "充电次数:" + charge_counter + "\n" +
                "电池序列号:" + seq + "\n" +
                "充电器支持最大电流:" + max_charging_current / 1000000f + "A\n" +
                "充电器支持最大电压:" + max_charging_voltage / 1000000f + "V\n" +
                "电池温度:" + temperature / 10f + "℃";
        LiveEventBus.get("batteryInfo", String.class).post(batteryInfo);
        LiveEventBus.get("batteryCurrentLevel", Integer.class).post(level);
        if (TextUtils.isEmpty(batteryInfo)) {
            ViseLog.d("电池信息\n" + batteryInfo);
        }
    }
}
//获取实时数据
//LiveEventBus.get("batteryInfo", String::class.java)
//        .observe(mActivity, Observer { info -> ViseLog.d(info) })