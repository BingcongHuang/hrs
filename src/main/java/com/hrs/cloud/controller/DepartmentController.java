package com.hrs.cloud.controller;

import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.entity.HrsDepartmentFirst;
import com.hrs.cloud.entity.HrsDepartmentSecond;
import com.hrs.cloud.entity.dto.HrsDepartmentFirstDto;
import com.hrs.cloud.entity.dto.HrsDepartmentSecondDto;
import com.hrs.cloud.entity.vo.UserVO;
import com.hrs.cloud.entity.vo.hrs.HrsDepartmentFirstVO;
import com.hrs.cloud.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/hrs")
public class DepartmentController extends BaseController {
    @Resource
    private DepartmentService departmentService;

    /**
     * 科室列表页面
     *
     * @return
     */
    @GetMapping("/department/page")
    @ApiIgnore(value = "页面")
    public String toDepartMentPage() {
        return "/hrs/department";
    }

    /**
     * 一级科室数据
     * @return
     */
    @PostMapping(value = "/department/flist/find")
    @ResponseBody
    public PageVO<HrsDepartmentFirst> findAllFirstDepartments(@RequestBody HrsDepartmentFirstDto dto){
        return departmentService.findAllFirstDepartments(dto);
    }

    /**
     * /**
     * 一级科室新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/firstDpt/save")
    @ApiIgnore(value = "页面")
    public String toFirstDtpSavePage(Model model) {
        return "hrs/fdpt-save";
    }

    /**
     *
     * 一级科室编辑页面
     *
     * @param model
     * @return
     */
    @GetMapping("/firstDpt/modify/{id}")
    @ApiIgnore(value = "页面")
    public String toFirstDtpEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("fdptInfo", departmentService.getFirstDptById(id));
        return "hrs/fdpt-save";
    }

    /**
     *
     * 一级科室删除
     * @return
     */
    @PostMapping("/firstDpt/del/{id}")
    @ResponseBody
    public Result delFDptInfo(@PathVariable Integer id) {
        UserVO userVO = getCurrentUserVO();
        return departmentService.delFDptInfo(id, userVO.getUserName());
    }

    /**
     * 新增/编辑一级科室数据
     */
    @RequestMapping(value = "/fisrtDpt/edit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result editFDptInfo(HrsDepartmentFirst departmentFirst) {
        UserVO userVO = getCurrentUserVO();
        return departmentService.modifyFDptInfo(departmentFirst, userVO.getUserName());
    }


    /**
     *
     * 二级科室列表页面
     *
     * @param model
     * @return
     */
    @GetMapping("/secondDpt/page")
    @ApiIgnore(value = "页面")
    public String toSecondDtpPage(@RequestParam(value = "id") Long id,@RequestParam(value = "fName") String fName, Model model) {
        model.addAttribute("fid", id);
        model.addAttribute("fName", fName);
        return "hrs/second-department";
    }

    /**
     *
     * 二级科室新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/secondDpt/save")
    @ApiIgnore(value = "页面")
    public String toSecondDtpSave(@RequestParam(value = "id") Long id,@RequestParam(value = "fName") String fName, Model model) {
        model.addAttribute("fdptId", id);
        model.addAttribute("fName", fName);
        return "hrs/sdpt-save";
    }

    /**
     * 二级科室数据
     * @return
     */
    @PostMapping(value = "/department/slist/find")
    @ResponseBody
    public PageVO<HrsDepartmentSecond> findAllSecondDepartments(@RequestBody HrsDepartmentSecondDto dto){
        return departmentService.findAllSecondDepartments(dto);
    }

    /**
     *
     * 二级科室编辑页面
     *
     * @param model
     * @return
     */
    @PostMapping("/secondDpt/modify/{id}")
    @ApiIgnore(value = "页面")
    public String toSecondDtpSavePage(@PathVariable Long id,@RequestParam(value = "fid") Long fid,@RequestParam(value = "fName") String fName, Model model) {
        model.addAttribute("sdptInfo", departmentService.getSecondDptById(id));
        model.addAttribute("fid", fid);
        model.addAttribute("fName", fName);
        return "hrs/sdpt-save";
    }

    /**
     *
     * 一级科室删除
     * @return
     */
    @PostMapping("/secondDpt/del/{id}")
    @ResponseBody
    public Result delSDptInfo(@PathVariable Integer id) {
        UserVO userVO = getCurrentUserVO();
        return departmentService.delSDptInfo(id, userVO.getUserName());
    }

    /**
     * 新增/编辑二级科室数据
     */
    @RequestMapping(value = "/secondDpt/edit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result editDptInfo(HrsDepartmentSecond departmentSecond) {
        UserVO userVO = getCurrentUserVO();
        return departmentService.modifySDptInfo(departmentSecond, userVO.getUserName());
    }


    /**
     * 查询所有可用科室数据
     * @return
     */
    @PostMapping(value = "/department/list/findAll")
    @ResponseBody
    public List<HrsDepartmentFirstVO> findAllDepartments(){
        return departmentService.findAllDepartments(false);
    }

    /**
     * 查询所有可用科室数据
     * @return
     */
    @PostMapping(value = "/department/flist/findAll")
    @ResponseBody
    public Result findAllFirstDpt(){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setData(departmentService.findAllFirstDpt());
        return result;
    }

    /**
     * 查询所有可用科室数据
     * @return
     */
    @PostMapping(value = "/department/slist/{fid}/findAll")
    @ResponseBody
    public Result findAllSecondDpt(@PathVariable(value = "fid") Integer fid){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setData(departmentService.findAllSecondDpt(fid));
        return result;
    }

}
