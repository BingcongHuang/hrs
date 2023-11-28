package com.hrs.cloud.constants;

import org.apache.commons.lang3.StringUtils;

public enum WeekEnum {
    MONDAY("MONDAY","1"),
    TUESDAY("TUESDAY","2"),
    WENDESDAY("WEDNESDAY","3"),
    THURSDAY("THURSDAY","4"),
    FRIDAY("FRIDAY","5"),
    SATURDAY("SATURDAY","6"),
    SUNDAY("SUNDAY","7");

    private String code;

    private String weekDay;

    WeekEnum(String code,String weekDay){
        this.code = code;
        this.weekDay = weekDay;
    }

    public String getCode(){
        return code;
    }

    public String getWeekDay(){
        return weekDay;
    }

    public static String getWeekDayByCode(String code){
        String result = "";
        for(WeekEnum item : WeekEnum.values()) {
            if(StringUtils.equals(item.getCode(),code)){
                result =item.getWeekDay();
                break;
            }
        }
        return result;
    }


}
