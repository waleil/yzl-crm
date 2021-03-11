package cn.net.yzl.crm.controller.dispatchrule;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.dispatchrule.DispatchRuleClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.workorder.model.db.DispatchRuleDetail;
import cn.net.yzl.workorder.model.db.DispatchRuleSetUp;
import cn.net.yzl.workorder.model.vo.DispatchRuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyadong
 * @date: 2021/1/2210:58
 * <p>
 * 智能派单分配规则 controller
 */
@Slf4j
@RestController
@RequestMapping(value = "crm_dispatch_rule")
@Api(tags = "智能派单分配规则")
public class DispatchRuleController {

    @Autowired
    private DispatchRuleClient dispatchRuleClient;

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @PostMapping("v1/saveDispatch")
    @ApiOperation(value = "编辑智能派单分配规则", notes = "编辑智能派单规则")
    public ComResponse saveDispatch(@RequestBody DispatchRuleDetail detail) {

        try {
            ComResponse<StaffImageBaseInfoDto> staffResponse = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            if (StringUtils.isEmpty(staffResponse) || StringUtils.isEmpty(staffResponse.getData())) {
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户不存在");
            }
            detail.setCreateNo(QueryIds.userNo.get());
            detail.setUpdateNo(QueryIds.userNo.get());
        } catch (Exception e) {
            log.error("请求[用户接口] 异常 ，异常信息:{}", e.getMessage());
            return ComResponse.fail(ComResponse.ERROR_STATUS, "请求[用户接口异常]");
        }
        ComResponse comResponse = null;
        try {
            comResponse = dispatchRuleClient.saveDispatch(detail);
        } catch (Exception e) {
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(),
                    ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }
        return comResponse;
    }


    @ApiOperation(value = "根据Id查询 智能派单[分配规则]")
    @GetMapping(value = "v1/getDispatchRuleById")
    public ComResponse getDispatchRule(@ApiParam(value = "id", required = true) @RequestParam("id") String id) {
        try {
            ComResponse<StaffImageBaseInfoDto> staffResponse = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            //智能工单--查询自己部门的数据
            if (StringUtils.isEmpty(staffResponse) || StringUtils.isEmpty(staffResponse.getData())) {
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户不存在");
            }
        } catch (Exception e) {
            log.error("请求[用户接口] 异常 ，异常信息:{}", e.getMessage());
            return ComResponse.fail(ComResponse.ERROR_STATUS, "请求[用户接口异常]");
        }
        ComResponse<DispatchRuleDetail> comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.getDispatchRule(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(),
                    "远程调用[智能派单接口异常]: {}",e.getMessage());
        }
        return comResponse;
    }


    @ApiOperation(value = "启用/停用 智能派单分配规则")
    @GetMapping(value = "v1/updateDispatchRule")
    public ComResponse updateDispatchRule(@ApiParam(value = "id", required = true) @RequestParam(value = "id", required = true) String id,
                                          @ApiParam(value = "status", required = true) @RequestParam(value = "status", required = true) Integer status) {

        try {
            ComResponse<StaffImageBaseInfoDto> staffResponse = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            if (StringUtils.isEmpty(staffResponse) || StringUtils.isEmpty(staffResponse.getData())) {
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户不存在");
            }
        } catch (Exception e) {
            log.error("请求[用户接口] 异常 ，异常信息:{}", e.getMessage());
            return ComResponse.fail(ComResponse.ERROR_STATUS, "请求[用户接口异常]");
        }
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

        try {
            ComResponse<StaffImageBaseInfoDto> staffResponse = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            if(null == staffResponse || staffResponse.getCode() != 200){
                return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), "[员工接口]，返回异常");
            }
        } catch (Exception e) {
            log.error("请求[用户接口] 异常 ，异常信息:{}", e.getMessage());
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), "请求[用户接口]异常");
        }
        ComResponse<Page> comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.queryDispatchRulesVO(dispatchRule);
        } catch (Exception e) {
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }
        return comResponse;
    }

    @ApiOperation(value = "查询，通过Id或者Type 查询智能派单[分配规则]")
    @PostMapping(value = "v1/queryDispathRuleDetailsByIdOrType")
    public ComResponse queryDispathRuleDetailsByTypeOrId(@RequestBody DispatchRuleVO dispatchRuleVO) {

        try {
            ComResponse<StaffImageBaseInfoDto> staffResponse = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            if (StringUtils.isEmpty(staffResponse) || StringUtils.isEmpty(staffResponse.getData())) {
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户不存在");
            }
        } catch (Exception e) {
            log.error("请求[用户接口] 异常 ，异常信息:{}", e.getMessage());
            return ComResponse.fail(ComResponse.ERROR_STATUS, "请求[用户接口异常]");
        }

        ComResponse<Page> comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.queryDispathRuleDetails(dispatchRuleVO);
        } catch (Exception e) {
            return ComResponse.fail(ResponseCodeEnums.API_ERROR_CODE.getCode(), ResponseCodeEnums.API_ERROR_CODE.getMessage());
        }
        return comResponse;
    }


    @PostMapping("v1/saveDispatchSetUp")
    @ApiOperation(value = "设置 [进线规则]", notes = "编辑智能派单规则 进线规则设置")
    public ComResponse saveDispatchSetUp(@RequestBody DispatchRuleSetUp dispatchRuleSetUp) {

        try {
            ComResponse<StaffImageBaseInfoDto> staffResponse = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            if (StringUtils.isEmpty(staffResponse) || StringUtils.isEmpty(staffResponse.getData())) {
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户不存在");
            }
//          创建 员工NO
            dispatchRuleSetUp.setCreateNo(QueryIds.userNo.get());
            dispatchRuleSetUp.setUpdateStaffNo(QueryIds.userNo.get());
        } catch (Exception e) {
            log.error("请求[用户接口] 异常 ，异常信息:{}", e.getMessage());
            return ComResponse.fail(ComResponse.ERROR_STATUS, "请求[用户接口异常]");
        }
        ComResponse comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.saveDispatchSetUp(dispatchRuleSetUp);
        } catch (Exception e) {
            return ComResponse.fail(ComResponse.ERROR_STATUS, "保存进线设置异常");
        }
        return comResponse;
    }


    @GetMapping("v1/getDispatchSetUp")
    @ApiOperation(value = "查询 [进线规则]", notes = "编辑智能派单规则 进线规则设置")
    public ComResponse getDispatchSetUp() {

        try {
            ComResponse<StaffImageBaseInfoDto> staffResponse = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            if (StringUtils.isEmpty(staffResponse) || StringUtils.isEmpty(staffResponse.getData())) {
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户不存在");
            }
        } catch (Exception e) {
            log.error("请求[用户接口] 异常 ，异常信息:{}", e.getMessage());
            return ComResponse.fail(ComResponse.ERROR_STATUS, "请求[用户接口异常]");
        }
        ComResponse comResponse = null;
        try {
            comResponse = this.dispatchRuleClient.getDispatchSetUpOne();
        } catch (Exception e) {
            return ComResponse.fail(ComResponse.ERROR_STATUS, "进线配置查询异常 ");
        }
        return comResponse;
    }


}
