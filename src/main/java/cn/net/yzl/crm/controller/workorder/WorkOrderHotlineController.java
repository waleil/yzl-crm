package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.workorder.WorkOrderHotlineClient;
import cn.net.yzl.crm.dto.ehr.EhrStaff;
import cn.net.yzl.crm.dto.workorder.GetDistributionStaffDTO;
import cn.net.yzl.crm.service.workorder.WorkOrderHotlineService;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.dto.MyWorkOrderHotlineListDTO;
import cn.net.yzl.workorder.model.dto.UpdateMoreAdjustDTO;
import cn.net.yzl.workorder.model.dto.UpdateRecyclingDTO;
import cn.net.yzl.workorder.model.dto.UpdateSingleAdjustDTO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import cn.net.yzl.workorder.model.vo.MyWorkOrderHotlineListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "热线工单相关接口开发")
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
        return workOrderHotlineClient.findMyWorkOrderHotlinePageList(myWorkOrderHotlineListDTO);
    }

    /**
     * 智能工单：热线工单管理-可分配员工
     * @param
     * @return
     */
    @PostMapping("v1/getDistributionStaff")
    @ApiOperation(value = "智能工单：热线工单管理-可分配员工", notes = "智能工单：热线工单管理-可分配员工")
    public ComResponse<Page<EhrStaff>> getDistributionStaff(@Validated @RequestBody GetDistributionStaffDTO getDistributionStaffDTO){
        return workOrderHotlineService.getDistributionStaff(getDistributionStaffDTO);
    }
}
