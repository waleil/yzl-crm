package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.service.StaffLassoService;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.staff.dto.lasso.CalculationDto;
import cn.net.yzl.crm.staff.dto.lasso.StaffCrowdGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "员工圈选服务")
@RestController
@RequestMapping("staff")
@Slf4j
public class StaffLassoController {


    @Autowired
    private CrmStaffClient crmStaffClient;

    @Autowired
    private StaffLassoService staffLassoService;


    @ApiOperation(value = "试算")
    @PostMapping("v1/calculationDto")
    public ComResponse<Integer> calculationDto(@RequestBody CalculationDto calculationDto) throws Exception {
        Integer lassoCount = staffLassoService.calculationDto(calculationDto);
        return ComResponse.success(lassoCount);
    }


    @ApiOperation(value = "列表页 - 试算员工数量")
    @GetMapping("v1/trialStaffNo")
    public ComResponse<Integer> trialStaffNo(@RequestParam("groupId") long groupId) throws Exception {
        return staffLassoService.trialStaffNo(groupId);
    }


    // 员工圈选 保存
    @ApiOperation(value = "保存员工全选组", httpMethod = "POST")
    @PostMapping("v1/saveStaffCrowdGroup")
    public ComResponse<Boolean> saveStaffCrowdGroupDTO(@RequestBody StaffCrowdGroup staffCrowdGroup) {
        return crmStaffClient.saveStaffCrowdGroupDTO(staffCrowdGroup);
    }

    // 获取员工群组列表
    @ApiOperation(value = "分页获取圈选群组", httpMethod = "GET")
    @GetMapping("v1/getGroupListByPage")
    public ComResponse<Page<StaffCrowdGroup>> getGroupListByPage(
            @RequestParam(value = "crowdGroupName", required = true) @ApiParam(value = "群组名称") String crowdGroupName,
            @RequestParam(value = "status", required = false) @ApiParam(value = "状态") Integer status,
            @RequestParam(value = "startTime", required = false) @ApiParam(value = "开始时间(yyyy-MM-dd)") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(value = "endTime", required = false) @ApiParam(value = "结束时间(yyyy-MM-dd)") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam("pageNo") @ApiParam(value = "开始页") Integer pageNo,
            @RequestParam("pageSize") @ApiParam(value = "每页大小") Integer pageSize) {

        return crmStaffClient.getGroupListByPage(crowdGroupName, status, startTime, endTime, pageNo, pageSize);

    }

    @ApiOperation(value = "圈选组 1:启用,-1未启用")
    @GetMapping("v1/updateEnable")
    public ComResponse<Integer> updateEnable(@RequestParam(value = "enable", required = false) @ApiParam(value = "1:启用,-1:已结束") int enable,
                                             @RequestParam(value = "groupId", required = true) @ApiParam(value = "组id") long groupId) {
        return crmStaffClient.updateEnable(enable, groupId);
    }

    // 获取员工群组列表
    @ApiOperation(value = "获取圈选详情")
    @GetMapping("v1/getStaffCrowdGroup")
    public ComResponse<StaffCrowdGroup> getStaffCrowdGroupDTO(
            @RequestParam("groupId") long groupId) {

        return crmStaffClient.getStaffCrowdGroupDTO(groupId);

    }
}
