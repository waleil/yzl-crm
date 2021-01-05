package cn.net.yzl.crm.controller;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.dto.ehr.EhrRobedQueryDto;
import cn.net.yzl.crm.dto.ehr.StaffScheduleInfoDto;
import cn.net.yzl.crm.dto.ehr.StaffScheduleQueryDto;
import cn.net.yzl.crm.service.micservice.StaffClient;
import cn.net.yzl.crm.sys.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "员工管理")
@RestController
@RequestMapping("/staff")
@Slf4j
public class StaffController {

    @Autowired
    private StaffClient staffService;

    /**
     * 获取员工排班信息
     * @return
     */
    @ApiOperation(value="获取员工排班信息",httpMethod = "post")
    @PostMapping("/getStaffScheduleInfo")
   public ComResponse<StaffScheduleInfoDto> getStaffScheduleInfo(@RequestBody StaffScheduleQueryDto staffScheduleQueryDto){
        log.info("......StaffController.getStaffScheduleInfo()开始,请求参数:{}......", JsonUtil.toJsonStr(staffScheduleQueryDto));
        if (null==staffScheduleQueryDto||staffScheduleQueryDto.getPageSize()==null||staffScheduleQueryDto.getPageNo()==null){
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse<StaffScheduleInfoDto> response = staffService.getStaffScheduleInfo(staffScheduleQueryDto);
        return response;
    }


    /**
     * 员工抢班
     * @return
     */
    @ApiOperation(value="员工抢班",httpMethod = "post")
    @PostMapping("/schedule/robedClass")
    public ComResponse<StaffScheduleInfoDto> robedClass(@RequestBody EhrRobedQueryDto queryDto){
        log.info("......StaffController.robedClass()开始,请求参数,{}......",JsonUtil.toJsonStr(queryDto));
        if (null==queryDto||StringUtils.isAnyBlank(queryDto.getStaffScheduleId(),queryDto.getTime(),queryDto.getType())){
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse comResponse = staffService.robedClass(queryDto);
        return comResponse;
    }


}
