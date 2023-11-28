package com.hrs.cloud.helper;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class DateHelper {

    public static String converToStringDate(Date date, String formatString) {
        if (Strings.isNullOrEmpty(formatString)) {
            SimpleDateFormat sdf_datetime_s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf_datetime_s.format(date);
        } else {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            return format.format(date);
        }
    }

    /**
     *
     * @return Date date。JAVA时间类型。
     */
    public static Date converToDateTime(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date converToDateTime(String dateStr,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @return Date date。JAVA时间类型。
     */
    public static String converToStringMiniuteDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param days 可以为任何整数，负数表示前days天，正数表示后days天
     * @return Date
     */
    public static Date getAddDayTime(Date dt, int days) {
        if (dt == null)
            dt = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
        return cal.getTime();
    }

    /**
     *
     * @return Date date。JAVA时间类型。
     */
    public static String converToStringTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date converToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date converToDate(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
            return sdf.parse(dateStr);
        } catch (Exception var4) {
            return null;
        }
    }
    public static String converToStringDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将时间移动days天
     *
     * @param date 当前日期
     * @param days 移动的天数
     * @return 移动后的时间
     */
    public static Date addDay(Date date, Integer days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    /**
     * 将时间移动months天
     *
     * @param date 当前日期
     * @param months 移动的月数
     * @return 移动后的时间
     */
    public static Date addMonth(Date date, Integer months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }
    /**
     * Description	： 比较两个时间的大小
     * @param data1
     * @param data2
     * @return
     *
     */
    public static int compare_date(Date data1, Date data2) {
        try {
            if (data1.getTime() > data2.getTime()) {
                return 1;
            } else if (data1.getTime() < data2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error("compare_Date is error",e);
        }
        return 0;
    }

    public static String    getDateZhStr(Date date){
        Date curDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String time = new SimpleDateFormat("HH:mm").format(date);
        String dateNumber = df.format(date);
        String curNumber = df.format(curDate);
        int minus = Integer.valueOf(curNumber) - Integer.valueOf(dateNumber);
        String zhStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        switch (minus) {
            case 0:
                zhStr = "今天 "+time;
                break;
            case 1:
                zhStr = "昨天 "+time;
                break;
            case 2:
                zhStr = "前天 "+time;
                break;
            default:
                break;

        }
        return zhStr;
    }

    public static int getMonth(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取某个时间当天某点的时间信息
     * @param cal
     * @param hourOfDay
     * @return
     */
    public static Long getSecondByHourOfDay(Calendar cal,Integer hourOfDay){
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis()/1000;
    }

    /**
     * 获取某个时间当天某点的时间信息
     * @param cal
     * @param hourOfDay
     * @return
     */
    public static Date getDateByHourOfDay(Calendar cal,Integer hourOfDay){
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);

        while(dEnd.after(calBegin.getTime())) {
            calBegin.add(5, 1);
            lDate.add(calBegin.getTime());
        }

        return lDate;
    }

    public static Date findLastDayCurrMonth(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }

    public static String getTimeIntervalBegin(Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin ;
    }

    public static String getTimeIntervalEnd(Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeEnd ;
    }
}
