package com.hrs.cloud.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.core.utils.DateUtil;
import com.hrs.cloud.core.utils.ID.IDUtils;
import com.hrs.cloud.core.utils.codec.MD5Util;
import com.hrs.cloud.dao.SysRoleMapper;
import com.hrs.cloud.dao.SysUserMapper;
import com.hrs.cloud.dao.SysUserRoleRelationMapper;
import com.hrs.cloud.entity.SysRole;
import com.hrs.cloud.entity.SysUser;
import com.hrs.cloud.entity.SysUserRoleRelation;
import com.hrs.cloud.entity.dto.SysUserDto;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentFirstVO;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentSecondVO;
import com.hrs.cloud.entity.vo.operatorUser.OperatorUser;
import com.hrs.cloud.entity.vo.operatorUser.OperatorUserListVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OperatorService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleRelationMapper sysUserRoleRelationMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private IDUtils idUtils;
    @Resource
    private DepartmentService departmentService;

    /**
     * 后台用户列表
     */

    public PageVO<OperatorUserListVO> getOperatorUserListVO(int page, int limit){
        PageVO<OperatorUserListVO> pageVO = new PageVO<OperatorUserListVO>();
        List<OperatorUserListVO> operatorUserListVOS = new ArrayList<>();
        Page<SysUser> pageInfo = new Page<>();
        pageInfo.setCurrent(page);
        pageInfo.setSize(limit);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETED", 0);
        IPage<SysUser> sysUserPage = sysUserMapper.selectPage(pageInfo, queryWrapper);
        if(CollectionUtils.isNotEmpty(sysUserPage.getRecords())){
            QueryWrapper<SysUserRoleRelation> roleRelationQueryWrapper = new QueryWrapper<>();
            List<SysUserRoleRelation> roleRelation = sysUserRoleRelationMapper.selectList(roleRelationQueryWrapper);
            Map<Long,Long> useRoleMap = roleRelation.stream().collect(Collectors.toMap(SysUserRoleRelation::getUserid,SysUserRoleRelation::getRoleid));
            QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
            List<SysRole> sysRoles = sysRoleMapper.selectList(sysRoleQueryWrapper);
            Map<Long,String> roleMap = sysRoles.stream().collect(Collectors.toMap(SysRole::getId,SysRole::getName));
            sysUserPage.getRecords().forEach(x->{
                OperatorUserListVO operatorUserListVO = new OperatorUserListVO();
                operatorUserListVO.setCreateTime(x.getCreateTime());
                operatorUserListVO.setLocked(x.getLocked());
                operatorUserListVO.setUpdateTime(x.getUpdateTime());
                operatorUserListVO.setUserEmail(x.getUserEmail());
                operatorUserListVO.setUserId(x.getUserId());
                operatorUserListVO.setUserName(x.getUserName());
                operatorUserListVO.setUserPhone(x.getUserTelphone());
                operatorUserListVO.setUserRealName(x.getUserRealname());
                operatorUserListVO.setUserSex(x.getUserSex());
                operatorUserListVO.setRole(useRoleMap.get(x.getUserId()));
                operatorUserListVO.setRoleName(roleMap.get(operatorUserListVO.getRole()));
                operatorUserListVO.setFirstDptId(x.getFirstDptId());
                operatorUserListVO.setSecondDptId(x.getSecondDptId());
                operatorUserListVO.setDoctorLevel(x.getDoctorLevel());
                operatorUserListVO.setIdType(x.getIdType());
                operatorUserListVO.setIdNum(x.getIdNum());
                operatorUserListVO.setDoctorIntroduction(x.getDoctorIntroduction());
                if(null != x.getBirthday()) {
                    operatorUserListVO.setBirthday(DateUtil.localDateToString(x.getBirthday()));
                }
                operatorUserListVOS.add(operatorUserListVO);
            });
            pageVO.setCode(0);
            pageVO.setMsg("");
            pageVO.setData(operatorUserListVOS);
            pageVO.setCount(sysUserPage.getTotal());
        }

        return pageVO;
    }

    /**
     * 编辑后台用户
     */
    public Result modifyOperatorUser(OperatorUser operatorUser, Long updateUser){
        Result result = new Result();
        if(operatorUser==null || null == operatorUser.getUserId() || StringUtils.isEmpty(operatorUser.getUserName())
        || StringUtils.isEmpty(operatorUser.getUserPass())){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("参数不正确");
            return result;
        }else{
            SysUser sysUser = new SysUser();
            sysUser.setUpdateTime(LocalDateTime.now());
            sysUser.setUpdateBy(updateUser);
            sysUser.setUserEmail(operatorUser.getUserEmail());
            if(StringUtils.isNotBlank(operatorUser.getUserPass())){
                sysUser.setUserPassword(MD5Util.MD5Encode(operatorUser.getUserPass(),"UTF-8"));
            }
            sysUser.setUserRealname(operatorUser.getUserRealName());
            sysUser.setUserTelphone(operatorUser.getUserPhone());
            sysUser.setUserSex(operatorUser.getUserSex());
//            sysUser.setId(operatorUser.getUserId());
            // 根据角色重置不需要的属性值
            if(operatorUser.getRoleId().intValue() == 2){
                //医生
                sysUser.setFirstDptId(operatorUser.getFirstDptId());
                sysUser.setSecondDptId(operatorUser.getSecondDptId());
                sysUser.setDoctorLevel(operatorUser.getDoctorLevel());
                sysUser.setDoctorIntroduction(operatorUser.getDoctorIntroduction());
            } else if(operatorUser.getRoleId().intValue() == 3){
                sysUser.setBirthday(LocalDate.parse(operatorUser.getBirthday()));
                sysUser.setIdType(operatorUser.getIdType());
                sysUser.setIdNum(operatorUser.getIdNum());
            }
            int i = sysUserMapper.update(sysUser, new QueryWrapper<SysUser>().eq("USER_ID", operatorUser.getUserId()));
            SysUserRoleRelation sysUserRole = new SysUserRoleRelation();
            sysUserRole.setRoleid(operatorUser.getRoleId());
            QueryWrapper<SysUserRoleRelation> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("USERID", operatorUser.getUserId());
            int m  = sysUserRoleRelationMapper.update(sysUserRole, userRoleQueryWrapper);

            if(i > 0 && m > 0){
                result.setCode(ResultCode.SUCCESS);
                result.setMessage("用户编辑成功");
                return result;
            }else{
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("用户编辑失败");
                return result;
            }
        }



    }
    /**
     * 删除后台用户
     */
    public Result delUser(Long userId,Long updateUser){
        Result result = new Result();

        SysUser sysUser = new SysUser();
        sysUser.setIsDeleted(false);
        sysUser.setUpdateBy(updateUser);
        sysUser.setUpdateTime(LocalDateTime.now());
//        sysUser.setId(userId);
        int i = sysUserMapper.update(sysUser,new QueryWrapper<SysUser>().eq("USER_ID", userId));
        if(i>0){
            result.setCode(ResultCode.SUCCESS);
            result.setMessage("删除用户成功");
            return result;
        }else{
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("删除用户失败");
            return result;
        }
    }

    /**
     * 后台用户锁定状态
     */
    public Result modifyUserLock(Long userId,Boolean locked,Long updateUser){
        Result result = new Result();

        SysUser sysUser = new SysUser();
        sysUser.setLocked(locked);
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setUpdateBy(updateUser);
//        sysUser.setId(userId);
        int i = sysUserMapper.update(sysUser,new QueryWrapper<SysUser>().eq("USER_ID", userId));
        if(i>0){
            result.setCode(ResultCode.SUCCESS);
            result.setMessage("操作成功");
            return result;
        }else{
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("操作失败");
            return result;
        }
    }

    /**
     * 添加后台用户
     */
    public Result addUser(OperatorUser operatorUser,Long createUser){
        Result result = new Result();
        int count = sysUserMapper.selectCount(
                new QueryWrapper<SysUser>().eq("USER_NAME", operatorUser.getUserName())
        );
        if(count>0){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("用户已经存在");
            return result;
        }else{

            SysUser sysUser = new SysUser();
            sysUser.setUpdateTime(LocalDateTime.now());
            sysUser.setUserEmail(operatorUser.getUserEmail());
            sysUser.setUserPassword(MD5Util.MD5Encode(operatorUser.getUserPass(),"UTF-8"));
            sysUser.setUserRealname(operatorUser.getUserRealName());
            sysUser.setUserTelphone(operatorUser.getUserPhone());
            sysUser.setUserSex(operatorUser.getUserSex());
            sysUser.setCreateTime(LocalDateTime.now());
            sysUser.setCreateBy(createUser);
            sysUser.setLocked(false);
            sysUser.setIsDeleted(false);
            Long userId = idUtils.getTicket();
            sysUser.setUserId(userId);
            sysUser.setUserName(operatorUser.getUserName());
            // 根据角色重置不需要的属性值
            if(operatorUser.getRoleId().intValue() == 2){
                //医生
                sysUser.setFirstDptId(operatorUser.getFirstDptId());
                sysUser.setSecondDptId(operatorUser.getSecondDptId());
                sysUser.setDoctorLevel(operatorUser.getDoctorLevel());
                sysUser.setDoctorIntroduction(operatorUser.getDoctorIntroduction());
            } else if(operatorUser.getRoleId().intValue() == 3){
                sysUser.setBirthday(LocalDate.parse(operatorUser.getBirthday()));
                sysUser.setIdType(operatorUser.getIdType());
                sysUser.setIdNum(operatorUser.getIdNum());
            }

            int i = sysUserMapper.insert(sysUser);

            SysUserRoleRelation sysUserRole = new SysUserRoleRelation();
            sysUserRole.setRoleid(operatorUser.getRoleId());
            sysUserRole.setUserid(userId);
            int m = sysUserRoleRelationMapper.insert(sysUserRole);

            if(i>0&&m>0){
                result.setCode(ResultCode.SUCCESS);
                result.setMessage("创建用户成功");
                return result;
            }else{
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("用户编辑失败");
                return result;
            }
        }

    }


    /**
     * 用户详情
     * @param userId
     * @return
     */
    public Result userDetail(Long userId){
        Result result = new Result();
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("USER_ID", userId));


        if(null != sysUser){
            SysUserRoleRelation sysUserRole = sysUserRoleRelationMapper.selectOne(
                    new QueryWrapper<SysUserRoleRelation>().eq("USER_ID", userId)
            );
            Long roleId =0L;
            if(null != sysUserRole){
                roleId = sysUserRole.getRoleid();
            }

            OperatorUser operatorUser = new OperatorUser();
            operatorUser.setUserSex(sysUser.getUserSex());
            operatorUser.setUserEmail(sysUser.getUserEmail());
            operatorUser.setUserName(sysUser.getUserName());
            operatorUser.setUserPhone(sysUser.getUserTelphone());
            operatorUser.setUserRealName(sysUser.getUserRealname());
            operatorUser.setRoleId(roleId);
            operatorUser.setUserId(sysUser.getUserId());
            result.setCode(ResultCode.SUCCESS);
            result.setMessage("");
            result.setData(operatorUser);
            return result;
        }
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("用户不存在");
            return result;

    }

    /**
     * 后台用户列表
     */

    public PageVO<OperatorUserListVO> findDoctorsByDto(SysUserDto dto){
        PageVO<OperatorUserListVO> pageVO = new PageVO<OperatorUserListVO>();
        List<OperatorUserListVO> operatorUserListVOS = new ArrayList<>();
        Page<SysUser> pageInfo = new Page<>();
        pageInfo.setCurrent(dto.getPage());
        pageInfo.setSize(dto.getLimit());
        QueryWrapper<SysUserRoleRelation> roleRelationQueryWrapper = new QueryWrapper<>();
        roleRelationQueryWrapper.eq("ROLEID",2); //角色为2（医生）的用户
        List<SysUserRoleRelation> roleRelation = sysUserRoleRelationMapper.selectList(roleRelationQueryWrapper);
        List<Long> userIds;
        if(CollectionUtils.isNotEmpty(roleRelation)) {
            userIds = roleRelation.stream().map(SysUserRoleRelation::getUserid).collect(Collectors.toList());
        } else {
            return pageVO;
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETED", 0);
        queryWrapper.in("USER_ID",userIds);
        if(StringUtils.isNoneBlank(dto.getUserRealname())){
            queryWrapper.like("USER_REALNAME",dto.getUserRealname());
        }
        if(StringUtils.isNoneBlank(dto.getUserTelphone())){
            queryWrapper.like("USER_TELPHONE",dto.getUserTelphone());
        }

        if(null != dto.getFirstDptId() && dto.getFirstDptId() > 0) {
            queryWrapper.eq("FIRST_DPT_ID", dto.getFirstDptId());
        }

        if(null != dto.getSecondDptId() && dto.getSecondDptId() > 0) {
            queryWrapper.eq("SECOND_DPT_ID", dto.getSecondDptId());
        }
        if(null != dto.getDoctorLevel() && dto.getDoctorLevel() > 0) {
            queryWrapper.eq("DOCTOR_LEVEL", dto.getDoctorLevel());
        }

        IPage<SysUser> sysUserPage = sysUserMapper.selectPage(pageInfo, queryWrapper);
        if(CollectionUtils.isNotEmpty(sysUserPage.getRecords())){
            List<HrsDepartmentFirstVO> departmentFirstVOS = departmentService.findAllDepartments(true);
             Map<Integer,HrsDepartmentFirstVO> fsdptMap = departmentFirstVOS.stream()
                    .collect(Collectors.toMap(HrsDepartmentFirstVO::getId,HrsDepartmentFirstVO -> HrsDepartmentFirstVO));
            sysUserPage.getRecords().forEach(x->{
                OperatorUserListVO operatorUserListVO = new OperatorUserListVO();
                operatorUserListVO.setCreateTime(x.getCreateTime());
                operatorUserListVO.setLocked(x.getLocked());
                operatorUserListVO.setUpdateTime(x.getUpdateTime());
                operatorUserListVO.setUserEmail(x.getUserEmail());
                operatorUserListVO.setUserId(x.getUserId());
                operatorUserListVO.setUserName(x.getUserName());
                operatorUserListVO.setUserPhone(x.getUserTelphone());
                operatorUserListVO.setUserRealName(x.getUserRealname());
                operatorUserListVO.setUserSex(x.getUserSex());
                operatorUserListVO.setFirstDptId(x.getFirstDptId());
                operatorUserListVO.setSecondDptId(x.getSecondDptId());
                operatorUserListVO.setDoctorLevel(x.getDoctorLevel());
                HrsDepartmentFirstVO firstVO = fsdptMap.get(x.getFirstDptId());
                operatorUserListVO.setFirstDptStr(firstVO.getDptName());

                if(CollectionUtils.isNotEmpty(firstVO.getSecondList())) {
                    Map<Integer,String> sdptMap = firstVO.getSecondList().stream()
                            .collect(Collectors.toMap(HrsDepartmentSecondVO::getId,HrsDepartmentSecondVO::getDptName));
                    operatorUserListVO.setSecondDptStr(sdptMap.get(x.getSecondDptId()));
                }

                operatorUserListVOS.add(operatorUserListVO);
            });
            pageVO.setCode(0);
            pageVO.setMsg("");
            pageVO.setData(operatorUserListVOS);
            pageVO.setCount(sysUserPage.getTotal());
        } else {
            pageVO.setCode(0);
            pageVO.setMsg("");
            pageVO.setData(operatorUserListVOS);
            pageVO.setCount(0L);
        }
        return pageVO;
    }

   public OperatorUserListVO getDoctorInfoById(Long id){
       OperatorUserListVO userListVO = new OperatorUserListVO();
       QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
       userQueryWrapper.eq("USER_ID",id);
       SysUser user = sysUserMapper.selectOne(userQueryWrapper);
       if(null != user && null == user.getFirstDptId() && null == user.getSecondDptId()){
           return userListVO;
       }
       userListVO.setUserName(user.getUserRealname());
       List<HrsDepartmentFirstVO> departmentFirstVOS = departmentService.findAllDepartments(true);
       Map<Integer,HrsDepartmentFirstVO> fsdptMap = departmentFirstVOS.stream()
               .collect(Collectors.toMap(HrsDepartmentFirstVO::getId,HrsDepartmentFirstVO -> HrsDepartmentFirstVO));
       HrsDepartmentFirstVO firstVO = fsdptMap.get(user.getFirstDptId());
       userListVO.setFirstDptStr(firstVO.getDptName());
       if(CollectionUtils.isNotEmpty(firstVO.getSecondList())) {
           Map<Integer,String> sdptMap = firstVO.getSecondList().stream()
                   .collect(Collectors.toMap(HrsDepartmentSecondVO::getId,HrsDepartmentSecondVO::getDptName));
           userListVO.setSecondDptStr(sdptMap.get(user.getSecondDptId()));
       }
       userListVO.setDoctorLevel(user.getDoctorLevel());
       userListVO.setFirstDptId(user.getFirstDptId());
       userListVO.setSecondDptId(user.getSecondDptId());
       userListVO.setUserId(user.getUserId());
        return userListVO;
    }

}
