package com.hrs.cloud.entity.vo.hrs;

import com.hrs.cloud.entity.HrsRegisterRecord;
import lombok.Data;

@Data
public class HrsRegisterRecordVO extends HrsRegisterRecord {

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医生级别（1：普通，2：专家）
     */
    private Integer doctorLevel;


    /**
     * 科室信息
     */
    private String dptInfo;

    /**
     * 就诊日期
     */
    private String visitingDateStr;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者年龄
     */
    private Integer age;

}
