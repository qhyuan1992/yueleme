package com.mini.yueleme.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by weiersyuan on 2016/7/27.
 */
public class TimeUtil {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;

    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    /**
     * 时间毫秒数格式化成字符串
     * @param timemills 时间
     * @return
     */
    public static String dateStr2Long (long timemills) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date dt = new Date(timemills);
        String dateString = sdf.format(dt);
        return dateString;
    }

    /**
     * 时间秒数格式化成字符串
     * @param time 时间秒/毫秒数
     * @return
     */
    public static String long2DateString (long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        java.util.Date dt = new Date(time);
        String dateString = sdf.format(dt);
        return dateString;
    }

    /**
     * 将时间毫秒数转化成刚刚，n分钟前等形式
     * @param time
     * @return
     */
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "未知时间";
        }
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "刚刚";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1分钟前";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "分钟前";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1小时前";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "小时前";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "昨天";
        } else {
            return diff / DAY_MILLIS + "天前";
        }
    }
}
