package com.hrs.cloud.controller;

import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.core.utils.DateUtil;
import com.hrs.cloud.entity.HrsRegisterCharge;
import com.hrs.cloud.entity.HrsRegisterRule;
import com.hrs.cloud.entity.dto.HrsRegisterDoctorDto;
import com.hrs.cloud.entity.dto.HrsRegisterRuleDto;
import com.hrs.cloud.entity.vo.UserVO;
import com.hrs.cloud.entity.vo.hrs.HrsRegisterRecordVO;
import com.hrs.cloud.entity.vo.operatorUser.OperatorUserListVO;
import com.hrs.cloud.service.DepartmentService;
import com.hrs.cloud.service.OperatorService;
import com.hrs.cloud.service.RegisterService;
import com.hrs.cloud.service.ScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/hrs")
public class RegisterController extends BaseController {
    @Resource
    private DepartmentService departmentService;
    @Resource
    private RegisterService registerService;
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private OperatorService operatorService;

    /**
     * 挂号规则列表页面
     *
     * @return
     */
    @GetMapping("/register/rule")
    @ApiIgnore(value = "页面")
    public String toRegisterRulePage() {
        return "/hrs/register-rule";
    }


    /**
     * 挂号手续费列表页面
     *
     * @return
     */
    @GetMapping("/register/charges")
    @ApiIgnore(value = "页面")
    public String toRegisterChargesPage() {
        return "/hrs/register-charges";
    }

    /**
     * 挂号规则数据
     * @return
     */
    @PostMapping(value = "/register/rule/find")
    @ResponseBody
    public PageVO<HrsRegisterRule> findRegisterRules(){
        return registerService.findRegisterRules();
    }

    /**
     * 手续费数据
     * @return
     */
    @PostMapping(value = "/register/charges/find")
    @ResponseBody
    public PageVO<HrsRegisterCharge> findAllRegisterCharges(){
        return registerService.findAllRegisterCharges();
    }

    /**
     *
     * 挂号规则新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/register/rule/save")
    @ApiIgnore(value = "页面")
    public String toRegisterRuleSavePage(Model model) {
        model.addAttribute("ruleDateStr","");
        return "hrs/register-rule-save";
    }

    /**
     * /**
     * 挂号手续费新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/register/charges/save")
    @ApiIgnore(value = "页面")
    public String toRegisterChargesSavePage(Model model) {
        return "hrs/register-charges-save";
    }


    /**
     *
     * 挂号规则编辑页面
     *
     * @param model
     * @return
     */
    @GetMapping("/register/rule/modify/{id}")
    @ApiIgnore(value = "页面")
    public String toRegisterRuleEditPage(@PathVariable Long id, Model model) {
        HrsRegisterRule registerRule = registerService.getRegisterRuleById(id);
        model.addAttribute("RuleInfo", registerRule);
        model.addAttribute("ruleDateStr", DateUtil.localDateToString(registerRule.getDateStart()) +" ~ "+ DateUtil.localDateToString(registerRule.getDateEnd()));
        return "hrs/register-rule-save";
    }

