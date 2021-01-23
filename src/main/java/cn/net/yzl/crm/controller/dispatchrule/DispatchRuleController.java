package cn.net.yzl.crm.controller.dispatchrule;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.dispatchrule.DispatchRuleClient;
import cn.net.yzl.workorder.model.db.DispatchRuleDetail;
import cn.net.yzl.workorder.model.db.DispatchRuleSetUp;
import cn.net.yzl.workorder.model.vo.DispatchRuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyadong
 * @date: 2021/1/2210:58
 * <p>
 * 智能派单分配规则 controller
 */
@RestController(value = "crm_dispatch_rule")
@Api(tags = "智能派单分配规则")
public class DispatchRuleController {

    @Autowired
    private DispatchRuleClient dispatchRuleClient;

    @PostMapping("v1/saveDispatch")
    @ApiOperation(value = "编辑智能派单分配规则", notes = "编辑智能派单规则")
    public ComResponse saveDispatch(@RequestBody DispatchRuleDetail detail) {

        ComResponse comResponse = null;
        try {
            comResponse = dispatchRuleClient.saveDispatch(detail);
        } catch (Exception e) {
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(),
                    ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }
        return comResponse;
    }

    @ApiOperation(value = "启用/停用 智能派单分配规则")
    @GetMapping(value = "v1/updateDispatchRule")
    public ComResponse updateDispatchRule(@ApiParam(value = "id", required = true) @RequestParam(value = "id", required = true) String id,
                                          @ApiParam(value = "status", required = true) @RequestParam(value = "status", required = true) Integer status) {
        ComResponse comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.updateDispatchRule(id, status);
        } catch (Exception e) {
            // 远程调用 异常
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }
        return comResponse;
    }

    /**
     * @return 智能派单查询，按照条件查询
     */
    @PostMapping(value = "v1/queryRulesByParams")
    @ApiOperation(value = "智能派单分配规则查询")
    public ComResponse queryDispatchRules(@RequestBody DispatchRuleVO dispatchRule) {

        ComResponse<Page> comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.queryDispatchRules(dispatchRule);
        } catch (Exception e) {
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }
        return comResponse;
    }


    @PostMapping("v1/saveDispatchSetUp")
    @ApiOperation(value = "设置 [进线规则]", notes = "编辑智能派单规则 进线规则设置")
    public ComResponse saveDispatchSetUp(@RequestBody DispatchRuleSetUp dispatchRuleSetUp) {

        ComResponse comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.saveDispatchSetUp(dispatchRuleSetUp);
        } catch (Exception e) {
            return ComResponse.fail(ComResponse.ERROR_STATUS, "保存进线设置异常");
        }
        return comResponse;
    }


    @PostMapping("v1/getDispatchSetUp")
    @ApiOperation(value = "查询 [进线规则]", notes = "编辑智能派单规则 进线规则设置")
    public ComResponse getDispatchSetUp() {

        ComResponse comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.getDispatchSetUpOne();
        } catch (Exception e) {
            return ComResponse.fail(ComResponse.ERROR_STATUS, "查询进线配置异常 ");
        }
        return comResponse;
    }


}
