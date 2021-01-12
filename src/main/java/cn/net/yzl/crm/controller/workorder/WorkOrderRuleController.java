package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.workorder.WorkOrderRuleClient;
import cn.net.yzl.workorder.model.db.WorkOrderRuleBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  工单规则配置相关接口
 */
@RestController
@RequestMapping("/workorder/workOrderRule")
@Api(tags = "工单规则配置相关接口开发")
public class WorkOrderRuleController {
    @Autowired
    private WorkOrderRuleClient ruleClient;

    /**
     *  添加工单规则配置
     * @param workOrderRuleBean
     * @return
     */
    @PostMapping("/v1/add")
    @ApiOperation(value = "添加工单规则配置")
    public ComResponse<Void> add(@RequestBody WorkOrderRuleBean workOrderRuleBean){
       return ruleClient.add(workOrderRuleBean);
    }

    /**
     *  根据ruleType查询工单规则配置列表
     * @param ruleType
     * @return
     */
    @GetMapping("/v1/list")
    @ApiOperation(value = "根据ruleType查询工单规则配置列表")
    public ComResponse<List<WorkOrderRuleBean>> list(@RequestParam(name = "ruleType",required = true) Integer ruleType){
        if (ruleType ==null ){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getMessage());
        } if (ruleType <= 0){//ruleType<=0
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        } else {
            ComResponse<List<WorkOrderRuleBean>> result= ruleClient.list(ruleType);
            return result;
        }

    }

    /**
     *  根据ID查询工单规则配置详情
     * @param id
     * @return
     */
    @GetMapping("/v1/detail")
    @ApiOperation(value = "根据id查询工单规则配置")
    @ApiImplicitParam(name = "id", value = "查询参数", required = true, dataType = "Integer")
    public ComResponse<WorkOrderRuleBean> getById(@RequestParam(name = "id",required = true) Integer id){
        ComResponse<WorkOrderRuleBean> result = ruleClient.getById(id);
        return result;
    }

    /**
     *  修改工单规则配置
     * @param workOrderRuleBean
     * @return
     */
    @PostMapping("/v1/edit")
    @ApiOperation(value = "修改工单规则配置")
    public ComResponse<Void> update(@RequestBody  WorkOrderRuleBean workOrderRuleBean){
        ComResponse<Void> result = ruleClient.update(workOrderRuleBean);
        return result;
    }
}
