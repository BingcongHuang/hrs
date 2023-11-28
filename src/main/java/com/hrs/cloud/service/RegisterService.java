package com.hrs.cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.core.utils.DateUtil;
import com.hrs.cloud.dao.HrsRegisterChargeMapper;
import com.hrs.cloud.dao.HrsRegisterRecordMapper;
import com.hrs.cloud.dao.HrsRegisterRuleMapper;
import com.hrs.cloud.dao.SysUserMapper;
import com.hrs.cloud.entity.HrsRegisterCharge;
import com.hrs.cloud.entity.HrsRegisterRecord;
import com.hrs.cloud.entity.HrsRegisterRule;
import com.hrs.cloud.entity.SysUser;
import com.hrs.cloud.entity.dto.HrsRegisterDoctorDto;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentFirstVO;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentSecondVO;
import com.hrs.cloud.entity.vo.hrs.HrsRegisterRecordVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegisterService {
    @Resource
    private HrsRegisterRuleMapper hrsRegisterRuleMapper;

    @Resource
    private HrsRegisterRecordMapper hrsRegisterRecordMapper;

    @Resource
    private HrsRegisterChargeMapper hrsRegisterChargeMapper;

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private DepartmentService departmentService;


    /**
     * 查询挂号规则信息
     * @return
     */
    public PageVO<HrsRegisterRule> findRegisterRules() {
        // 返回值定义
        PageVO<HrsRegisterRule> pageVO = new PageVO<>();
        Page<HrsRegisterRule> page = new Page<>();
        page.setCurrent(1);
        page.setSize(-1);
        // mybatis_plus的写法
        QueryWrapper<HrsRegisterRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED",0);
        queryWrapper.orderByDesc("UPDATE_TIME");
        IPage<HrsRegisterRule> departmentFirsts = hrsRegisterRuleMapper.selectPage(page,queryWrapper);
        // 返回
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(departmentFirsts.getRecords());
        pageVO.setCount(departmentFirsts.getTotal());
        return pageVO;
    }

    /**
     * 查询挂号手续费信息
     * @return
     */
    public PageVO<HrsRegisterCharge> findAllRegisterCharges() {
        // 返回值定义
        PageVO<HrsRegisterCharge> pageVO = new PageVO<>();
        Page<HrsRegisterCharge> page = new Page<>();
        page.setCurrent(1);
        page.setSize(-1);
        // mybatis_plus的写法
        QueryWrapper<HrsRegisterCharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED",0);
        queryWrapper.orderByDesc("UPDATE_TIME");
        IPage<HrsRegisterCharge> departmentFirsts = hrsRegisterChargeMapper.selectPage(page,queryWrapper);
        // 返回
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(departmentFirsts.getRecords());
        pageVO.setCount(departmentFirsts.getTotal());
        return pageVO;
    }

    /**
     * 根据id查询挂号规则信息
     * @param id
     * @return
     */
    public HrsRegisterRule getRegisterRuleById(Long id) {
        return hrsRegisterRuleMapper.selectById(id);
    }

    /**
     * 根据id查询挂号招待费信息
     * @param id
     * @return
     */
    public HrsRegisterCharge getRegisterChargeById(Long id) {
        return hrsRegisterChargeMapper.selectById(id);
    }

    /**
     * 保存编辑挂号规则信息
     */
    public Result modifyRegisterRuleInfo(HrsRegisterRule registerRule, String updateUser){
        Result result = new Result();
        if(registerRule==null){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("参数不正确");
            return result;
        }
        int count;
        if(null != registerRule.getId()) {
            // 编辑
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsRegisterRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                        .ne("ID",registerRule.getId())
                    .and(QueryWrapper -> QueryWrapper.between("DATE_START",registerRule.getDateStart(),registerRule.getDateEnd())
                            .or().between("DATE_END",registerRule.getDateStart(),registerRule.getDateEnd()));
            int cekCount = hrsRegisterRuleMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("日期区间有冲突，请确认！");
                return result;
            }
            registerRule.setUpdateBy(updateUser.toString());
            registerRule.setUpdateTime(LocalDateTime.now());
            count = hrsRegisterRuleMapper.updateById(registerRule);
        } else {
            // 新增
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsRegisterRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .and(QueryWrapper -> QueryWrapper.between("DATE_START",registerRule.getDateStart(),registerRule.getDateEnd())
                            .or().between("DATE_END",registerRule.getDateStart(),registerRule.getDateEnd()));
            int cekCount = hrsRegisterRuleMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("日期区间有交集，请确认！");
                return result;
            }
            registerRule.setStatus(1);
            registerRule.setDeleted(0);
            registerRule.setCreateBy(updateUser);
            registerRule.setUpdateBy(updateUser);
            registerRule.setCreateTime(LocalDateTime.now());
            registerRule.setUpdateTime(LocalDateTime.now());
            count = hrsRegisterRuleMapper.insert(registerRule);
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
     * 保存编辑手续费信息
     */
    public Result modifyRegisterChargeInfo(HrsRegisterCharge registerCharge, String updateUser){
        Result result = new Result();
        if(registerCharge==null){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("参数不正确");
            return result;
        }
        int count;
        if(null != registerCharge.getId()) {
            // 编辑
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsRegisterCharge> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .eq("DOCTOR_LEVEL",registerCharge.getDoctorLevel())
                    .ne("ID",registerCharge.getId());
            int cekCount = hrsRegisterChargeMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("已存在该级别医生挂号手续费,请查询修改！");
                return result;
            }
            registerCharge.setUpdateBy(updateUser.toString());
            registerCharge.setUpdateTime(LocalDateTime.now());
            count = hrsRegisterChargeMapper.updateById(registerCharge);
        } else {
            // 新增
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsRegisterCharge> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .eq("DOCTOR_LEVEL",registerCharge.getDoctorLevel());
            int cekCount = hrsRegisterChargeMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("已存在该级别医生挂号手续费,请查询修改！");
                return result;
            }

            registerCharge.setStatus(1);
            registerCharge.setDeleted(0);
            registerCharge.setCreateBy(updateUser);
            registerCharge.setUpdateBy(updateUser);
            registerCharge.setCreateTime(LocalDateTime.now());
            registerCharge.setUpdateTime(LocalDateTime.now());
            count = hrsRegisterChargeMapper.insert(registerCharge);
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
     * 逻辑删除挂号规则
     * @param id
     * @param updateUser
     * @return
     */
    public Result delRegisterRuleInfo(Long id,String updateUser) {
        Result result = new Result();
        HrsRegisterRule record = new HrsRegisterRule();
        //逻辑删除
        record.setId(id);
        record.setDeleted(1);
        record.setUpdateBy(updateUser);
        record.setUpdateTime(LocalDateTime.now());
        int count = hrsRegisterRuleMapper.updateById(record);
        if (count > 0) {
            result.setCode(ResultCode.SUCCESS);
        } else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("操作失败");
            return result;
        }
        return result;
    }

    /**
     * 逻辑挂号手续费信息
     * @param id
     * @param updateUser
     * @return
     */
    public Result delRegisterChargeInfo(Long id,String updateUser) {
        Result result = new Result();
        HrsRegisterCharge record = new HrsRegisterCharge();
        //逻辑删除
        record.setId(id);
        record.setDeleted(1);
        record.setUpdateBy(updateUser);
        record.setUpdateTime(LocalDateTime.now());
        int count = hrsRegisterChargeMapper.updateById(record);
        if (count > 0) {
            result.setCode(ResultCode.SUCCESS);
        } else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("操作失败");
            return result;
        }
        return result;
    }

    /**
     * 根据规则查询可挂号日期
     * @return
     */
    public List<String> getRegisterDateByRule(){
        List<String> registerDates = new ArrayList<>();
        QueryWrapper<HrsRegisterRule> registerRuleQueryWrapper = new QueryWrapper<>();
        registerRuleQueryWrapper.eq("DELETED",0)
                .le("DATE_START",LocalDate.now())
                .ge("DATE_END",LocalDate.now());
        List<HrsRegisterRule> registerRules = hrsRegisterRuleMapper.selectList(registerRuleQueryWrapper);
        HrsRegisterRule rule = new HrsRegisterRule();
        if(CollectionUtils.isNotEmpty(registerRules)){
            rule = registerRules.get(0);
        }
        int dateRange = 7;
        if(null != rule && null != rule.getEarlyDaysLimit()){
            dateRange = rule.getEarlyDaysLimit();
        }
        LocalDate earlyDate = LocalDate.now();
        for(int i = 0;i < dateRange;i++){
            registerDates.add(DateUtil.localDateToString(earlyDate));
            earlyDate = earlyDate.plusDays(1);
        }
        return registerDates;
    }


    /**
     * 查询患者已挂号信息
     * @param dto
     * @return
     */
    public List<HrsRegisterRecordVO> findPatientRegisterRecors(HrsRegisterDoctorDto dto){
        List<HrsRegisterRecordVO> registerRecords = new ArrayList<>();
        QueryWrapper<HrsRegisterRecord> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("PATIENT_USER_ID",dto.getPatientUserId());
        recordQueryWrapper.orderByAsc("STATUS");
        List<HrsRegisterRecord>  records = hrsRegisterRecordMapper.selectList(recordQueryWrapper);
        if(CollectionUtils.isNotEmpty(records)) {
            List<Long> docIds = records.stream().map(HrsRegisterRecord::getDoctorUserId).collect(Collectors.toList());
            QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("USER_ID",docIds);
            List<SysUser> users = sysUserMapper.selectList(userQueryWrapper);
            Map<Long,SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser->SysUser));
            List<HrsDepartmentFirstVO> departmentFirstVOS = departmentService.findAllDepartments(true);
            Map<Integer,HrsDepartmentFirstVO> fsdptMap = departmentFirstVOS.stream()
                    .collect(Collectors.toMap(HrsDepartmentFirstVO::getId,HrsDepartmentFirstVO -> HrsDepartmentFirstVO));
            records.forEach(r->{
                HrsRegisterRecordVO recordVO = new HrsRegisterRecordVO();
                BeanUtils.copyProperties(r,recordVO);
                SysUser user = userMap.get(r.getDoctorUserId());
                recordVO.setDoctorName(user.getUserRealname());
                recordVO.setDoctorLevel(user.getDoctorLevel());
                HrsDepartmentFirstVO firstVO = fsdptMap.get(user.getFirstDptId());
                String dptInfo = firstVO.getDptName() + "_";
                Map<Integer,String> sdptMap = firstVO.getSecondList().stream()
                        .collect(Collectors.toMap(HrsDepartmentSecondVO::getId,HrsDepartmentSecondVO::getDptName));
                dptInfo += sdptMap.get(user.getSecondDptId());
                recordVO.setDptInfo(dptInfo);
                recordVO.setVisitingDateStr(DateUtil.localDateToString(r.getVisitingDate()));
                registerRecords.add(recordVO);
            });
        }
        return registerRecords;
    }

    public Result doRegister(HrsRegisterDoctorDto dto,String operatorName) {
        Result result = new Result();
        // 判断患者和医生是否存在
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("USER_ID",dto.getPatientUserId())
                .or().eq("USER_ID",dto.getDoctorUserId());
        int count = sysUserMapper.selectCount(userQueryWrapper);
        if(count != 2){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("就诊卡号不对，请确认");
            return result;
        }

        // 查询规则
        QueryWrapper<HrsRegisterRule> registerRuleQueryWrapper = new QueryWrapper<>();
        registerRuleQueryWrapper.eq("DELETED",0)
                .le("DATE_START",LocalDate.now())
                .ge("DATE_END",LocalDate.now());
        List<HrsRegisterRule> registerRules = hrsRegisterRuleMapper.selectList(registerRuleQueryWrapper);
        HrsRegisterRule rule = new HrsRegisterRule();
        if(CollectionUtils.isNotEmpty(registerRules)){
            rule = registerRules.get(0);
        }
        if(null != rule){
            if(null != rule.getUnusedLimit()) {
                // 查询未使用挂号
                QueryWrapper<HrsRegisterRecord> recordQueryWrapper = new QueryWrapper<>();
                recordQueryWrapper.eq("PATIENT_USER_ID",dto.getPatientUserId())
                        .eq("status",1);
                int unUseCount = hrsRegisterRecordMapper.selectCount(recordQueryWrapper);
                if(unUseCount > rule.getUnusedLimit()){
                    result.setCode(ResultCode.FAIL_MESSAGE);
                    result.setMessage("你15天内有多个未使用挂号单，被限制挂号");
                    return result;
                }
            }

            if(null != rule.getAddtionLimit() && dto.getSourceType() == 2) {
                // 查询该医生当天追加挂号
                QueryWrapper<HrsRegisterRecord> addRecordQueryWrapper = new QueryWrapper<>();
                addRecordQueryWrapper.eq("DOCTOR_USER_ID",dto.getDoctorUserId())
                        .eq("VISITING_DATE",dto.getRegisterDate())
                        .eq("TIME_FLAG",dto.getTimeFlag());
                int addedCount = hrsRegisterRecordMapper.selectCount(addRecordQueryWrapper);
                if(addedCount > rule.getAddtionLimit()){
                    result.setCode(ResultCode.FAIL_MESSAGE);
                    result.setMessage("已达今天最大追加挂号，被限制挂号");
                    return result;
                }
            }
        }
        // 挂号费是否有值，无值则根据规则
        //查询挂号费信息
        if(null == dto.getRegisterCharges()){
            QueryWrapper<HrsRegisterCharge> chargeQueryWrapper = new QueryWrapper<>();
            chargeQueryWrapper.eq("DELETED",0);
            List<HrsRegisterCharge> charges = hrsRegisterChargeMapper.selectList(chargeQueryWrapper);
            Map<Integer, BigDecimal> chargeMap = new IdentityHashMap<>();
            if(CollectionUtils.isNotEmpty(charges)) {
                chargeMap = charges.stream().collect(Collectors.toMap(HrsRegisterCharge::getDoctorLevel,HrsRegisterCharge::getRegisterCharges));
            }
            BigDecimal docCharges = chargeMap.get(dto.getDoctorLevel());
            if(null == docCharges) {
                docCharges = new BigDecimal(10);
            }
            dto.setRegisterCharges(docCharges);
        }
        HrsRegisterRecord record = new HrsRegisterRecord();
        record.setCreateBy(operatorName);
        record.setPatientUserId(dto.getPatientUserId());
        record.setDoctorUserId(dto.getDoctorUserId());
        record.setRegisterCharges(dto.getRegisterCharges());
        record.setVisitingDate(LocalDate.parse(dto.getRegisterDate()));
        record.setFirstDptId(dto.getFirstDptId());
        record.setSecondDptId(dto.getSecondDptId());
        record.setSourceType(dto.getSourceType());
        record.setStatus(0);
        record.setTimeFlag(dto.getTimeFlag());
        record.setDeleted(0);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateBy(operatorName);
        record.setUpdateTime(LocalDateTime.now());
        hrsRegisterRecordMapper.insert(record);
        result.setCode(ResultCode.SUCCESS);
        return result;
    }

    public Result cnlRegister(HrsRegisterDoctorDto dto,String operatorName){
        Result result = new Result();
        HrsRegisterRecord record = new HrsRegisterRecord();
        record.setId(dto.getId());
        record.setStatus(2);
        record.setUpdateBy(operatorName);
        record.setUpdateTime(LocalDateTime.now());
        int count = hrsRegisterRecordMapper.updateById(record);
        if(count > 0) {
            result.setCode(ResultCode.SUCCESS);
        } else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("操作失败");
            return result;
        }
        return result;
    }

    /**
     * 消号
     * @param dto
     * @param operatorName
     * @return
     */
    public Result consumeRegister(HrsRegisterDoctorDto dto,String operatorName){
        Result result = new Result();
        HrsRegisterRecord record = new HrsRegisterRecord();
        record.setId(dto.getId());
        record.setStatus(1);
        record.setUpdateBy(operatorName);
        record.setUpdateTime(LocalDateTime.now());
        int count = hrsRegisterRecordMapper.updateById(record);
        if(count > 0) {
            result.setCode(ResultCode.SUCCESS);
        } else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("操作失败");
            return result;
        }
        return result;
    }

    /**
     * 查询科室该医生的患者已挂号信息
     * @param dto
     * @return
     */
    public List<HrsRegisterRecordVO> findPatientRegisterRecorsForDoctor(HrsRegisterDoctorDto dto){
        List<HrsRegisterRecordVO> registerRecords = new ArrayList<>();
        QueryWrapper<HrsRegisterRecord> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("DOCTOR_USER_ID",dto.getDoctorUserId())
                .ne("STATUS",2)
                .eq("DELETED",0)
                .eq("VISITING_DATE",dto.getRegisterDate());
        recordQueryWrapper.orderByAsc("STATUS").orderByAsc("TIME_FLAG");
        List<HrsRegisterRecord>  records = hrsRegisterRecordMapper.selectList(recordQueryWrapper);
        if(CollectionUtils.isNotEmpty(records)) {
            List<Long> patientIds = records.stream().map(HrsRegisterRecord::getPatientUserId).collect(Collectors.toList());
            QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("USER_ID",patientIds);
            List<SysUser> users = sysUserMapper.selectList(userQueryWrapper);
            Map<Long,SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser->SysUser));
            records.forEach(r->{
                HrsRegisterRecordVO recordVO = new HrsRegisterRecordVO();
                BeanUtils.copyProperties(r,recordVO);
                SysUser user = userMap.get(r.getPatientUserId());
                recordVO.setPatientName(user.getUserRealname());
                recordVO.setAge(user.getBirthday().until(LocalDate.now()).getYears()); //计算
                recordVO.setVisitingDateStr(DateUtil.localDateToString(r.getVisitingDate()));
                registerRecords.add(recordVO);
            });
        }
        return registerRecords;
    }

}
