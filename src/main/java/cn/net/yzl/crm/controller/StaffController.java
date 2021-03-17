package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.constant.EhrParamEnum;
import cn.net.yzl.crm.dto.ehr.EhrDepartDto;
import cn.net.yzl.crm.dto.ehr.EhrPostDto;
import cn.net.yzl.crm.dto.ehr.EhrPostLevelDto;
import cn.net.yzl.crm.dto.ehr.EhrRobedQueryDto;
import cn.net.yzl.crm.dto.ehr.EhrStaff;
import cn.net.yzl.crm.dto.ehr.MarketTargetDto;
import cn.net.yzl.crm.dto.ehr.StaffQueryDto;
import cn.net.yzl.crm.dto.ehr.StaffScheduleDetailDto;
import cn.net.yzl.crm.dto.ehr.StaffScheduleInfoDto;
import cn.net.yzl.crm.dto.ehr.StaffScheduleQueryDto;
import cn.net.yzl.crm.dto.ehr.StaffTrainDto;
import cn.net.yzl.crm.dto.ehr.SysDictDto;
import cn.net.yzl.crm.dto.staff.CallnfoCriteriaTO;
import cn.net.yzl.crm.dto.staff.OrderCriteriaDto;
import cn.net.yzl.crm.dto.staff.StaffCallRecord;
import cn.net.yzl.crm.dto.staff.StaffChangeRecordDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.StaffDetail;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.WorkOrderClient;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OderListResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "员工管理")
@RestController
@RequestMapping("/staff")
@Slf4j
public class StaffController {

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private StaffService staffService;


    @Autowired
    private WorkOrderClient workOrderClient;

