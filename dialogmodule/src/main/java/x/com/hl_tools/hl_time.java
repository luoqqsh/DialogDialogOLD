package x.com.hl_tools;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import x.com.hl_tools.model.TimeConstants;

public class hl_time {
    public static final String yyyyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String HHmmss = "HH:mm:ss";

    /**
     * 时间戳格式化成指定格式
     *
     * @param time     时间戳
     * @param timeType 要得到的时间格式
     * @return 转换后的时间
     */
    public static String convertLongToTime(long time, String timeType) {
        SimpleDateFormat df = new SimpleDateFormat(timeType, Locale.CHINA);
        Date date = new Date(time);
        return df.format(date);
    }

    /**
     * 时间格式转换，如20190101 转成 2019-01-01
     *
     * @param time      时间
     * @param timeType1 需要转换的时间格式
     * @param timeType2 要得到的时间格式
     * @return 转换后的时间
     * @throws ParseException 异常
     */
    public static String convertToTime(String time, String timeType1, String timeType2) throws ParseException {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(timeType1, Locale.CHINA);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(timeType2, Locale.CHINA);
        Date dt = simpleDateFormat1.parse(time);
        return simpleDateFormat2.format(dt != null ? dt : "");
    }

    public static String convertToTime(Date time, String timeType) {
        return "";
    }

    /**
     * 将秒数转为xx小时xx分钟xx秒
     *
     * @param second 秒数
     * @return 结果
     */
    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

    /**
     * 获取当前时间戳
     *
     * @return long时间戳
     */
    public static long getNowTimeLong() {
        return new Date().getTime();
    }

    /**
     * 获取现在时间
     *
     * @param timeType 日期格式，例如yyyy-MM-dd HH:mm:ss SSS
     * @return 今天时间
     */
    public static String getNowTimeStr(String timeType) {
        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        SimpleDateFormat sdf = new SimpleDateFormat(timeType, Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

    /**
     * 指定日期转时间戳
     *
     * @param dateString 指定日期
     * @param pattern    日期格式
     * @return 时间戳
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        Date date = new Date();
        try {
            return dateFormat.parse(dateString).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取几天后日期
     *
     * @param timeType 日期格式，例如yyyy-MM-dd HH:mm:ss SSS
     * @param amount   明天为1，后天为2，以此类推
     * @return 日期
     */
    public static String getTomoData(String timeType, int amount) {
        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        SimpleDateFormat sdf = new SimpleDateFormat(timeType, Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

    /**
     * 将毫秒数格式化为"##:##"的时间
     *
     * @param milliseconds 毫秒数
     * @return ##:##
     */
    public static String formatTime(long milliseconds) {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = milliseconds / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /*获取星期几*/
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 是否今天第一次
     *
     * @param context 上下文
     * @param key     关键字
     * @param isSave  是否保存
     * @return 结果
     */
    private static boolean isTodayFirst(Context context, String key, boolean isSave) {
        String currentActivityDate = getNowTimeStr(yyyyMMdd);
        String activityDate = hl_sharedP.getParam(context, key + "activity_date", "");
        if (!activityDate.equals(currentActivityDate)) {
            if (isSave) {
                hl_sharedP.setParam(context, key + "activity_date", currentActivityDate);
            }
            return true;
        }
        return false;
    }

    /**
     * Time span in unit to milliseconds.
     *
     * @param timeSpan The time span.
     * @param unit     The unit of time span.
     *                 <ul>
     *                 <li>{@link TimeConstants#MSEC}</li>
     *                 <li>{@link TimeConstants#SEC }</li>
     *                 <li>{@link TimeConstants#MIN }</li>
     *                 <li>{@link TimeConstants#HOUR}</li>
     *                 <li>{@link TimeConstants#DAY }</li>
     *                 </ul>
     * @return milliseconds
     */
    public static long timeSpan2Millis(long timeSpan, @TimeConstants.Unit int unit) {
        return timeSpan * unit;
    }

    /**
     * Milliseconds to time span in unit.
     *
     * @param millis The milliseconds.
     * @param unit   The unit of time span.
     *               <ul>
     *               <li>{@link TimeConstants#MSEC}</li>
     *               <li>{@link TimeConstants#SEC }</li>
     *               <li>{@link TimeConstants#MIN }</li>
     *               <li>{@link TimeConstants#HOUR}</li>
     *               <li>{@link TimeConstants#DAY }</li>
     *               </ul>
     * @return time span in unit
     */
    public static long millis2TimeSpan(long millis, @TimeConstants.Unit int unit) {
        return millis / unit;
    }

    /**
     * Milliseconds to fit time span.
     *
     * @param millis    The milliseconds.
     *                  <p>millis &lt;= 0, return null</p>
     * @param precision The precision of time span.
     *                  <ul>
     *                  <li>precision = 0, return null</li>
     *                  <li>precision = 1, return 天</li>
     *                  <li>precision = 2, return 天, 小时</li>
     *                  <li>precision = 3, return 天, 小时, 分钟</li>
     *                  <li>precision = 4, return 天, 小时, 分钟, 秒</li>
     *                  <li>precision &gt;= 5，return 天, 小时, 分钟, 秒, 毫秒</li>
     *                  </ul>
     * @return fit time span
     */
    @SuppressLint("DefaultLocale")
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) return null;
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 按照分钟 格式化为 小时 ：分钟
     */
    public static String[] formatHomeTime(long walkTime) {
        String hour = "00";
        String min = "00";
        if (walkTime > 60) {
            long h = walkTime / 60;
            if (h >= 10) {
                hour = h + "";
            } else {
                hour = "0" + h;
            }
            //剩余分钟
            long m = walkTime - 60 * h;

            if (m >= 10) {
                min = m + "";

            } else {
                min = "0" + m;
            }
        } else if (walkTime < 10) {
            min = "0" + walkTime;
        } else {
            min = "" + walkTime;

        }
        String[] string = new String[]{hour, min};
        return string;
    }
}
