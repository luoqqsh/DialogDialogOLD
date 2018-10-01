package x.com.hl_tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class hl_time {
    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time, String timetype) {
        SimpleDateFormat df = new SimpleDateFormat(timetype, Locale.CHINA);
        Date date = new Date(time);
        return df.format(date);
    }

    public static String convertToTime(String time, String timetype1, String timetype2) throws ParseException {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(timetype1, Locale.CHINA);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(timetype2, Locale.CHINA);
        Date dt = simpleDateFormat1.parse(time);
        return simpleDateFormat2.format(dt);
    }

    public static String convertToTime(Date time, String timetype) {
        return "";
    }

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

    //获取当前日期
    public String getTodayData(String timetype) {
        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        SimpleDateFormat sdf = new SimpleDateFormat(timetype, Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

    // 获得明天日期,明天为1，后天为2
    public String getTomoData(String timetype, int amount) {
        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        SimpleDateFormat sdf = new SimpleDateFormat(timetype, Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

}
