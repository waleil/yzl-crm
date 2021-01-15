package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.workorder.WorkOrderHotlineClient;
import cn.net.yzl.crm.dto.workorder.UpdateMoreAdjustDTO;
import cn.net.yzl.crm.dto.workorder.UpdateRecyclingDTO;
import cn.net.yzl.crm.dto.workorder.UpdateSingleAdjustDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**work_order_hotline_flow
     *  热线工单：回收
     * @param updateRecyclingDTO
     * @return
     */
    @ApiOperation(value = "热线工单：回收")
    @PostMapping("v1/updateRecycling")
    public ComResponse<Void> updateRecycling(@Validated  @RequestBody UpdateRecyclingDTO updateRecyclingDTO){
       return workOrderHotlineClient.updateRecycling(updateRecyclingDTO);
    }

    /**
     * 热线工单：单数据调整
     * @param
     * @return
     */
    @PostMapping("v1/updateSingleAdjust")
    @ApiOperation(value = "热线工单：单数据调整", notes = "热线工单：单数据调整")
    public ComResponse<Void> updateSingleAdjust(@Validated @RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO){
        return workOrderHotlineClient.updateSingleAdjust(updateSingleAdjustDTO);
    }

    /**
     * 热线工单：多数据调整
     * @param
     * @return
     */
    @PostMapping("v1/updateMoreAdjust")
    @ApiOperation(value = "热线工单：多数据调整", notes = "热线工单：多数据调整")
    public ComResponse<Void> updateMoreAdjust(@Validated @RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO){
        return workOrderHotlineClient.updateMoreAdjust(updateMoreAdjustDTO);
    }

}