    /**
     * 获取员工排班信息
     *
     * @return
     */
    @ApiOperation(value = "获取员工排班信息", httpMethod = "POST")
    @PostMapping("/getStaffScheduleInfo")
    public ComResponse<StaffScheduleInfoDto> getStaffScheduleInfo(@RequestBody StaffScheduleQueryDto staffScheduleQueryDto) {
        log.info("......StaffController.getStaffScheduleInfo()开始,请求参数:{}......", JsonUtil.toJsonStr(staffScheduleQueryDto));

        if (null == staffScheduleQueryDto || staffScheduleQueryDto.getPageSize() == null || staffScheduleQueryDto.getPageNo() == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        staffScheduleQueryDto.setStaffNo(QueryIds.userNo.get());
        ComResponse<StaffScheduleInfoDto> response = ehrStaffClient.getStaffBaseInfo(staffScheduleQueryDto);
        return response;
    }


    /**
     * 员工抢班
     *
     * @return
     */
    @ApiOperation(value = "员工抢班", httpMethod = "POST")
    @PostMapping("/schedule/robedClass")
    public ComResponse<StaffScheduleInfoDto> robedClass(@RequestBody EhrRobedQueryDto queryDto) {
        log.info("......StaffController.robedClass()开始,请求参数,{}......", JsonUtil.toJsonStr(queryDto));
        if (null == queryDto || StringUtils.isAnyBlank(queryDto.getStaffScheduleId(), queryDto.getTime(), queryDto.getType())) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse comResponse = ehrStaffClient.robedClass(queryDto);
        return comResponse;
    }


    /**
     * 排班-根据员工工号和时间获取排班详情
     *
     * @return
     */
    @ApiOperation(value = "根据员工工号和时间获取排班详情", httpMethod = "GET")
    @GetMapping("/schedule/getDetailByStaffNoAndTime")
    public ComResponse<StaffScheduleDetailDto> getDetailByStaffNoAndTime(@ApiParam(name = "staffNo", value = "员工工号") @RequestParam("staffNo") String staffNo,
                                                                         @ApiParam(name = "time", value = "时间(yyyy-mm)") @RequestParam("time") String time) {
        log.info("......StaffController.getDetailByStaffNoAndTime()开始,请求参数,staffNo={},time={}......", staffNo, time);
        if (StringUtils.isAnyBlank(staffNo, time)) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        ComResponse comResponse = ehrStaffClient.getDetailByStaffNoAndTime(staffNo, time);
        return comResponse;
    }

    /**
     * 员工画像  根据员工id获取员工基本信息
     *
     * @return
     */
    @ApiOperation(value = "员工画像  根据员工id获取员工基本信息", httpMethod = "GET")
    @GetMapping("/getDetailsByNo")
    public ComResponse<StaffImageBaseInfoDto> getDetailsByNo(@ApiParam(name = "staffNo", value = "员工工号") @RequestParam("staffNo") String staffNo) {
        log.info("......StaffController.getDetailsByNo()开始,请求参数,staffNo={}......", staffNo);
        if (StringUtils.isBlank(staffNo)) {
            //未传 默认为当前用户 从头消息中获取
            staffNo = QueryIds.userNo.get();
        }
        StaffImageBaseInfoDto staffImageBaseInfoByStaffNo = staffService.getStaffImageBaseInfoByStaffNo(staffNo);
        return ComResponse.success(staffImageBaseInfoByStaffNo);
    }


    /**
     * 员工画像  根据条件获取员工通话记录
     *
     * @return
     */
    @ApiOperation(value = "员工画像  根据条件获取员工通话记录", httpMethod = "POST")
    @PostMapping("/getCallRecordByStaffNo")
    public ComResponse<Page<StaffCallRecord>> getCallRecordByStaffNo(@RequestBody CallnfoCriteriaTO callnfoCriteriaTO) {
        log.info("......StaffController.getCallRecordByStaffNo()开始,请求参数,staffNo={}......", JsonUtil.toJsonStr(callnfoCriteriaTO));
        if (callnfoCriteriaTO == null || callnfoCriteriaTO.getPageSize() == 0 || callnfoCriteriaTO.getPageNo() == 0) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        if (StringUtils.isBlank(callnfoCriteriaTO.getStaffNo())) {
            //未传 默认为当前用户 从头消息中获取
            callnfoCriteriaTO.setStaffNo(QueryIds.userNo.get());
        }
        ComResponse<Page<StaffCallRecord>> response = workOrderClient.getCallRecordByStaffNo(callnfoCriteriaTO);
        return response;
    }

    /**
     * 员工画像  获取员工商品旅程
     *
     * @return
     */
    @ApiOperation(value = "员工画像  获取员工商品旅程", httpMethod = "GET")
    @GetMapping("/getStaffProductTravel")
    public ComResponse<Page<StaffProdcutTravelDto>> getStaffProductTravel(@ApiParam(name = "staffNo", value = "员工工号") @RequestParam("staffNo") String staffNo,
                                                                          @ApiParam(name = "pageNo", value = "起始页") @RequestParam("pageNo") Integer pageNo,
                                                                          @ApiParam(name = "pageSize", value = "每页多少条") @RequestParam("pageSize") Integer pageSize) {
        log.info("......StaffController.getStaffProductTravel()开始,请求参数,staffNo={},pageNo={},pageSize={}......", staffNo, pageNo, pageSize);
        if (null == pageNo || null == pageSize) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        if (StringUtils.isBlank(staffNo)) {
            //未传 默认为当前用户 从头消息中获取
            staffNo = QueryIds.userNo.get();
        }
        Page<StaffProdcutTravelDto> page = staffService.getStaffProductTravel(staffNo, pageNo, pageSize);
        return ComResponse.success(page);
    }


    /**
     * 员工画像  获取员工顾客列表
     *
     * @return
     */
    @ApiOperation(value = "员工画像  获取员工顾客列表", httpMethod = "GET")
    @GetMapping("/getCustomerListByStaffNo")
    public ComResponse<Page<CustomerDto>> getCustomerListByStaffNo(@ApiParam(name = "staffNo", value = "员工工号") @RequestParam("staffNo") String staffNo,
                                                                   @ApiParam(name = "pageNo", value = "起始页") @RequestParam("pageNo") Integer pageNo,
                                                                   @ApiParam(name = "pageSize", value = "每页多少条") @RequestParam("pageSize") Integer pageSize) {
        log.info("......StaffController.getCustomerListByStaffNo()开始,请求参数,staffNo={},pageNo={},pageSize={}......", staffNo, pageNo, pageSize);
        if (null == pageNo || null == pageSize) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }

        if (StringUtils.isBlank(staffNo)) {
            //未传 默认为当前用户 从头消息中获取
            staffNo = QueryIds.userNo.get();
        }
        Page<CustomerDto> page = staffService.getCustomerListByStaffNo(staffNo, pageNo, pageSize);
        return ComResponse.success(page);
    }

