package com.hrs.cloud.entity.vo.hrs;

import lombok.Data;

import java.io.Serializable;

@Data
public class HrsDepartmentSecondVO implements Serializable {
    private static final long serialVersionUID = -8803236595499399270L;
    private Integer id;

    private String dptName;
    /**
     * 顺序
     */
    private Integer sortNum;

    /**
     * 科室简介
     */
    private String dptRemark;
}
