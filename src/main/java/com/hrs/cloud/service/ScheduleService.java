package com.hrs.cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.constants.WeekEnum;
import com.hrs.cloud.core.utils.DateUtil;
import com.hrs.cloud.dao.*;
import com.hrs.cloud.entity.*;
import com.hrs.cloud.entity.dto.HrsDoctorScheduleDto;
import com.hrs.cloud.entity.dto.HrsRegisterDoctorDto;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentFirstVO;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentSecondVO;
import com.hrs.cloud.entity.vo.hrs.HrsDoctorScheduleVO;
import com.hrs.cloud.entity.vo.hrs.HrsRegisterDoctorVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Resource
    private HrsDoctorScheduleMapper hrsDoctorScheduleMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleRelationMapper sysUserRoleRelationMapper;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private HrsRegisterChargeMapper hrsRegisterChargeMapper;

    @Resource
    private HrsRegisterRecordMapper hrsRegisterRecordMapper;

    /**
     * 多条件分页查询医生排班信息
     * @return
     */
    public PageVO<HrsDoctorScheduleVO> findDoctorSchedules(HrsDoctorScheduleDto dto) {
        // 返回值定义
        PageVO<HrsDoctorScheduleVO> pageVO = new PageVO<>();
        List<HrsDoctorScheduleVO> doctorScheduleVOS = new ArrayList<>();
        Page<HrsDoctorSchedule> page = new Page<>();
        page.setCurrent(dto.getPage());
        page.setSize(dto.getLimit());
        // 查询用户信息
        QueryWrapper<HrsDoctorSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED",0);
        List<Long> userIds = new ArrayList<>();
        if(StringUtils.isNoneBlank(dto.getUserName())
        || null != dto.getFirstDptId()
        || null != dto.getSecondDptId()
        || null != dto.getDoctorLevel()) {
            QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("IS_DELETED",0);
            if(StringUtils.isNoneBlank(dto.getUserName())){
                userQueryWrapper.like("USER_REALNAME",dto.getUserName());
            }
            if(null != dto.getFirstDptId() && dto.getFirstDptId() > 0){
                userQueryWrapper.eq("FIRST_DPT_ID",dto.getFirstDptId());
            }
            if(null != dto.getSecondDptId() && dto.getSecondDptId() > 0){
                userQueryWrapper.eq("SECOND_DPT_ID",dto.getSecondDptId());
            }
            if(null != dto.getDoctorLevel() && dto.getDoctorLevel() > 0){
                userQueryWrapper.eq("DOCTOR_LEVEL",dto.getDoctorLevel());
            }
            List<SysUser> sysUsers = sysUserMapper.selectList(userQueryWrapper);
            List<Long> sysUserIds = sysUsers.stream().map(SysUser::getUserId).collect(Collectors.toList());
            QueryWrapper<SysUserRoleRelation> roleRelationQueryWrapper = new QueryWrapper<>();
            roleRelationQueryWrapper.eq("ROLEID",2); //角色为2（医生）的用户
            List<SysUserRoleRelation> roleRelation = sysUserRoleRelationMapper.selectList(roleRelationQueryWrapper);
            List<Long> roleUserIds =roleRelation.stream().map(SysUserRoleRelation::getUserid).collect(Collectors.toList());
            // 两个集合的userId取交集
            if(CollectionUtils.isNotEmpty(sysUserIds)) {
                Collection intersection = CollectionUtils.intersection(sysUserIds,roleUserIds);
                userIds.addAll(intersection);
            }
            if(CollectionUtils.isNotEmpty(userIds)) {
                queryWrapper.in("USER_ID",userIds);
            } else {
                queryWrapper.eq("USER_ID",0);
            }
        }
        if(null != dto.getScheduleType()) {
            queryWrapper.eq("SCHEDULE_TYPE",dto.getScheduleType());
        }
        IPage<HrsDoctorSchedule> doctorScheduls = hrsDoctorScheduleMapper.selectPage(page,queryWrapper);
        if(CollectionUtils.isNotEmpty(doctorScheduls.getRecords())){
            List<HrsDepartmentFirstVO> departmentFirstVOS = departmentService.findAllDepartments(true);
            Map<Integer,HrsDepartmentFirstVO> fsdptMap = departmentFirstVOS.stream()
                    .collect(Collectors.toMap(HrsDepartmentFirstVO::getId,HrsDepartmentFirstVO -> HrsDepartmentFirstVO));
            QueryWrapper<SysUser> doctorQueryWrapper = new QueryWrapper<>();
            doctorQueryWrapper.in("USER_ID",doctorScheduls.getRecords().stream().map(HrsDoctorSchedule::getUserId).collect(Collectors.toList()));

            List<SysUser> currentSysUsers = sysUserMapper.selectList(doctorQueryWrapper);
            Map<Long,SysUser> userMap = currentSysUsers.stream().collect(Collectors.toMap(SysUser::getUserId,SysUser->SysUser));
            doctorScheduls.getRecords().forEach(dct->{
                HrsDoctorScheduleVO doctorScheduleVO = new HrsDoctorScheduleVO();
                BeanUtils.copyProperties(dct,doctorScheduleVO);
                SysUser user = userMap.get(dct.getUserId());
                doctorScheduleVO.setUserName(user.getUserRealname());
                doctorScheduleVO.setUserTelphone(user.getUserTelphone());
                // 组装科室信息
                HrsDepartmentFirstVO firstVO = fsdptMap.get(user.getFirstDptId());
                String dptInfo = firstVO.getDptName() + "_";
                Map<Integer,String> sdptMap = firstVO.getSecondList().stream()
                        .collect(Collectors.toMap(HrsDepartmentSecondVO::getId,HrsDepartmentSecondVO::getDptName));
                dptInfo += sdptMap.get(user.getSecondDptId());
                doctorScheduleVO.setDptInfo(dptInfo);
                doctorScheduleVOS.add(doctorScheduleVO);
            });
        }

        // 返回
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(doctorScheduleVOS);
        pageVO.setCount(doctorScheduls.getTotal());
        return pageVO;
    }

    /**
     * 保存编辑医生排班信息
     */
    public Result modifyDoctorSchedule(HrsDoctorSchedule doctorSchedule, String updateUser){
        Result result = new Result();
        if(doctorSchedule == null){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("参数不正确");
            return result;
        }
        List<LocalDate> currentDates = DateUtil
                .fetchFlightDateReturnLocalDate(doctorSchedule.getDateStart()
                        ,doctorSchedule.getDateEnd()
                        ,doctorSchedule.getDaysOfWeek());
        int count;
        if(null != doctorSchedule.getId()) {
            // 编辑
            // 校验排班是否有重复的。
            Boolean checkFlag = false;

            QueryWrapper<HrsDoctorSchedule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .eq("USER_ID",doctorSchedule.getUserId())
                    .eq("SCHEDULE_TYPE",doctorSchedule.getScheduleType())
                    .ne("ID",doctorSchedule.getId());
            List<HrsDoctorSchedule> exsitSchedules = hrsDoctorScheduleMapper.selectList(queryWrapper);
            checkFlag = checkConflict(currentDates,doctorSchedule.getMoningFlag(),doctorSchedule.getAfternoonFlag(),exsitSchedules);
            if(checkFlag) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("该医生排班冲突，请核对！");
                return result;
            }
            doctorSchedule.setUpdateBy(updateUser.toString());
            doctorSchedule.setUpdateTime(LocalDateTime.now());
            count = hrsDoctorScheduleMapper.updateById(doctorSchedule);
        } else {
            // 新增
            // 校验排班是否有重复的。
            Boolean checkFlag = false;
            QueryWrapper<HrsDoctorSchedule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .eq("USER_ID",doctorSchedule.getUserId())
                    .eq("SCHEDULE_TYPE",doctorSchedule.getScheduleType());
            List<HrsDoctorSchedule> exsitSchedules = hrsDoctorScheduleMapper.selectList(queryWrapper);
            checkFlag = checkConflict(currentDates,doctorSchedule.getMoningFlag(),doctorSchedule.getAfternoonFlag(),exsitSchedules);
            if(checkFlag) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("该医生排班冲突，请核对！");
                return result;
            }
            doctorSchedule.setStatus(1);
            doctorSchedule.setDeleted(0);
            doctorSchedule.setCreateBy(updateUser);
            doctorSchedule.setUpdateBy(updateUser);
            doctorSchedule.setCreateTime(LocalDateTime.now());
            doctorSchedule.setUpdateTime(LocalDateTime.now());
            count = hrsDoctorScheduleMapper.insert(doctorSchedule);
        }
        if(count > 0) {
            result.setCode(ResultCode.SUCCESS);
            return result;
        } else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("操作失败");
            return result;
        }
    }

    /**
     * 逻辑删除辑医生排班
     * @param id
     * @param updateUser
     * @return
     */
    public Result delDoctorScheduleById(Long id,String updateUser) {
        Result result = new Result();
        HrsDoctorSchedule record = new HrsDoctorSchedule();
        //逻辑删除
        record.setId(id);
        record.setDeleted(1);
        record.setUpdateBy(updateUser);
        record.setUpdateTime(LocalDateTime.now());
        int count = hrsDoctorScheduleMapper.updateById(record);
        if (count > 0) {
            result.setCode(ResultCode.SUCCESS);
        } else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("操作失败");
            return result;
        }
        return result;
    }


    private boolean checkConflict(List<LocalDate> currentDates,Integer moningFlag,Integer afterFlag,List<HrsDoctorSchedule> exsitSchedules){
        // currentDates:本次选择的日期集合
        // 该医生上午已排班记录
        List<HrsDoctorSchedule> moningSchedules = exsitSchedules.stream().filter(sc->sc.getMoningFlag() == 1).collect(Collectors.toList());

        // 该医生下午已排班记录
        List<HrsDoctorSchedule> afternoonSchedules = exsitSchedules.stream().filter(sc->sc.getAfternoonFlag() == 1).collect(Collectors.toList());

        // 校验上午是否冲突
        if(moningFlag == 1
                && CollectionUtils.isNotEmpty(moningSchedules)) {
            List<LocalDate> moningDates = new ArrayList<>();
            moningSchedules.forEach(ms->{
                moningDates.addAll(DateUtil.fetchFlightDateReturnLocalDate(
                        ms.getDateStart(),ms.getDateEnd(),ms.getDaysOfWeek()));
            });

            Collection collection = CollectionUtils.intersection(currentDates,moningDates);
            if(CollectionUtils.isNotEmpty(collection)) {
                return true;
            }
        }
        // 校验下午是否冲突
        if(afterFlag == 1
                && CollectionUtils.isNotEmpty(afternoonSchedules)) {
            List<LocalDate> afternoonDates = new ArrayList<>();
            afternoonSchedules.forEach(ms->{
                afternoonDates.addAll(DateUtil.fetchFlightDateReturnLocalDate(
                        ms.getDateStart(),ms.getDateEnd(),ms.getDaysOfWeek()));
            });

            Collection collection = CollectionUtils.intersection(currentDates,afternoonDates);
            if(CollectionUtils.isNotEmpty(collection)) {
                return true;
            }
        }
        return false;
    }


    public HrsDoctorScheduleVO getDoctorScheduleDetail(Long id){
        HrsDoctorScheduleVO scheduleVO = new HrsDoctorScheduleVO();
        HrsDoctorSchedule schedule = hrsDoctorScheduleMapper.selectById(id);
        BeanUtils.copyProperties(schedule,scheduleVO);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID",schedule.getUserId());
        SysUser user = sysUserMapper.selectOne(queryWrapper);
        List<HrsDepartmentFirstVO> departmentFirstVOS = departmentService.findAllDepartments(true);
        Map<Integer,HrsDepartmentFirstVO> fsdptMap = departmentFirstVOS.stream()
                .collect(Collectors.toMap(HrsDepartmentFirstVO::getId,HrsDepartmentFirstVO -> HrsDepartmentFirstVO));

        // 组装科室信息
        HrsDepartmentFirstVO firstVO = fsdptMap.get(user.getFirstDptId());
        String dptInfo = firstVO.getDptName() + "_";
        Map<Integer,String> sdptMap = firstVO.getSecondList().stream()
                .collect(Collectors.toMap(HrsDepartmentSecondVO::getId,HrsDepartmentSecondVO::getDptName));
        dptInfo += sdptMap.get(user.getSecondDptId());
        scheduleVO.setDptInfo(dptInfo);
        scheduleVO.setUserName(user.getUserRealname());
        return scheduleVO;
    }

    /**
     * 查询满足条件的可挂号的医生信息集合
     * @param dto
     * @return
     */
    public List<HrsRegisterDoctorVO> findRegisterDoctors(HrsRegisterDoctorDto dto){
        List<HrsRegisterDoctorVO> doctorVOS = new ArrayList<>();
        // 根据科室查询医生信息
        QueryWrapper<SysUser> doctorQueryWrapper = new QueryWrapper<>();
        doctorQueryWrapper.eq("IS_DELETED",0)
                .eq("FIRST_DPT_ID",dto.getFirstDptId())
                .eq("SECOND_DPT_ID",dto.getSecondDptId());
        List<SysUser> doctors = sysUserMapper.selectList(doctorQueryWrapper);
        List<Long> doctorIds = doctors.stream().map(SysUser::getUserId).collect(Collectors.toList());


        // 计算传入日期是周几
        String weekDay = WeekEnum.getWeekDayByCode(LocalDate.parse(dto.getRegisterDate()).getDayOfWeek().toString());

        QueryWrapper<HrsDoctorSchedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.eq("DELETED",0)
                .in("USER_ID",doctorIds)
                .le("DATE_START",LocalDate.parse(dto.getRegisterDate()))
                .ge("DATE_END",LocalDate.parse(dto.getRegisterDate()))
                .like("DAYS_OF_WEEK",weekDay);
                List<HrsDoctorSchedule> schedules = hrsDoctorScheduleMapper.selectList(scheduleQueryWrapper);
        if(CollectionUtils.isNotEmpty(schedules)) {
            List<HrsDoctorSchedule> mixDoctors = new ArrayList<>();
            // 出诊集合
            List<HrsDoctorSchedule> workDoctors = schedules.stream().filter(s->s.getScheduleType() == 1).collect(Collectors.toList());
            // 停诊集合
            List<HrsDoctorSchedule> restDoctors = schedules.stream().filter(s->s.getScheduleType() == 2).collect(Collectors.toList());

            if(CollectionUtils.isNotEmpty(workDoctors)) {
                for(HrsDoctorSchedule hds : workDoctors){
                    List<Long> doctIds = mixDoctors.stream().map(HrsDoctorSchedule::getUserId).collect(Collectors.toList());
                    if(doctIds.contains(hds.getUserId())){
                        for(HrsDoctorSchedule ms : mixDoctors){
                            if(ms.getUserId().equals(hds.getUserId())){
                                if(hds.getMoningFlag() == 1){
                                    ms.setMoningFlag(hds.getMoningFlag());
                                }
                                if(hds.getAfternoonFlag() == 1){
                                    ms.setAfternoonFlag(hds.getAfternoonFlag());
                                }
                            }
                        }
                    } else {
                        mixDoctors.add(hds);
                    }

                }
                //判断是否有停诊
                if(CollectionUtils.isNotEmpty(restDoctors)){
                    for(HrsDoctorSchedule ms : mixDoctors){
                        for(HrsDoctorSchedule rd:restDoctors){
                            if(ms.getUserId().equals(rd.getUserId())){
                                if(rd.getMoningFlag() == 1){
                                    ms.setMoningFlag(2);
                                }
                                if(rd.getAfternoonFlag() == 1){
                                    ms.setAfternoonFlag(2);
                                }
                            }
                        }
                    }
                }
            }

            List<Long> userIds = mixDoctors.stream().map(HrsDoctorSchedule::getUserId).collect(Collectors.toList());
            //查询医生信息
            QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("USER_ID",userIds);
            List<SysUser> users = sysUserMapper.selectList(userQueryWrapper);
            Map<Long,SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getUserId,SysUser->SysUser));
            //查询挂号费信息
            QueryWrapper<HrsRegisterCharge> chargeQueryWrapper = new QueryWrapper<>();
            chargeQueryWrapper.eq("DELETED",0);
            List<HrsRegisterCharge> charges = hrsRegisterChargeMapper.selectList(chargeQueryWrapper);
            Map<Integer, BigDecimal> chargeMap = new IdentityHashMap<>();
            if(CollectionUtils.isNotEmpty(charges)) {
                chargeMap = charges.stream().collect(Collectors.toMap(HrsRegisterCharge::getDoctorLevel,HrsRegisterCharge::getRegisterCharges));
            }
            // 查询已挂号信息，计算剩余可挂号数
            QueryWrapper<HrsRegisterRecord> recordQueryWrapper = new QueryWrapper<>();
            recordQueryWrapper.eq("FIRST_DPT_ID",dto.getFirstDptId())
                    .eq("SECOND_DPT_ID",dto.getSecondDptId())
                    .eq("VISITING_DATE",dto.getRegisterDate())
                    .ne("STATUS",2);
            List<HrsRegisterRecord> registeredList = hrsRegisterRecordMapper.selectList(recordQueryWrapper);
            int moningRegisterNums;
            int afternoonRegisterNums;
            if(CollectionUtils.isNotEmpty(registeredList)){
                moningRegisterNums = registeredList.stream().filter(r->r.getTimeFlag() == 1).collect(Collectors.toList()).size();
                afternoonRegisterNums = registeredList.stream().filter(r->r.getTimeFlag() == 2).collect(Collectors.toList()).size();
            } else {
                moningRegisterNums = 0;
                afternoonRegisterNums = 0;
            }
            Map<Integer, BigDecimal> finalChargeMap = chargeMap;
            mixDoctors.forEach(s->{
                HrsRegisterDoctorVO doctorVO = new HrsRegisterDoctorVO();
                SysUser sysUser = userMap.get(s.getUserId());
                doctorVO.setDoctorId(sysUser.getUserId());
                doctorVO.setDoctorName(sysUser.getUserRealname());
                doctorVO.setDoctorIntroduction(sysUser.getDoctorIntroduction());
                doctorVO.setMoningFlag(s.getMoningFlag());
                doctorVO.setAfternoonFlag(s.getAfternoonFlag());
                doctorVO.setDoctorLevel(sysUser.getDoctorLevel());
                BigDecimal docCharges = finalChargeMap.get(sysUser.getDoctorLevel());
                if(null == docCharges) {
                    docCharges = new BigDecimal(10);
                }
                doctorVO.setRegisterCharges(docCharges);
                doctorVO.setMoningNums(s.getMoningLimit() - moningRegisterNums);
                doctorVO.setAfternoonNums(s.getAfternoonLimit() - afternoonRegisterNums);
                doctorVOS.add(doctorVO);
            });
        }
        return doctorVOS;
    }
}
