package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.workorder.WorkOrderRuleConfigClient;
import cn.net.yzl.crm.dto.ehr.BusinessPostDto;
import cn.net.yzl.crm.dto.ehr.PostDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.workorder.model.db.WorkOrderRuleConfigBean;
import cn.net.yzl.workorder.model.enums.DeptTypeEnums;
import cn.net.yzl.workorder.model.vo.WorkOrderRuleConfigTO;
import io.swagger.annotations.Api;
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
    private WorkOrderRuleConfigClient workOrderRuleConfigClient;

    @Autowired
    private EhrStaffClient ehrStaffClient;

    /**
     * 根据业务属性获取岗位列表
     */
    @ApiOperation(value="根据业务属性获取岗位列表",httpMethod = "GET")
    @GetMapping(value = "/getPostByBussinessAttrCode")
    ComResponse<List<PostDto>> getPostByBussinessAttrCode(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode){
        if(bussinessAtrrCode == DeptTypeEnums.HOTLINE_CENTER.getCode()){
            bussinessAtrrCode=41;//热线工单 42
        } else {
            bussinessAtrrCode=42;//回访工单 43
        }
        return ehrStaffClient.getPostByBussinessAttrCode(bussinessAtrrCode);
    }

    /**
     * 根据业务属性获取岗位列表
     */
    @ApiOperation(value="根据业务属性和岗位id获取岗位级别列表",httpMethod = "GET")
    @GetMapping(value = "/getBusiPostListByAttr")
    ComResponse<List<BusinessPostDto>> getBusiPostListByAttr(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode, @RequestParam("postId") Integer postId){
        if (bussinessAtrrCode!=null && postId!=null){
            if(bussinessAtrrCode == DeptTypeEnums.HOTLINE_CENTER.getCode()){
                bussinessAtrrCode=41;//热线工单 42
            } else {
                bussinessAtrrCode=42;//回访工单 43
            }
            ComResponse<List<BusinessPostDto>> posts = ehrStaffClient.getBusiPostListByAttr(bussinessAtrrCode, postId);
            return posts;
        }else{
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "业务属性编号热线，回访或者岗位编号");
        }
    }

    /**
     * 功能描述: 添加或修改工单规则配置
     *
     * @Param: [workOrderRuleConfigTO]
     * @Return: cn.net.yzl.common.entity.ComResponse<java.lang.Void>
     * @Author: xuwei
     * @Date: 2021/1/15 1:29 下午
     */
    @PostMapping("/v1/addOrUpdate")
    @ApiOperation(value = "添加或修改工单规则配置")
    public ComResponse<Void> addOrUpdate(@RequestBody WorkOrderRuleConfigTO workOrderRuleConfigTO){
        return workOrderRuleConfigClient.createOrUpdateWorkOrderRuleConfigInfo(workOrderRuleConfigTO);
    }

    /**
     *  根据ruleType查询工单规则配置列表
     * @param ruleType
     * @return
     */
    @GetMapping("/v1/list")
    @ApiOperation(value = "根据ruleType查询工单规则配置列表")
    public ComResponse<List<WorkOrderRuleConfigBean>> list(@RequestParam(name = "ruleType",required = true) Integer ruleType){
        if (ruleType ==null ){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getMessage());
        } if (ruleType <= 0){//ruleType<=0
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        } else {
            ComResponse<List<WorkOrderRuleConfigBean>> result= workOrderRuleConfigClient.list(ruleType);
            return result;
        }

    }

}
