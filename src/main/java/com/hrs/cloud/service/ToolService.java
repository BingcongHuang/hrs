package com.hrs.cloud.service;

import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.dao.ToolInstMapper;
import com.hrs.cloud.entity.ToolInst;
import com.hrs.cloud.entity.dto.ToolInstDto;
import com.hrs.cloud.entity.dto.ToolSearchDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ToolService {
@Resource
private ToolInstMapper toolInstMapper;
    /**
     * 分页查询工具设备列表
     * @param dto
     * @return
     */
    public PageVO<ToolInst> findToolInfos(ToolInstDto dto) {
        PageVO<ToolInst> pageVO = new PageVO<ToolInst>();
        Page<ToolInst> page = new Page<>();
        page.setCurrent(dto.getPage());
        page.setSize(dto.getLimit());
        QueryWrapper<ToolInst> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED", 0);

        if(StringUtils.isNotEmpty(dto.getToolCode())) {
            queryWrapper.like("TOOL_CODE", dto.getToolCode());
        }
        if(StringUtils.isNotEmpty(dto.getToolName())) {
            queryWrapper.eq("TOOL_NAME", dto.getToolName());
        }
        if(StringUtils.isNotEmpty(dto.getToolEname())) {
            queryWrapper.eq("TOOL_ENAME", dto.getToolEname());
        }
        IPage<ToolInst> sysUserPage = toolInstMapper.selectPage(page, queryWrapper);


        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(sysUserPage.getRecords());
        pageVO.setCount(sysUserPage.getTotal());

        return pageVO;
    }


    /**
     * 编辑工具设备
     */
    public Result modifyToolInfo(ToolInst toolInst, Long updateUser){
        Result result = new Result();
        if(toolInst==null){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("参数不正确");
            return result;
        }
        int count;
        if(null != toolInst.getId()) {
            // 编辑
            toolInst.setUpdateBy(updateUser.toString());
            toolInst.setUpdateTime(LocalDateTime.now());
            count = toolInstMapper.updateById(toolInst);
        } else {
            // 新增
            toolInst.setStatus(0);
            toolInst.setDeleted(0);
            toolInst.setCreateBy(updateUser.toString());
            toolInst.setCreateTime(LocalDateTime.now());
            toolInst.setUpdateTime(LocalDateTime.now());
            count = toolInstMapper.insert(toolInst);
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

    public ToolInst getObjectById(Long id) {
        return toolInstMapper.selectById(id);
    }

    /**
     * 前端分页查询工具设备
     * @param dto
     * @return
     */
    public PageVO<ToolInst> findToolSearchResult(ToolSearchDto dto) {
        PageVO<ToolInst> pageVO = new PageVO<ToolInst>();
        Page<ToolInst> page = new Page<>();
        page.setCurrent(dto.getPage());
        page.setSize(dto.getLimit());
        QueryWrapper<ToolInst> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED", 0);
        queryWrapper.like("TOOL_CODE", dto.getStr())
                .or()
                .like("TOOL_NAME", dto.getStr())
                .orderByDesc("UPDATE_TIME");
        IPage<ToolInst> sysUserPage = toolInstMapper.selectPage(page, queryWrapper);
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(sysUserPage.getRecords());
        pageVO.setCount(sysUserPage.getTotal());
        return pageVO;
    }

    /**
     * 前端分页查询工具设备
     * @param dto
     * @return
     */
    public Integer findToolSearchCount(ToolSearchDto dto) {
        QueryWrapper<ToolInst> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETED", 0);
        queryWrapper.like("TOOL_CODE", dto.getStr())
                .or()
                .like("TOOL_NAME", dto.getStr());
        return toolInstMapper.selectCount(queryWrapper);
    }

    /**
     * 导入工具设备
     */
    @Transactional
    public Result importToolInfo(List<ToolInst> toolInsts, Long updateUser){
        Result result = new Result();
        if(CollectionUtils.isEmpty(toolInsts)){
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("Excel为空");
            return result;
        }
        toolInsts.forEach(inst->{
            inst.setStatus(0);
            inst.setDeleted(0);
            inst.setCreateBy(updateUser.toString());
            inst.setCreateTime(LocalDateTime.now());
            inst.setUpdateTime(LocalDateTime.now());
            toolInstMapper.insert(inst);
        });
        result.setCode(ResultCode.SUCCESS);
        return result;
    }

}
