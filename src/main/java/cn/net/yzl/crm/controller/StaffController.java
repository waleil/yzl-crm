package cn.net.yzl.crm.controller;



import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.constant.EhrParamEnum;
import cn.net.yzl.crm.dto.ehr.*;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.StaffClient;
import cn.net.yzl.crm.sys.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "员工管理")
@RestController
@RequestMapping("/staff")
@Slf4j
public class StaffController {

    @Autowired
    private StaffClient staffClient;

    @Autowired
    private StaffService staffService;

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
        ComResponse<StaffScheduleInfoDto> response = staffClient.getStaffScheduleInfo(staffScheduleQueryDto);
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
        ComResponse comResponse = staffClient.robedClass(queryDto);
        return comResponse;
    }


    /**
     * 排班-根据员工工号和时间获取排班详情
     * @return
     */
    @ApiOperation(value="根据员工工号和时间获取排班详情",httpMethod = "get")
    @GetMapping("/schedule/getDetailByStaffNoAndTime")
    public ComResponse<StaffScheduleDetailDto> getDetailByStaffNoAndTime(@ApiParam(name = "staffNo",value ="员工工号") @RequestParam("staffNo") String staffNo,
                                                                         @ApiParam(name="time",value ="时间(yyyy-mm)") @RequestParam("time")String time){
        log.info("......StaffController.getDetailByStaffNoAndTime()开始,请求参数,staffNo={},time={}......",staffNo,time);
        if (StringUtils.isAnyBlank(staffNo,time)){
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse comResponse = staffClient.getDetailByStaffNoAndTime(staffNo,time);
        return comResponse;
    }

    /**
     * 员工画像  根据员工id获取员工基本信息
     * @return
     */
    @ApiOperation(value="员工画像  根据员工id获取员工基本信息",httpMethod = "get")
    @GetMapping("/getDetailsByNo")
    public ComResponse<StaffImageBaseInfoDto> getDetailsByNo(@ApiParam(name = "staffNo",value ="员工工号") @RequestParam("staffNo") String staffNo){
        log.info("......StaffController.getDetailsByNo()开始,请求参数,staffNo={}......",staffNo);
        if (StringUtils.isAnyBlank(staffNo)){
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        StaffImageBaseInfoDto staffImageBaseInfoByStaffNo = staffService.getStaffImageBaseInfoByStaffNo(staffNo);
        return ComResponse.success(staffImageBaseInfoByStaffNo);
    }

    /**
     * 员工列表  获取员工所有状态
     * @return
     */
    @ApiOperation(value="员工列表  获取员工所有状态",httpMethod = "get")
    @GetMapping("/getAllStuffStatus")
    public ComResponse<List<StaffStatusDto>> getAllStuffStatus(){
        log.info("......StaffController.getAllStuffStatus()开始,......");
        ComResponse<List<StaffStatusDto>> response = staffClient.getAllStuffStatus( EhrParamEnum.EHR_DICT_STAFF_STATUS);
        return response;
    }

    /**
     * 员工列表  根据岗位id获取部门岗位级别列表
     * @return
     */
    @ApiOperation(value="根据岗位id获取部门岗位级别列表",httpMethod = "get")
    @GetMapping("/getPostLevelListByDepartId")
    public ComResponse<EhrPostLevelDto> getPostLevelListByPostId(@ApiParam(name = "postId") @RequestParam("postId") Integer postId){
        log.info("......StaffController.getPostLevelListByPostId()开始,请求参数: postId={}......",postId);
        ComResponse<EhrPostLevelDto> response = staffClient.getPostLevelListByPostId(postId);
        return response;
    }

    /**
     * 员工列表  根据部门id获取部门岗位列表
     * @return
     */
    @ApiOperation(value="根据部门id获取部门岗位列表",httpMethod = "get")
    @GetMapping("/getPostListByDepartId")
    public ComResponse<List<EhrPostDto>> getPostListByDepartId(@ApiParam(name = "departId")@RequestParam("departId") Integer departId){
        log.info("......StaffController.getPostListByDepartId()开始,......");
        ComResponse<List<EhrPostDto>> response = staffClient.getPostListByDepartId(departId);
        return response;
    }

    /**
     * 员工列表  根据条件分页查询员工列表
     * @return
     */
    @ApiOperation(value="根据条件分页查询员工列表",httpMethod = "post")
    @PostMapping("/getStaffListByPage")
    public ComResponse<Page<EhrStaff>> getStaffListByPage(@RequestBody StaffQueryDto query){
        log.info("......StaffController.getStaffListByPage()开始, 请求参数:{}......",JsonUtil.toJsonStr(query));
        ComResponse<Page<EhrStaff>> response = staffClient.getStaffListByPage(query);
        return response;
    }


    /**
     * 根据员工id获取当前员工部门以及下属部门
     * @return
     */
    @ApiOperation(value="根据员工id获取当前员工部门以及下属部门",httpMethod = "get")
    @GetMapping("/getListByStaffNo")
    public ComResponse<List<EhrDepartDto>> getListByStaffNo(@ApiParam(name = "staffNo")@RequestParam("staffNo") String staffNo){
        log.info("......StaffController.getListByStaffNo(), 请求参数:{}......",staffNo);
        ComResponse<List<EhrDepartDto>> response = staffClient.getListByStaffNo(staffNo);
        return response;
    }



}
