package com.hrs.cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.dao.HrsDepartmentFirstMapper;
import com.hrs.cloud.dao.HrsDepartmentSecondMapper;
import com.hrs.cloud.entity.HrsDepartmentFirst;
import com.hrs.cloud.entity.HrsDepartmentSecond;
import com.hrs.cloud.entity.dto.HrsDepartmentFirstDto;
import com.hrs.cloud.entity.dto.HrsDepartmentSecondDto;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentFirstVO;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentSecondVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    @Resource
    private HrsDepartmentFirstMapper hrsDepartmentFirstMapper;
    @Resource
    private HrsDepartmentSecondMapper hrsDepartmentSecondMapper;

    /**
     * 查询全量一级科室信息
     * @return
     */
    public PageVO<HrsDepartmentFirst> findAllFirstDepartments(HrsDepartmentFirstDto dto) {
        // 返回值定义
        PageVO<HrsDepartmentFirst> pageVO = new PageVO<>();
        Page<HrsDepartmentFirst> page = new Page<>();
        page.setCurrent(dto.getPage());
        page.setSize(dto.getLimit());
        // mybatis_plus的写法
        QueryWrapper<HrsDepartmentFirst> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED",0);
        queryWrapper.orderByAsc("SORT_NUM");
        if(StringUtils.isNoneBlank(dto.getDptCname())) {
            queryWrapper.like("D_CNAME",dto.getDptCname());
        }

        if(null != dto.getStatus() && dto.getStatus().intValue() == 1) {
            queryWrapper.eq("STATUS",1);
        }

        IPage<HrsDepartmentFirst> departmentFirsts = hrsDepartmentFirstMapper.selectPage(page,queryWrapper);

        // 返回
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(departmentFirsts.getRecords());
        pageVO.setCount(departmentFirsts.getTotal());
        return pageVO;
    }

    /**
     * 根据id查询一级科室信息
     * @param id
     * @return
     */
    public HrsDepartmentFirst getFirstDptById(Long id) {
        return hrsDepartmentFirstMapper.selectById(id);
    }

    /**
     * 保存编辑一级科室信息
     */
    public Result modifyFDptInfo(HrsDepartmentFirst departmentFirst, String updateUser){
        Result result = new Result();
        if(departmentFirst==null){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("参数不正确");
            return result;
        }
        int count;
        if(null != departmentFirst.getId()) {
            // 编辑
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsDepartmentFirst> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .eq("DPT_CNAME",departmentFirst.getDptCname())
                    .ne("ID",departmentFirst.getId());
            int cekCount = hrsDepartmentFirstMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("已存在科室："+departmentFirst.getDptCname()+",请确认！");
                return result;
            }
            departmentFirst.setUpdateBy(updateUser.toString());
            departmentFirst.setUpdateTime(LocalDateTime.now());
            count = hrsDepartmentFirstMapper.updateById(departmentFirst);
        } else {
            // 新增
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsDepartmentFirst> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                        .eq("DPT_CNAME",departmentFirst.getDptCname());
            int cekCount = hrsDepartmentFirstMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("已存在科室："+departmentFirst.getDptCname()+",请勿重复添加");
                return result;
            }

            departmentFirst.setStatus(0);
            departmentFirst.setDeleted(0);
            departmentFirst.setCreateBy(updateUser);
            departmentFirst.setUpdateBy(updateUser);
            departmentFirst.setCreateTime(LocalDateTime.now());
            departmentFirst.setUpdateTime(LocalDateTime.now());
            count = hrsDepartmentFirstMapper.insert(departmentFirst);
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
     * 逻辑删除一级科室
     * @param id
     * @param updateUser
     * @return
     */
    public Result delFDptInfo(Integer id,String updateUser) {
        Result result = new Result();
        HrsDepartmentFirst record = new HrsDepartmentFirst();
        //逻辑删除
        record.setId(id);
        record.setDeleted(1);
        record.setUpdateBy(updateUser);
        record.setUpdateTime(LocalDateTime.now());
        int count = hrsDepartmentFirstMapper.updateById(record);
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
     * 查询全量二级科室信息
     * @return
     */
    public PageVO<HrsDepartmentSecond> findAllSecondDepartments(HrsDepartmentSecondDto dto) {
        // 返回值定义
        PageVO<HrsDepartmentSecond> pageVO = new PageVO<>();
        Page<HrsDepartmentSecond> page = new Page<>();
        page.setCurrent(dto.getPage());
        page.setSize(dto.getLimit());
        // mybatis_plus的写法
        QueryWrapper<HrsDepartmentSecond> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED",0);
        queryWrapper.orderByAsc("SORT_NUM");
        if(null != dto.getFdptId()) {
            queryWrapper.eq("FDPT_ID",dto.getFdptId());
        }

        if(StringUtils.isNoneBlank(dto.getDptCname())) {
            queryWrapper.like("D_CNAME",dto.getDptCname());
        }

        if(null != dto.getStatus() && dto.getStatus().intValue() == 1) {
            queryWrapper.eq("STATUS",1);
        }

        IPage<HrsDepartmentSecond> departmentFirsts = hrsDepartmentSecondMapper.selectPage(page,queryWrapper);

        // 返回
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(departmentFirsts.getRecords());
        pageVO.setCount(departmentFirsts.getTotal());
        return pageVO;
    }

    /**
     * 根据id查询二级科室信息
     * @param id
     * @return
     */
    public HrsDepartmentSecond getSecondDptById(Long id) {
        return hrsDepartmentSecondMapper.selectById(id);
    }

    /**
     * 保存编辑二级科室信息
     */
    public Result modifySDptInfo(HrsDepartmentSecond departmentSecond, String updateUser){
        Result result = new Result();
        if(departmentSecond==null){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("参数不正确");
            return result;
        }
        int count;
        if(null != departmentSecond.getId()) {
            // 编辑
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsDepartmentSecond> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .eq("DPT_CNAME",departmentSecond.getDptCname())
                    .ne("ID",departmentSecond.getId());
            int cekCount = hrsDepartmentSecondMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("已存在科室："+departmentSecond.getDptCname()+",请确认！");
                return result;
            }
            departmentSecond.setUpdateBy(updateUser.toString());
            departmentSecond.setUpdateTime(LocalDateTime.now());
            count = hrsDepartmentSecondMapper.updateById(departmentSecond);
        } else {
            // 新增
            // 判断一级科室名是否有重复的。
            QueryWrapper<HrsDepartmentSecond> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETED",0)
                    .eq("DPT_CNAME",departmentSecond.getDptCname());
            int cekCount = hrsDepartmentSecondMapper.selectCount(queryWrapper);
            if(cekCount > 0) {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("已存在科室："+departmentSecond.getDptCname()+",请勿重复添加");
                return result;
            }

            departmentSecond.setStatus(0);
            departmentSecond.setDeleted(0);
            departmentSecond.setCreateBy(updateUser);
            departmentSecond.setUpdateBy(updateUser);
            departmentSecond.setCreateTime(LocalDateTime.now());
            departmentSecond.setUpdateTime(LocalDateTime.now());
            count = hrsDepartmentSecondMapper.insert(departmentSecond);
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
     * 逻辑删除一级科室
     * @param id
     * @param updateUser
     * @return
     */
    public Result delSDptInfo(Integer id,String updateUser) {
        Result result = new Result();
        HrsDepartmentSecond record = new HrsDepartmentSecond();
        //逻辑删除
        record.setId(id);
        record.setDeleted(1);
        record.setUpdateBy(updateUser);
        record.setUpdateTime(LocalDateTime.now());
        int count = hrsDepartmentSecondMapper.updateById(record);
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
     * 全量查询科室信息
     * @return
     */
    public List<HrsDepartmentFirstVO>  findAllDepartments(Boolean hasDelData) {
        List<HrsDepartmentFirstVO> result = new ArrayList<>();
        QueryWrapper<HrsDepartmentFirst> queryWrapper = new QueryWrapper<>();
        if (null != hasDelData && !hasDelData) {
            queryWrapper.eq("DELETED",0);
        }
        queryWrapper.orderByAsc("SORT_NUM");
        // 查询所有一级科室
        List<HrsDepartmentFirst> hrsDepartmentFirsts = hrsDepartmentFirstMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(hrsDepartmentFirsts)) {
            for(HrsDepartmentFirst fdpt : hrsDepartmentFirsts){
                HrsDepartmentFirstVO firstVO = new HrsDepartmentFirstVO();
                firstVO.setId(fdpt.getId());
                firstVO.setDptName(fdpt.getDptCname());
                firstVO.setSortNum(fdpt.getSortNum());
                List<HrsDepartmentSecondVO> secondVOS = new ArrayList<>();
                QueryWrapper<HrsDepartmentSecond> sdptQueryWrapper = new QueryWrapper<>();
                sdptQueryWrapper.eq("DELETED",0)
                                .eq("FDPT_ID",fdpt.getId());
                queryWrapper.orderByAsc("SORT_NUM");
                List<HrsDepartmentSecond> hrsDepartmentSeconds = hrsDepartmentSecondMapper.selectList(sdptQueryWrapper);
                if(CollectionUtils.isNotEmpty(hrsDepartmentSeconds)){
                    for(HrsDepartmentSecond sdpt : hrsDepartmentSeconds){
                        HrsDepartmentSecondVO secondVO = new HrsDepartmentSecondVO();
                        secondVO.setId(sdpt.getId());
                        secondVO.setDptName(sdpt.getDptCname());
                        secondVO.setSortNum(sdpt.getSortNum());
                        secondVO.setDptRemark(sdpt.getDptRemark());
                        secondVOS.add(secondVO);
                    }
                    firstVO.setSecondList(secondVOS);
                }
                result.add(firstVO);
            }
        }
        return result;
    }

    /**
     * 全量查询所有一级科室信息，不分页
     * @return
     */
    public List<HrsDepartmentFirstVO>  findAllFirstDpt() {
        List<HrsDepartmentFirstVO> result = new ArrayList<>();
        QueryWrapper<HrsDepartmentFirst> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED",0);
        queryWrapper.orderByAsc("SORT_NUM");
        // 查询所有一级科室
        List<HrsDepartmentFirst> hrsDepartmentFirsts = hrsDepartmentFirstMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(hrsDepartmentFirsts)) {
            for(HrsDepartmentFirst fdpt : hrsDepartmentFirsts){
                HrsDepartmentFirstVO firstVO = new HrsDepartmentFirstVO();
                firstVO.setId(fdpt.getId());
                firstVO.setDptName(fdpt.getDptCname());
                firstVO.setSortNum(fdpt.getSortNum());
                result.add(firstVO);
            }
        }
        return result;
    }

    /**
     * 全量查询所有二级科室信息，不分页
     * @return
     */
    public List<HrsDepartmentSecondVO>  findAllSecondDpt(Integer fid) {
        List<HrsDepartmentSecondVO> secondVOS = new ArrayList<>();
        QueryWrapper<HrsDepartmentSecond> sdptQueryWrapper = new QueryWrapper<>();
        sdptQueryWrapper.eq("DELETED",0)
                .eq("FDPT_ID",fid);
        List<HrsDepartmentSecond> hrsDepartmentSeconds = hrsDepartmentSecondMapper.selectList(sdptQueryWrapper);
        if(CollectionUtils.isNotEmpty(hrsDepartmentSeconds)){
            for(HrsDepartmentSecond sdpt : hrsDepartmentSeconds){
                HrsDepartmentSecondVO secondVO = new HrsDepartmentSecondVO();
                secondVO.setId(sdpt.getId());
                secondVO.setDptName(sdpt.getDptCname());
                secondVO.setSortNum(sdpt.getSortNum());
                secondVO.setDptRemark(sdpt.getDptRemark());
                secondVOS.add(secondVO);
            }
        }
        return secondVOS;
    }

}
