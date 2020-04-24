package x.com.hl_battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import x.com.hl_log.ViseLog;

public class BatteryChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ViseLog.d(intent);
        String action = intent.getAction();
        if (action == null) return;
        if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
            // 当前电池的电压
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            ViseLog.d(voltage);
            // 电池的健康状态
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            ViseLog.d(health);
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_GOOD://好
                    break;
                case BatteryManager.BATTERY_HEALTH_COLD://过冷
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD://良好
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT://过热
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE://电压过高
                    break;
                case BatteryManager.BATTERY_HEALTH_UNKNOWN://未知
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE://未知
                    break;
                default:
                    break;
            }
            // 电池当前的电量, 它介于0和 EXTRA_SCALE之间
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            ViseLog.d(level);
            // 电池电量的最大值
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            ViseLog.d(scale);
            // 当前手机使用的是哪里的电源
            int pluged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            ViseLog.d(pluged);
            switch (pluged) {
                case BatteryManager.BATTERY_PLUGGED_AC://电源充电
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB://USB充电
                    break;
                case BatteryManager.BATTERY_PLUGGED_WIRELESS://无线充电
                    break;
                default:
                    break;
            }
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int icon = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1);
            ViseLog.d(status);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:// 正在充电
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING://放电中
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:// 充满
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:// 没有充电
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:// 未知状态
                    break;
                default:
                    break;
            }
            // 电池使用的技术。比如，对于锂电池是Li-ion
            String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            ViseLog.d(technology);
            // 当前电池的温度
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            ViseLog.d(temperature);//0.1度单位
        } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_LOW)) {
            // 表示当前电池电量低
            ViseLog.d("low power");
        } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_OKAY)) {
            // 表示当前电池已经从电量低恢复为正常
            ViseLog.d("low normal");
        }
    }
}
//max_charging_voltage充电器支持的最大充电电压，单位微伏
//max_charging_current充电器支持的最大充电电流，单位为微安
//present电池是否存在
//seq序列号
//charge_counter充电次数
//invalid_charger如果连接了不支持的充电器，则Int值设置为非零
//battery_low是否低电量