    /**
     * 员工画像  获取员工旅程
     *
     * @return
     */
    @ApiOperation(value = "员工画像  获取员工旅程", httpMethod = "GET")
    @GetMapping("/getStaffTravleList")
    public ComResponse<List<StaffTrainDto>> getStaffTravleList(@ApiParam(name = "staffNo", value = "员工工号") @RequestParam("staffNo") String staffNo) {
        log.info("......StaffController.getStaffTravleList()开始,请求参数,staffNo={}......", staffNo);
        if (StringUtils.isBlank(staffNo)) {
            //未传 默认为当前用户 从头消息中获取
            staffNo = QueryIds.userNo.get();
        }
        ComResponse<List<StaffTrainDto>> response = ehrStaffClient.getStaffTrain(staffNo);
        return response;
    }


    /**
     * 员工画像  获取员工订单列表
     *
     * @return
     */
    @ApiOperation(value = "员工画像  获取员工订单列表", httpMethod = "POST")
    @PostMapping("/getStaffOrderList")
    public ComResponse<Page<OderListResDTO>> getStaffOrderList(@RequestBody OrderCriteriaDto req) {
        log.info("......StaffController.getStaffOrderList()开始,请求参数,{}......", JsonUtil.toJsonStr(req));
        if (StringUtils.isBlank(req.getStaffNo())) {
            //未传 默认为当前用户 从头消息中获取
            req.setStaffNo(QueryIds.userNo.get());
        }
        ComResponse<Page<OderListResDTO>> response = staffService.getStaffOrderList(req);
        return response;
    }

    /**
     * 员工列表  获取员工所有状态
     *
     * @return
     */
    @ApiOperation(value = "员工列表  获取员工所有状态", httpMethod = "GET")
    @GetMapping("/getAllStuffStatus")
    public ComResponse<List<SysDictDto>> getAllStuffStatus() {
        log.info("......StaffController.getAllStuffStatus()开始,......");
        ComResponse<List<SysDictDto>> response = ehrStaffClient.getAllStuffStatus(EhrParamEnum.EHR_DICT_STAFF_STATUS);
        return response;
    }

    /**
     * 员工画像  员工异动字典
     *
     * @return
     */
    @ApiOperation(value = "员工画像  员工异动字典", httpMethod = "GET")
    @GetMapping("/getAllAbnormalType")
    public ComResponse<List<SysDictDto>> getAllAbnormalType() {
        log.info("......StaffController.getAllAbnormalType()开始,......");
        ComResponse<List<SysDictDto>> response = ehrStaffClient.getAllStuffStatus(EhrParamEnum.EHR_DICT_ABNORMAL_TYPE);
        return response;
    }

    /**
     * 员工列表  根据岗位id获取部门岗位级别列表
     *
     * @return
     */
    @ApiOperation(value = "根据岗位id获取部门岗位级别列表", httpMethod = "GET")
    @GetMapping("/getPostLevelListByDepartId")
    public ComResponse<EhrPostLevelDto> getPostLevelListByPostId(@ApiParam(name = "postId") @RequestParam("postId") Integer postId) {
        log.info("......StaffController.getPostLevelListByPostId()开始,请求参数: postId={}......", postId);
        ComResponse<EhrPostLevelDto> response = ehrStaffClient.getPostLevelListByPostId(postId);
        return response;
    }

