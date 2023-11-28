package com.hrs.cloud.controller;

import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.core.utils.DateUtil;
import com.hrs.cloud.entity.HrsDoctorSchedule;
import com.hrs.cloud.entity.dto.HrsDoctorScheduleDto;
import com.hrs.cloud.entity.vo.UserVO;
import com.hrs.cloud.entity.vo.hrs.HrsDoctorScheduleVO;
import com.hrs.cloud.service.DepartmentService;
import com.hrs.cloud.service.ScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.time.LocalDate;

@Controller
@RequestMapping("/hrs")
public class ScheduleController extends BaseController {
    @Resource
    private ScheduleService scheduleService;

    @Resource
    private DepartmentService departmentService;

    /**
     * 排班列表页面
     *
     * @return
     */
    @GetMapping("/schedule/page")
    @ApiIgnore(value = "页面")
    public String toManagePage(Model model) {
        model.addAttribute("dptInfos",departmentService.findAllFirstDpt());
        return "/hrs/doctor-schedule";
    }

    /**
     * 排班数据
     * @return
     */
    @PostMapping(value = "/schedule/find")
    @ResponseBody
    public PageVO<HrsDoctorScheduleVO> findDoctorSchedules(@RequestBody HrsDoctorScheduleDto dto){
        return scheduleService.findDoctorSchedules(dto);
    }

    /**
     * /**
     * 排班新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/schedule/save")
    @ApiIgnore(value = "页面")
    public String toScheduleSavePage(Model model,@RequestParam(value = "scheduleType") String scheduleType) {
        model.addAttribute("dptInfos",departmentService.findAllFirstDpt());
        model.addAttribute("scdDateStr","");
        model.addAttribute("scheduleType",scheduleType);
        return "hrs/schedule-save";
    }

    /**
     * /**
     * 排班新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/schedule/modify/{id}")
    @ApiIgnore(value = "页面")
    public String toScheduleEditPage(Model model,@PathVariable Long id) {
        HrsDoctorScheduleVO scheduleVO = scheduleService.getDoctorScheduleDetail(id);
        model.addAttribute("scdDateStr",DateUtil.localDateToString(scheduleVO.getDateStart()) +" ~ "+ DateUtil.localDateToString(scheduleVO.getDateEnd()));
        model.addAttribute("scheduleType",scheduleVO.getScheduleType());
        model.addAttribute("ScdInfo",scheduleVO);
        return "hrs/schedule-save";
    }

    /**
     * 新增/编辑排班数据
     */
    @RequestMapping(value = "/schedule/edit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result editUser(HrsDoctorScheduleDto dto) {
        UserVO userVO = getCurrentUserVO();
        HrsDoctorSchedule doctorSchedule = new HrsDoctorSchedule();
        doctorSchedule.setId(dto.getId());
        doctorSchedule.setScheduleType(dto.getScheduleType());
        doctorSchedule.setDateStart(LocalDate.parse(dto.getDateStart()));
        doctorSchedule.setDateEnd(LocalDate.parse(dto.getDateEnd()));
        doctorSchedule.setDaysOfWeek(dto.getDaysOfWeek());
        doctorSchedule.setUserId(dto.getDoctorUserId());
        doctorSchedule.setMoningFlag(dto.getMoningFlag());
        doctorSchedule.setAfternoonFlag(dto.getAfternoonFlag());
        return scheduleService.modifyDoctorSchedule(doctorSchedule, userVO.getUserName());
    }

    /**
     *
     * 排班计划删除
     * @return
     */
    @PostMapping("/schedule/del/{id}")
    @ResponseBody
    public Result delScheduleInfo(@PathVariable Long id) {
        UserVO userVO = getCurrentUserVO();
        return scheduleService.delDoctorScheduleById(id, userVO.getUserName());
    }










}
