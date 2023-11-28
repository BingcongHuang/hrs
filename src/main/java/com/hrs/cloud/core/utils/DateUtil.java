/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hrs.cloud.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtil {

    public static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear(Date date) {
		return formatDate(date, "yyyy");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay() {
		return formatDate(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	
	/**
	 * 自定义格式化的时间
	 *
	 * @return
	 */
	public static String getDay(Date date,String pattern) {
		return formatDate(date, pattern);
	}
	
	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays() {
		return formatDate(new Date(), "yyyyMMdd");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays(Date date) {
		return formatDate(date, "yyyyMMdd");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
	
	/**
	 * 获取YYYY-MM-DD HH:mm:ss.SSS格式
	 *
	 * @return
	 */
	public static String getMsTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 获取YYYYMMDDHHmmss格式
	 *
	 * @return
	 */
	public static String getAllTime() {
		return formatDate(new Date(), "yyyyMMddHHmmss");
	}
	
	public static String getAllMillisecondTime() {
	    return formatDate(new Date(), "yyyyMMddHHmmssSSS");
	}
	

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime(Date date) {
		if(date != null) {
			return formatDate(date, "yyyy-MM-dd HH:mm:ss");
		} else {
			return "";
		}
	}
	
	
	/**
	 * 获取YYYY-MM-DD HH:mm格式
	 *
	 * @return
	 */
	public static String getStandardTime(Date date) {
		if(date != null) {
			return formatDate(date, "yyyy-MM-dd HH:mm");
		} else {
			return "";
		}
	}

	public static String formatDate(Date date, String pattern) {
		if(date == null){
			return "";
		}
		String formatDate = null;
		if (StringUtils.isNotBlank(pattern)) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * @Title: compareDate
	 * @Description:(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @author bingcong huang
	 */
	public static boolean compareDate(String s, String e) {
		if (parseDate(s) == null || parseDate(e) == null) {
			return false;
		}
		return parseDate(s).getTime() >= parseDate(e).getTime();
	}
   
	
	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseDate(String date) {
		return parse(date,"yyyy-MM-dd");
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseTime(String date) {
		return parse(date,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		try {
			if(StringUtils.isNotBlank(date)) {
				return DateUtils.parseDate(date,pattern);
			} else {
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static String format(Date date, String pattern) {
	    if (date != null) {
		    return DateFormatUtils.format(date, pattern);
	    } else {
	        return null;
	    }
	}

	/**
	 * 把日期转换为Timestamp
	 *
	 * @param date
	 * @return
	 */
	public static Timestamp format(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s) {
		return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s, String pattern) {
        return parse(s, pattern) != null;
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
					startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author bingcong huang
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	/**
	 * 格式化Oracle Date
	 * @param value
	 * @return
	 */
//	public static String buildDateValue(Object value){
//		if(Func.isOracle()){
//			return "to_date('"+ value +"','yyyy-mm-dd HH24:MI:SS')";
//		}else{
//			return Func.toStr(value);
//		}
//	}

	/**
     * 给时间加上几个小时
     * @param date 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return
     */
    public static Date addDateMinut(Date date, int hour){   
        if (date == null){   
            return null;  
        }
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.add(Calendar.HOUR, hour);// 24小时制   
        date = cal.getTime();   
        cal = null;   
        return date;   

    }
    
    
    /**
     * 给时间加上几十分钟
     * @param date 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param minute 需要加的分钟
     * @return
     */
    public static Date addDateMinuts(Date date, int minute){   
        if (date == null){   
            return null;  
        }
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.add(Calendar.MINUTE, minute);// 24小时制   
        date = cal.getTime();   
        cal = null;   
        return date;   
    }
    
    
    
    
    
    public static String getDatePoor(Date endDate, Date nowDate) {
    	 
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }


	public static String getDatePoorExt(Date endDate, Date nowDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		if(diff > 0) {
			// 计算差多少天
			long day = diff / nd;
			// 计算差多少小时
			long hour = diff % nd / nh;
			// 计算差多少分钟
			long min = diff % nd % nh / nm;
			// 计算差多少秒//输出结果
			// long sec = diff % nd % nh % nm / ns;
			return day + "天" + hour + "小时" + min + "分钟";
		} else {
			return "航班已起飞";
		}
	}

    public static Date getNextDay(){
    	Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, +1); //今天的时间加一天  
        return calendar.getTime(); 
    }
    
    public static Date getBeforeDay(){
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -1); //今天的时间减一天  
        return calendar.getTime(); 
    }

    /**
     * 得到当前时间的五天前数据
     * @return
     */
    public static Date getBeforeFiveDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -5); //今天的时间减一天
        return calendar.getTime();
    }
    
    
    public static Date getNextDay(int days){
    	if(days > 0){
	    	Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(new Date());  
	        calendar.add(Calendar.DAY_OF_MONTH, +days); //设置时间加days天  
	        return calendar.getTime(); 
    	} else {
    		return new Date();
    	}
    }
    
    public static Date getNextDay(Date date){
    	Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, +1); //今天的时间加一天  
        return calendar.getTime(); 
    }
    
    public static Date getNextDay(Date date,int days){
    	Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, days); //今天的时间加一天  
        return calendar.getTime(); 
    }
    
    public static Integer getWeekOfDate(Date dt) {  
        Integer[] weekDays = {7,1,2,3,4,5,6};  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(dt);  
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;  
        if (w < 0)  
            w = 0;  
        return weekDays[w];  
    }  
    
    public static Date getDate( String dateStr, String... formats ) {
        Date date = null;
        for ( String formatStr : formats ) {
            try {
                SimpleDateFormat format = new SimpleDateFormat( formatStr );
                date = format.parse( dateStr );
                break;
            } catch ( ParseException e ) {

            }
        }
        return date;
    }
    
    /**
     * compareStringDate:(日期比较，如果s>=e 返回true 否则返回false)
     * @author bingcong huang
     * @param s
     * @param e
     * @return
     * @since JDK 1.8
     */
    public static boolean compareStringDate(String s,String e){
        int flag = s.compareTo( e );
        if(flag >= 0){
            return true;
        }else{
            return false;
        }
    }
    
    
    
    public static  boolean  isEffectiveDate(Date date ,Date startDate,Date endDate){
         date = DateUtil.parseDate( DateUtil.format( date, "yyyy-MM-dd" ) );
        boolean flag = false;
        long time  = date.getTime();
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        if(time>=startTime && time<=endTime){
            flag = true;
        }
        return flag;
    }

	/**
	 * @param hourMinite 10:01
	 * @param date
	 * @return
	 */
	public static Date joinDate(String hourMinite,Date date){
		String stdDate = DateUtil.format(date, "yyyyMMdd");
		return DateUtil.parse(stdDate+" " + hourMinite, "yyyyMMdd HH:mm");
	}


	public static void main(String[] args) {
		//System.out.println(getTime(new Date()));
		//System.out.println(getAfterDayWeek("3"));
		//System.out.println(addDateMinut(new Date(),2));
		//System.out.println(getDatePoor(new Date(),new Date()));
		//Integer hh = Integer.parseInt(formatDate(new Date(),"HH"));
//		Date date = getNextDay();
//		System.out.println(getMsTime());
		//20180704094306265
		//20180704094808126
		///System.out.println(formatDate(new Date(), "yyyyMMddHHmmssSSS"));
//		String sendTime ="20180720154047+08"; ;
//		
//		Date time = DateUtil.parse( sendTime.substring( 0, 14 ), "yyyyMMddHHmmss" );
//		System.out.println( time );
//	    String str = getDay( getNextDay(new Date(),1) );
//	    System.out.println( str );
	    
//	    String data  = "2018-09-26 17:10";
//	    Date dd = DateUtil.parse( data, "yyyy-MM-dd HH:mm" );
//	    Date ss =  DateUtil.parse( DateUtil.format( new Date(), "yyyy-MM-dd HH:mm" ), "yyyy-MM-dd HH:mm" );
//	    System.out.println( dd.getTime() );
//	    System.out.println( ss.getTime() );
//	    System.out.println( dd.getTime()>ss.getTime() );
//	   String date = "2019-03-30";
//	   String startDate = "2019-01-08";
//	   String endDate = "2019-03-30";
//   	   boolean s=  isEffectiveDate(DateUtil.parseDate(date),DateUtil.parseDate(startDate),DateUtil.parseDate(endDate));
//	   
//	   System.out.println( s );

	  }

	public static String localDateToString(LocalDate date) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(fmt);
	}

	public static List<LocalDate> fetchFlightDateReturnLocalDate(LocalDate effective, LocalDate expires,
																 String daysOfWeek) {
		List<Integer> daysOfWeeks = resolveDaysOfWeek(daysOfWeek);
		return Stream.iterate(effective, localDate -> localDate.plusDays(1L))
				.limit(ChronoUnit.DAYS.between(effective, expires) + 1)
				.filter(localDate1 -> daysOfWeeks.contains(localDate1.getDayOfWeek().getValue()))
				.collect(Collectors.toList());
	}

	private static List<Integer> resolveDaysOfWeek(String daysOfWeek) {
		List<Integer> daysOfWeeks = new ArrayList<>();
		if (StringUtils.isNotEmpty(daysOfWeek)) {
			for (int i = 0, len = daysOfWeek.length(); i < len; i++) {
				daysOfWeeks.add(Integer.valueOf(String.valueOf(daysOfWeek.charAt(i))));
			}
		}
		return daysOfWeeks;
	}

}
