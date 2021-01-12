package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.workorder.AutoAllocateRuleClient;
import cn.net.yzl.workorder.model.db.AutoAllocateRuleBean;
import cn.net.yzl.workorder.model.vo.AutoAllocateRuleCriteriaTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


/**
 * 功能描述: 智能派单管理相关接口
 *
 * @Author: xuwei
 * @Date: 2021/1/9 4:16 下午
 */
@Api(tags = "智能派单管理")
@Slf4j
@RestController
@RequestMapping("/workorder/autoAllocateRule")
public class AutoAllocateRuleController {

    @Autowired
    private AutoAllocateRuleClient autoAllocateRuleClient;

    @PostMapping("/v1/list")
    @ApiOperation(value = "查询智能派单规则列表")
    @ApiImplicitParam(name = "criteriaTO", value = "查询参数，可以都为空", required = false, dataType = "AutoAllocateRuleCriteriaTO")
    public ComResponse<List<AutoAllocateRuleBean>> list(@RequestBody AutoAllocateRuleCriteriaTO criteriaTO) {
        ComResponse<List<AutoAllocateRuleBean>> result = autoAllocateRuleClient.list(criteriaTO);
        return result;
    }

    @ApiOperation(value = "修改智能派单规则状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status",paramType="query",dataType = "Integer",required = true,value = "是否启用（1：启用，0：禁用）"),
            @ApiImplicitParam(name = "id",paramType="query",dataType = "Integer",required = true,value = "id")
    })
    @RequestMapping(value="/v1/changeStatus",method=RequestMethod.GET)
    public ComResponse<Void> changeStatus(@RequestParam(name = "id", required = true) Integer id,
                                                              @RequestParam(name = "status", required = true) Integer status) {

        if(id == null || id <= 0){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getMessage());
        }
        if (status == 1 || status == 0) {
            return autoAllocateRuleClient.updateStatusById(id,status);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    /**
     * 功能描述: 根据id删除规则（逻辑）
     * @Return: cn.net.yzl.common.entity.ComResponse<java.lang.Void>
     * @Author: xuwei
     * @Date: 2021/1/9 9:35 下午
     */
    @RequestMapping(value="/v1/delete",method=RequestMethod.GET)
    @ApiOperation(value = "删除智能派单规则状态")
    public ComResponse<Void> deleteById(@RequestParam(name = "id", required = true) Integer id){
        if (id > 0) {
            return autoAllocateRuleClient.deleteById(id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

//    public ComResponse<Void> add(@RequestBody Map<String, Object> map){
//        return null;
//    }




}