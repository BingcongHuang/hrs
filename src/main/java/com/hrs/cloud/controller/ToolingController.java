package com.hrs.cloud.controller;

import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.core.utils.IpUtils;
import com.hrs.cloud.entity.ToolInst;
import com.hrs.cloud.entity.dto.ToolInstDto;
import com.hrs.cloud.entity.dto.ToolSearchDto;
import com.hrs.cloud.entity.vo.UserVO;
import com.hrs.cloud.helper.ExcelReader;
import com.hrs.cloud.helper.RedisHelper;
import com.hrs.cloud.service.ToolService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

/**
 * 工具设备
 */
@Controller
@RequestMapping("/tool")
public class ToolingController extends BaseController {
    @Resource
    private ToolService toolService;
    @Resource
    private RedisHelper redisUtils;
    private static final String REQUEST_LIMIT_PRE = "request_limit_";
    private static final Integer REQUEST_LIMIT = 20;
    private static final String ZERO = "0";

    private static Logger logger = Logger.getLogger(ToolingController.class.getName());

    /**
     * 工具设备列表页面
     *
     * @return
     */
    @GetMapping("/page")
    @ApiIgnore(value = "页面")
    public String toManagePage() {
        return "tool/toolList";
    }

    /**
     * 分页查询工具设备数据
     * @return
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public PageVO<ToolInst> findToolInst(@RequestBody ToolInstDto dto){
        return toolService.findToolInfos(dto);
    }

    /**
     * /**
     * 工具设备页面
     *
     * @param model
     * @return
     */
    @GetMapping("/save")
    @ApiIgnore(value = "页面")
    public String toToolInstSavePage(Model model) {
        return "tool/tool-save";
    }

    /**
     * /**
     * 工具设备页面
     *
     * @param model
     * @return
     */
    @GetMapping("/modify/{id}")
    @ApiIgnore(value = "页面")
    public String toToolInstSavePage(@PathVariable Long id, Model model) {
        model.addAttribute("toolInst", toolService.getObjectById(id));
        return "tool/tool-save";
    }

    /**
     * 新增/编辑工具设备数据
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result editUser(ToolInst toolInst) {
        UserVO userVO = getCurrentUserVO();
        return toolService.modifyToolInfo(toolInst, userVO.getUserId());
    }

    /**
     * /**
     * 工具设备查询页面
     *
     */
    @GetMapping("/search/toPage")
    @ApiIgnore(value = "页面")
    public String toToolingSerarchPage(Model model) {
        model.addAttribute("user", getCurrentUserVO());
        return "tool/search";
    }

    /**
     * /**
     * 工具设备页面
     *
     * @param model
     * @return
     */
    @PostMapping("/search")
    @ApiIgnore(value = "页面")
    public String findToolSearchResult(ToolSearchDto dto, Model model, HttpServletRequest request) {
        dto.setLimit(2);
        UserVO userVO = getCurrentUserVO();
        if(null != userVO) {
            dto.setUserId(userVO.getUserId().toString());
            model.addAttribute("userId", userVO.getUserId());
        } else {
            // 未登录用户，根据ip限制访问，1天最多20次
            String ip = IpUtils.getIpAddr(request);
            Integer requestCount = redisUtils.get(REQUEST_LIMIT_PRE + ip,Integer.class);
            if(null != requestCount && requestCount >= REQUEST_LIMIT) {
                model.addAttribute("requestLimit", "1");
                return "/tool/result";
            } else {
                model.addAttribute("requestLimit", "0");
                if(null == requestCount) {
                    redisUtils.setex(REQUEST_LIMIT_PRE + ip,86400,1);
                } else if(ZERO.equals(dto.getIsAppend())) {
                    redisUtils.incr(REQUEST_LIMIT_PRE + ip);
                }
            }
        }
        PageVO<ToolInst> pages =  toolService.findToolSearchResult(dto);
        model.addAttribute("toolResult", pages.getData());
        model.addAttribute("isApppend", dto.getIsAppend());
        model.addAttribute("dataCount", dto.getPage() * dto.getLimit());
        model.addAttribute("totalCount", pages.getCount());
        return "/tool/result";
    }

    /**
     * /**
     * 工具设备页面
     * @return
     */
    @PostMapping("/search/count")
    @ApiIgnore(value = "dto")
    @ResponseBody
    public R findToolSearchResult(ToolSearchDto dto) {
        return R.ok(toolService.findToolSearchCount(dto));
    }


    @PostMapping("/uploadExcel")
    @ResponseBody
    public Result uploadImage(MultipartFile file) {
        Result result = new Result();
        UserVO userVO = getCurrentUserVO();
        // 检查前台数据合法性
        if (null == file || file.isEmpty()) {
            logger.warning("上传的Excel商品数据文件为空！上传时间：" + new Date());
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("导入文件为空");
            return result;
        }

        try {
            // 解析Excel
            List<ToolInst> toolInsts = ExcelReader.readExcel(file);
            return toolService.importToolInfo(toolInsts, userVO.getUserId());
        } catch (Exception e) {
            logger.warning("上传的Excel商品数据文件为空！上传时间：" + new Date());
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("导入文件异常");
            return result;
        }

    }

}
