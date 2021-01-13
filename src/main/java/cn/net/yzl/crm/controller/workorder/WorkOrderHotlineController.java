package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.workorder.WorkOrderHotlineClient;
import cn.net.yzl.crm.client.workorder.WorkOrderRuleClient;
import cn.net.yzl.crm.dto.workorder.UpdateRecyclingDTO;
import cn.net.yzl.workorder.model.db.WorkOrderHotlineBean;
import cn.net.yzl.workorder.model.db.WorkOrderRuleBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

    /**work_order_hotline_flow
     *  热线工单：回收
     * @param updateRecyclingDTO
     * @return
     */
    @ApiOperation(value = "热线工单：回收")
    @PostMapping("v1/updateRecycling")
    public ComResponse<Void> updateRecycling(@RequestBody UpdateRecyclingDTO updateRecyclingDTO){
        if(StringUtils.isEmpty(updateRecyclingDTO.getCode())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"工单标识校验错误");
        }
        if(StringUtils.isEmpty(updateRecyclingDTO.getStaffNo())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"员工编号校验错误");
        }
        if(StringUtils.isEmpty(updateRecyclingDTO.getOperator())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"操作人校验错误");
        }
       return workOrderHotlineClient.updateRecycling(updateRecyclingDTO);
    }

}
