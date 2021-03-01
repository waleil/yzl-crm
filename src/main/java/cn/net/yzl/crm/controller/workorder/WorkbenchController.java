package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.workorder.WorkbenchClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.workorder.model.vo.SelectWorkOrderByMemberCardVO;
import cn.net.yzl.workorder.model.vo.VisitSeatMonitoringVo;
import cn.net.yzl.workorder.model.vo.VisitSeatsVo;
import cn.net.yzl.workorder.model.vo.WorkbenchHotlineManagerMonitoringVO;
import cn.net.yzl.workorder.model.vo.WorkbenchHotlineManagerVO;
import cn.net.yzl.workorder.model.vo.WorkbenchHotlineVO;
import cn.net.yzl.workorder.model.vo.WorkbenchVisitManagerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作台
 */
@RestController
@RequestMapping("workbench")
@Api(tags = "工作台")
public class WorkbenchController {


    @Autowired
    WorkbenchClient workbenchClient;

    @RequestMapping(value = "workbenchHotline",method = RequestMethod.GET)
    @ApiOperation(value = "工作台-热线", notes = "工作台-热线")
    public ComResponse<WorkbenchHotlineVO> workbenchHotline(){
        String userNo = QueryIds.userNo.get();
        if(StringUtils.isEmpty(userNo)){
            return ComResponse.fail(ComResponse.ERROR_STATUS, ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getMessage());
        }
        ComResponse<WorkbenchHotlineVO> workbenchHotlineVO = workbenchClient.workbenchHotline(userNo);
        return workbenchHotlineVO;
    }

    @ApiOperation(value = "工作台-回访坐席",notes = "工作台-回访坐席接口")
    @GetMapping(value = "v1/visitSeats")
    public ComResponse<VisitSeatsVo> visitSeats(){
        String userNo = QueryIds.userNo.get();
        if(StringUtils.isEmpty(userNo)){
            return ComResponse.fail(ComResponse.ERROR_STATUS, ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getMessage());
        }
       return workbenchClient.visitSeats(userNo);
    }

    @RequestMapping(value = "getWorkbenchHotlineManager",method = RequestMethod.GET)
    @ApiOperation(value = "工作台-热线经理", notes = "工作台-热线经理")
    public ComResponse<WorkbenchHotlineManagerVO> getWorkbenchHotlineManager(){
        String userNo = QueryIds.userNo.get();
        if(StringUtils.isEmpty(userNo)){
            return ComResponse.fail(ComResponse.ERROR_STATUS, ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getMessage());
        }
        ComResponse<WorkbenchHotlineManagerVO> workbenchHotlineManager = workbenchClient.getWorkbenchHotlineManager(userNo);
        return workbenchHotlineManager;
    }

    @ApiOperation(value = "工作台-回访经理",notes = "工作台-回访经理")
    @GetMapping(value = "getWorkbenchVisitManager")
    public ComResponse<WorkbenchVisitManagerVo> getWorkbenchVisitManager(){
        String userNo = QueryIds.userNo.get();
        if(StringUtils.isEmpty(userNo)){
            return ComResponse.fail(ComResponse.ERROR_STATUS, ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getMessage());
        }
        return workbenchClient.getWorkbenchVisitManager(userNo);
    }

    @ApiOperation(value = "工作台-回访经理-坐席监控", notes = "工作台-回访经理-坐席监控")
    @GetMapping(value = "getVisitSeatMonitoring")
    public ComResponse<Page<VisitSeatMonitoringVo>> getVisitSeatMonitoring(@ApiParam(value = "页数",required = true) @RequestParam("pageNo") Integer pageNo,
                                                                           @ApiParam(value = "条数",required = true) @RequestParam("pageSize") Integer pageSize){
        String userNo = QueryIds.userNo.get();
        if(StringUtils.isEmpty(userNo)){
            return ComResponse.fail(ComResponse.ERROR_STATUS, ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getMessage());
        }
        return workbenchClient.getVisitSeatMonitoring(userNo,pageNo,pageSize);
    }

    @RequestMapping(value = "getWorkbenchHotlineManagerMonitoring",method = RequestMethod.GET)
    @ApiOperation(value = "工作台-热线经理-坐席监控", notes = "工作台-热线经理-坐席监控")
    public ComResponse<Page<WorkbenchHotlineManagerMonitoringVO>> getWorkbenchHotlineManagerMonitoring(@ApiParam(value = "页数",required = true) @RequestParam("pageNo") Integer pageNo,
                                                                                                       @ApiParam(value = "条数",required = true) @RequestParam("pageSize") Integer pageSize){
        String userNo = QueryIds.userNo.get();
        if(StringUtils.isEmpty(userNo)){
            return ComResponse.fail(ComResponse.ERROR_STATUS, ResponseCodeEnums.TOKEN_INVALID_ERROR_CODE.getMessage());
        }
        ComResponse<Page<WorkbenchHotlineManagerMonitoringVO>> workbenchHotlineManagerMonitoringVOS = workbenchClient.getWorkbenchHotlineManagerMonitoring(userNo,pageNo,pageSize);
        return workbenchHotlineManagerMonitoringVOS;
    }

    @RequestMapping(value = "insertWorkbenchHotlineAndCallbackCount",method = RequestMethod.GET)
    @ApiOperation(value = "工作台-热线&回访经理-坐席监控-增加拨打次数", notes = "工作台-热线&回访经理-坐席监控-增加拨打次数")
    public ComResponse<Void> insertWorkbenchHotlineAndCallbackCount(@ApiParam(value = "用户编码",required = true) @RequestParam("staffNo") String staffNo,
                                                                      @ApiParam(value = "工单类型(1：热线，2：回访)",required = true) @RequestParam("workOrderType") Integer workOrderType){
        workbenchClient.insertWorkbenchHotlineAndCallbackCount(staffNo,workOrderType);
        return ComResponse.success();
    }

    @RequestMapping(value = "answerWorkbenchHotlineAndCallback",method = RequestMethod.GET)
    @ApiOperation(value = "工作台-热线&&回访经理-接听", notes = "工作台-热线&&回访经理-接听")
    public ComResponse<SelectWorkOrderByMemberCardVO> answerWorkbenchHotlineAndCallback(@ApiParam(value = "用户编码",required = true) @RequestParam("staffNo") String staffNo){
        SelectWorkOrderByMemberCardVO selectWorkOrderByMemberCardVO = workbenchClient.answerWorkbenchHotlineAndCallback(staffNo);
        return ComResponse.success(selectWorkOrderByMemberCardVO);
    }
}