    /**
     *
     * 手续费编辑页面
     *
     * @param model
     * @return
     */
    @GetMapping("/register/charges/modify/{id}")
    @ApiIgnore(value = "页面")
    public String toRegisterChargesEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("ChargesInfo", registerService.getRegisterChargeById(id));
        return "hrs/register-charges-save";
    }

    /**
     * 新增/编辑挂号规则数据
     */
    @RequestMapping(value = "/register/rule/edit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result editRegisterRule(HrsRegisterRuleDto dto) {
        UserVO userVO = getCurrentUserVO();
        HrsRegisterRule hrsRegisterRule = new HrsRegisterRule();
        hrsRegisterRule.setId(dto.getId());
        hrsRegisterRule.setDateStart(LocalDate.parse(dto.getDateStart()));
        hrsRegisterRule.setDateEnd(LocalDate.parse(dto.getDateEnd()));
        hrsRegisterRule.setUnusedLimit(dto.getUnusedLimit());
        hrsRegisterRule.setAddtionLimit(dto.getAddtionLimit());
        hrsRegisterRule.setEarlyDaysLimit(dto.getEarlyDaysLimit());




        return registerService.modifyRegisterRuleInfo(hrsRegisterRule, userVO.getUserName());
    }

    /**
     * 新增/编辑手续费数据
     */
    @RequestMapping(value = "/register/charges/edit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result editRegisterCharges(HrsRegisterCharge hrsRegisterCharge) {
        UserVO userVO = getCurrentUserVO();
        return registerService.modifyRegisterChargeInfo(hrsRegisterCharge, userVO.getUserName());
    }

    /**
     *
     * 规则删除
     * @return
     */
    @PostMapping("/register/rule/del/{id}")
    @ResponseBody
    public Result delRegisterRule(@PathVariable Long id) {
        UserVO userVO = getCurrentUserVO();
        return registerService.delRegisterRuleInfo(id, userVO.getUserName());
    }

    /**
     *
     * 手续费删除
     * @return
     */
    @PostMapping("/register/charges/del/{id}")
    @ResponseBody
    public Result delRegisterCharges(@PathVariable Long id) {
        UserVO userVO = getCurrentUserVO();
        return registerService.delRegisterChargeInfo(id, userVO.getUserName());
    }

    /**
     * /**
     * 挂号查询页面
     *
     */
    @GetMapping("/patient/register/toPage")
    @ApiIgnore(value = "页面")
    public String toRegisterPage(Model model) {
        model.addAttribute("user", getCurrentUserVO());
        model.addAttribute("dptInfos",departmentService.findAllFirstDpt());
        model.addAttribute("earlyDates",registerService.getRegisterDateByRule());
        return "hrs/patient";
    }


    /**
     * /**
     * 挂号查询医生结果
     * @return
     */
    @GetMapping("/patient/register/search")
    public String findRegisterDoctors(HrsRegisterDoctorDto dto,Model model) {
        model.addAttribute("doctors", scheduleService.findRegisterDoctors(dto));
        return "hrs/doctor-list";
    }

    /**
     *
     * 患者挂号信息
     * @return
     */
    @GetMapping("/patient/register/find")
    public String findRegisterRecords(HrsRegisterDoctorDto dto,Model model) {
        model.addAttribute("rgsRcords", registerService.findPatientRegisterRecors(dto));
        return "hrs/patient-register-list";
    }


    /**
     * 新增挂号
     */
    @RequestMapping(value = "/register/add", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result doRegister(HrsRegisterDoctorDto dto) {
        UserVO userVO = getCurrentUserVO();
        return registerService.doRegister(dto, userVO.getUserName());
    }

    /**
     * 新增挂号
     */
    @RequestMapping(value = "/register/cnl", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result cnlRegister(HrsRegisterDoctorDto dto) {
        UserVO userVO = getCurrentUserVO();
        return registerService.cnlRegister(dto, userVO.getUserName());
    }

    /**
     * 消号
     */
    @RequestMapping(value = "/register/consume", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result consumeRegister(HrsRegisterDoctorDto dto) {
        UserVO userVO = getCurrentUserVO();
        return registerService.consumeRegister(dto, userVO.getUserName());
    }


    /**
     * 科室列表页面
     *
     * @return
     */
    @GetMapping("/register/patients/page")
    @ApiIgnore(value = "页面")
    public String todptPatientsPage(Model model) {
        UserVO userVO = getCurrentUserVO();
        OperatorUserListVO vo = operatorService.getDoctorInfoById(userVO.getUserId());
        model.addAttribute("doctor",vo);
        model.addAttribute("registerDate",DateUtil.localDateToString(LocalDate.now()));
        return "/hrs/dpt-patient";
    }

    /**
     *
     * @return
     */
    @PostMapping(value = "/register/patients/find")
    @ResponseBody
    public PageVO<HrsRegisterRecordVO> findPatientRegisterRecorsForDoctor(@RequestBody HrsRegisterDoctorDto dto){
        UserVO userVO = getCurrentUserVO();
        dto.setDoctorUserId(userVO.getUserId());
        PageVO<HrsRegisterRecordVO> pageVO = new PageVO<>();
        List<HrsRegisterRecordVO> recordVOS = registerService.findPatientRegisterRecorsForDoctor(dto);
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(recordVOS);
        pageVO.setCount((long) recordVOS.size());
        return pageVO;
    }


}
