package com.manager.shopping.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/15.
 * @author 文捷
 * 时间工具类
 */

public class DateUtils {

    /**
     * 转化时间输入时间与当前时间的间隔
     *
     * @param timestamp
     * @return
     *
     */
    public static String converTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > 24 * 60 * 60) {// 1天以上
            timeStr = timeGap / (24 * 60 * 60) + "天前";
        } else if (timeGap > 60 * 60) {// 1小时-24小时
            timeStr = timeGap / (60 * 60) + "小时前";
        } else if (timeGap > 60) {// 1分钟-59分钟
            timeStr = timeGap / 60 + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 获取星期
     * @param dateStr
     * @return
     */
        public static String getWeek(String dateStr) {
        Date date = null;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date  = sdf.parse(dateStr);
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
            String week = null;
            switch (dayOfWeek){
                case 1:
                    week = "星期一";
                    break;
                case 2:
                    week = "星期二";
                    break;
                case 3:
                    week = "星期三";
                    break;
                case 4:
                    week = "星期四";
                    break;
                case 5:
                    week = "星期五";
                    break;
                case 6:
                    week = "星期六";
                    break;
                default:
                    week = "星期天";
                    break;
            }
        return week;
    }
}
