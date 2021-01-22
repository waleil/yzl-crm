package cn.net.yzl.crm.client.dispatchrule;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.db.DispatchRuleDetail;
import cn.net.yzl.workorder.model.vo.DispatchRuleVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liyadong
 * @date: 2021/1/2211:01
 * <p>
 * 远程调用  规则接口
 */
//@FeignClient(name = "dispatchRuleClient", url = "${api.gateway.url}/dispatch_rule")
@FeignClient(name = "dispatchRuleClient", url = "localhost:4602/dispatch_rule") // 本地测试
@Service
public interface DispatchRuleClient {
    /**
     * 保存修改 智能派单规则分配
     *
     * @param detail
     * @return
     */
    @PostMapping("v1/saveDispatch")
    public ComResponse saveDispatch(@RequestBody DispatchRuleDetail detail);

    /**
     * 通过id 修改 分配规则 状态(是否 启用 )
     *
     * @param
     * @return
     */
    @GetMapping("v1/updateDispatchRule")
    public ComResponse updateDispatchRule(@RequestParam String id, @RequestParam Integer status);


    /**
     * @return 智能派单查询，按照条件查询分页查询
     */
    @PostMapping("v1/queryDispatchRules")
    @ApiOperation("智能派单查询，按照条件查询分页查询")
    public ComResponse queryDispatchRules(@RequestBody DispatchRuleVO dispatchRule);


}
