package cn.net.yzl.crm.controller.workorder;

import cn.hutool.system.UserInfo;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.workorder.WorkOrderHotlineClient;
import cn.net.yzl.crm.dto.ehr.EhrStaff;
import cn.net.yzl.crm.dto.workorder.GetDistributionStaffDTO;
import cn.net.yzl.crm.service.workorder.WorkOrderHotlineService;
import cn.net.yzl.crm.utils.UserInfoUtil;
import cn.net.yzl.workorder.common.Constant;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.dto.MyWorkOrderHotlineListDTO;
import cn.net.yzl.workorder.model.dto.UpdateAcceptStatusReceiveDTO;
import cn.net.yzl.workorder.model.dto.UpdateDisposeWorkOrderCommit;
import cn.net.yzl.workorder.model.dto.UpdateMoreAdjustDTO;
import cn.net.yzl.workorder.model.dto.UpdateRecyclingDTO;
import cn.net.yzl.workorder.model.dto.UpdateSingleAdjustDTO;
import cn.net.yzl.workorder.model.vo.CallInfoVo;
import cn.net.yzl.workorder.model.vo.FindDWorkOrderHotlineDetailsVO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import cn.net.yzl.workorder.model.vo.MyWorkOrderHotlineListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  工单规则配置相关接口
 */
@RestController
@RequestMapping("/workorder/workOrderHotline")
@Api(tags = "智能工单-热线工单管理相关接口开发")
public class WorkOrderHotlineController {

    @Autowired
    private WorkOrderHotlineClient workOrderHotlineClient;

    @Autowired
    private WorkOrderHotlineService workOrderHotlineService;

    /**work_order_hotline_flow
     * 智能工单：热线工单管理-回收
     * @param updateRecyclingDTO
     * @return
     */
    @ApiOperation(value = "智能工单：热线工单管理-回收",notes = "智能工单：热线工单管理-回收")
    @PostMapping("v1/updateRecycling")
    public ComResponse<Void> updateRecycling(@Validated  @RequestBody UpdateRecyclingDTO updateRecyclingDTO){
        updateRecyclingDTO.setStaffNo(UserInfoUtil.getUserId());
        updateRecyclingDTO.setOperator(UserInfoUtil.getUserName());
        updateRecyclingDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
       return workOrderHotlineClient.updateRecycling(updateRecyclingDTO);
    }

    /**
     * 智能工单：热线工单管理-单数据调整
     * @param
     * @return
     */
    @PostMapping("v1/updateSingleAdjust")
    @ApiOperation(value = "智能工单：热线工单管理-单数据调整", notes = "智能工单：热线工单管理-单数据调整")
    public ComResponse<Void> updateSingleAdjust(@Validated @RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO){
        updateSingleAdjustDTO.setOperator(UserInfoUtil.getUserName());
        updateSingleAdjustDTO.setOperatorCode(UserInfoUtil.getUserId());
        updateSingleAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateSingleAdjustDTO.setAcceptStatus(1);//人工触发 改为已接受
        return workOrderHotlineClient.updateSingleAdjust(updateSingleAdjustDTO);
    }

    /**
     * 智能工单：热线工单管理-多数据调整
     * @param
     * @return
     */
    @PostMapping("v1/updateMoreAdjust")
    @ApiOperation(value = "智能工单：热线工单管理-多数据调整", notes = "智能工单：热线工单管理-多数据调整")
    public ComResponse<Void> updateMoreAdjust(@Validated @RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO){
        updateMoreAdjustDTO.setAcceptStatus(1);//人工触发 改为已接受
        updateMoreAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateMoreAdjustDTO.setOperator(UserInfoUtil.getUserName());
        updateMoreAdjustDTO.setOperatorCode(UserInfoUtil.getUserId());
        return workOrderHotlineClient.updateMoreAdjust(updateMoreAdjustDTO);
    }

