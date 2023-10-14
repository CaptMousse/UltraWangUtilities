package wang.ultra.my_utilities.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {
    public static String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getTime(String time) {
        String a = "20231012140512";
        StringBuffer sb = new StringBuffer(time);
        sb.insert(12, ":");
        sb.insert(10, ":");
        sb.insert(8, " ");
        sb.insert(6, "-");
        sb.insert(4, "-");
        return sb.toString();
    }

    public static String getWeekDay() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE"));
    }

    public static int getWeekDayNum() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @return 例如20230610193932
     */
    public static String getNoSymbolTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public static String getNoSymbolHourMinutes() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
    }

    public static String getNoSymbolTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date(time));
    }

    public static String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getDateFromStr(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(6, "-");
        sb.insert(4, "-");
        return sb.toString();
    }

    public static String getSimpleTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    public static String getWeek() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        return format.format(new Date());
    }

    public static String getWeekday() {
        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        if (isFirstSunday) {
            weekday = weekday - 1;
            if (weekday == 0) {
                weekday = 7;
            }
        }
        return String.valueOf(weekday);
    }

    /**
     * @param date 限定格式: 2023-08-23 21:32:49
     * @return
     */
    public static long getMillis(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取x天前, x小时前
     * @param time
     * @return
     */
    public static String getTimeAgo(String time) {
        long millisTimeAgo = (System.currentTimeMillis() - getMillis(time)) / 1000;

        String timeAgo;
        if (millisTimeAgo > 365 * 24 * 60 * 60) {
            timeAgo = (int) (millisTimeAgo / (365 * 24 * 60 * 60)) + "年前";
        } else if (millisTimeAgo > 24 * 60 * 60) {
            timeAgo = (int) (millisTimeAgo / (24 * 60 * 60)) + "天前";
        } else if (millisTimeAgo > 60 * 60) {
            timeAgo = (int) (millisTimeAgo / (60 * 60)) + "小时前";
        } else if (millisTimeAgo > 60) {
            timeAgo = (int) (millisTimeAgo / (60)) + "分前";
        } else if (millisTimeAgo > 1) {
            timeAgo = millisTimeAgo + "秒前";
        } else {
            timeAgo = "1秒前";
        }
        return timeAgo;
    }
}