    /**
     * 员工列表  根据部门id获取部门岗位列表
     *
     * @return
     */
    @ApiOperation(value = "根据部门id获取部门岗位列表", httpMethod = "GET")
    @GetMapping("/getPostListByDepartId")
    public ComResponse<List<EhrPostDto>> getPostListByDepartId(@ApiParam(name = "departId") @RequestParam("departId") Integer departId) {
        log.info("......StaffController.getPostListByDepartId()开始,......");
        ComResponse<List<EhrPostDto>> response = ehrStaffClient.getPostListByDepartId(departId);
        return response;
    }

    /**
     * 获取 组织架构 部门树形列表
     *
     * @return
     */
    @ApiOperation(value = "获取 组织架构 部门树形列表", httpMethod = "GET")
    @GetMapping("/getDepartTree")
    public ComResponse<EhrDepartDto> getDepartTree() {
        log.info("......StaffController.getDepartTree()开始,......");
        ComResponse<EhrDepartDto> response = ehrStaffClient.getDepartTree();
        return response;
    }

    /**
     * 员工列表  根据条件分页查询员工列表
     *
     * @return
     */
    @ApiOperation(value = "根据条件分页查询员工列表", httpMethod = "POST")
    @PostMapping("/getStaffListByPage")
    public ComResponse<Page<EhrStaff>> getStaffListByPage(@RequestBody StaffQueryDto query) {
        log.info("......StaffController.getStaffListByPage()开始, 请求参数:{}......", JsonUtil.toJsonStr(query));
        query.setStaffNo(QueryIds.userNo.get());
        ComResponse<Page<EhrStaff>> response = ehrStaffClient.getStaffListByPage(query);
        return response;
    }

    @ApiOperation(value = "根据员工工号数组批量查询用户详情", notes = "根据staffno数组批量查询用户详情")
    @RequestMapping(value = "/getDetailsListByNo", method = RequestMethod.POST)
    ComResponse<List<StaffDetail>> getDetailsListByNo(@RequestBody List<String> list) {
        return staffService.getDetailsListByNo(list);
    }


    /**
     * 根据员工id获取当前员工部门以及下属部门
     *
     * @return
     */
    @ApiOperation(value = "根据员工id获取当前员工部门以及下属部门", httpMethod = "GET")
    @GetMapping("/getListByStaffNo")
    public ComResponse<List<EhrDepartDto>> getListByStaffNo(@ApiParam(name = "staffNo")/*@RequestParam(value = "staffNo",required = false)*/ String staffNo) {
        log.info("......StaffController.getListByStaffNo(), 请求参数:{}......", staffNo);
        if (StringUtils.isBlank(staffNo)) {
            //未传 默认为当前用户 从头消息中获取
            staffNo = QueryIds.userNo.get();
        }
        ComResponse<List<EhrDepartDto>> response = ehrStaffClient.getListByStaffNo(staffNo);
        return response;
    }

    @ApiOperation(value = "获取当前登录人的营销目标")
    @GetMapping("/getMarketTarget")
    public ComResponse<MarketTargetDto> getMarketTarget() {
        return staffService.getMarketTarget();
    }


    @ApiOperation(value = "员工变动-查询员工最新变动后状态")
    @GetMapping("/getStaffLastChangeRecord")
    public ComResponse<StaffChangeRecordDto> getStaffLastChangeRecord(@NotBlank @ApiParam(value="员工工号")String staffNo) {
        return ehrStaffClient.getStaffLastChangeRecord(staffNo);
    }

    @ApiOperation(value = "员工变动-根据员工变动编号查询变动状态")
    @GetMapping("/getStaffChangeRecordById")
    public ComResponse<StaffChangeRecordDto> getStaffChangeRecordById(@NotNull @ApiParam(value="员工变动编号")  Integer id) {
        return ehrStaffClient.getStaffChangeRecordById(id);
    }
}