    /**
     * 智能工单：热线工单管理-列表
     * @param
     * @return
     */
    @PostMapping("v1/findWorkOrderHotlinePageList")
    @ApiOperation(value = "智能工单：热线工单管理-列表", notes = "智能工单：热线工单管理-列表")
    public ComResponse<Page<FindWorkOrderHotlinePageListVO>> findWorkOrderHotlinePageList(@RequestBody FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO){
        return workOrderHotlineClient.findWorkOrderHotlinePageList(findWorkOrderHotlinePageListDTO);
    }

    /**
     * 智能工单：我的热线工单-列表
     * @param
     * @return
     */
    @PostMapping("v1/findMyWorkOrderHotlinePageList")
    @ApiOperation(value = "智能工单：我的热线工单-列表", notes = "智能工单：我的热线工单-列表")
    public ComResponse<Page<MyWorkOrderHotlineListVO>> findMyWorkOrderHotlinePageList(@RequestBody MyWorkOrderHotlineListDTO myWorkOrderHotlineListDTO){
        String userId = UserInfoUtil.getUserId();
        if(StringUtils.isEmpty(userId)){
            ComResponse.fail(ComResponse.ERROR_STATUS,"用户校验失败");
        }
        myWorkOrderHotlineListDTO.setStaffNo(userId);
        return workOrderHotlineClient.findMyWorkOrderHotlinePageList(myWorkOrderHotlineListDTO);
    }

    /**
     * 智能工单：热线工单管理-可分配员工
     * @param
     * @return
     */
    @PostMapping("v1/getDistributionStaff")
    @ApiOperation(value = "智能工单：热线工单管理-可分配员工", notes = "智能工单：热线工单管理-可分配员工")
    public ComResponse<Page<EhrStaff>> getDistributionStaff(@RequestBody GetDistributionStaffDTO getDistributionStaffDTO){
        String userId = UserInfoUtil.getUserId();
        if(StringUtils.isEmpty(userId)){
            ComResponse.fail(ComResponse.ERROR_STATUS,"用户校验失败");
        }
        getDistributionStaffDTO.setStaffNo(userId);
        return workOrderHotlineService.getDistributionStaff(getDistributionStaffDTO);
    }

    /**
     * 智能工单：我的热线工单-接收
     * @param
     * @return
     */
    @PostMapping("v1/updateAcceptStatusReceive")
    @ApiOperation(value = "智能工单：我的热线工单-接收", notes = "智能工单：我的热线工单-接收")
    public ComResponse<Void> updateAcceptStatusReceive(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO){
        updateAcceptStatusReceiveDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateAcceptStatusReceiveDTO.setOperator(UserInfoUtil.getUserName());
        updateAcceptStatusReceiveDTO.setOperatorCode(UserInfoUtil.getUserId());
        return workOrderHotlineClient.updateAcceptStatusReceive(updateAcceptStatusReceiveDTO);
    }

    /**
     * 智能工单：我的热线工单-处理工单详情
     * @param
     * @return
     */
    @PostMapping("v1/findDWorkOrderHotlineDetails")
    @ApiOperation(value = "我的热线工单-处理工单详情", notes = "我的热线工单-处理工单详情")
    public ComResponse<FindDWorkOrderHotlineDetailsVO> findDWorkOrderHotlineDetails(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO){
        return workOrderHotlineClient.findDWorkOrderHotlineDetails(updateAcceptStatusReceiveDTO);
    }

    /**
     * 智能工单：我的热线工单-处理工单提交
     * @return
     */
    @PostMapping("v1/updateDisposeWorkOrderCommit")
    @ApiOperation(value = "智能工单：我的热线工单-处理工单提交", notes = "智能工单：我的热线工单-处理工单提交")
    public ComResponse<Void> updateDisposeWorkOrderCommit(@Validated @RequestBody UpdateDisposeWorkOrderCommit updateDisposeWorkOrderCommit){
        return workOrderHotlineClient.updateDisposeWorkOrderCommit(updateDisposeWorkOrderCommit);
    }
}
