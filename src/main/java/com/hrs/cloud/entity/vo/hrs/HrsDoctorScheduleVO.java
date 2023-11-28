package com.hrs.cloud.entity.vo.hrs;

import com.hrs.cloud.entity.HrsDoctorSchedule;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-16
 */
@Data
public class HrsDoctorScheduleVO extends HrsDoctorSchedule implements Serializable {


    private static final long serialVersionUID = -5693723265589288844L;

    private String userName;

    private String dptInfo;

    /**
     * 手机号
     */
    private String userTelphone;

}
