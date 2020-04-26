package x.com.hl_battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.text.TextUtils
import x.com.hl_livedatabus.LiveEventBus
import x.com.hl_log.ViseLog

class BatteryChangedReceiver : BroadcastReceiver() {
    private var batteryInfo: BatteryInfo? = null
    private var batteryInfoStr: String? = null
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (batteryInfo == null) batteryInfo = BatteryInfo()
        when {
            action.equals(Intent.ACTION_BATTERY_CHANGED, ignoreCase = true) -> {
                batteryInfo!!.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
                batteryInfo!!.batteryIcon = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1)
                batteryInfo!!.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                batteryInfo!!.chargeCounter = intent.getIntExtra("charge_counter", -1)
                batteryInfo!!.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                batteryInfo!!.seq = intent.getIntExtra("seq", -1)
                batteryInfo!!.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "未知"
                batteryInfo!!.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
                batteryInfo!!.max_charging_current = intent.getIntExtra("max_charging_current", -1)
                batteryInfo!!.max_charging_voltage = intent.getIntExtra("max_charging_voltage", -1)
                batteryInfo!!.health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
                batteryInfo!!.pluged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                batteryInfo!!.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            }
            action.equals(Intent.ACTION_BATTERY_LOW, ignoreCase = true) -> {
                // 表示当前电池电量低
                LiveEventBus.get("lowPower", Boolean::class.java).post(true)
            }
            action.equals(Intent.ACTION_BATTERY_OKAY, ignoreCase = true) -> {
                // 表示当前电池已经从电量低恢复为正常
                LiveEventBus.get("lowPower", Boolean::class.java).post(false)
            }
        }
        LiveEventBus.get("batteryInfo", BatteryInfo::class.java).post(batteryInfo)
        if (TextUtils.isEmpty(batteryInfoStr)) {
            batteryInfoStr = batteryInfo.toString()
            ViseLog.d("电池信息\n$batteryInfoStr")
        }
    }
}