package cn.net.yzl.crm.client.workorder;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.db.WorkOrderRuleBean;
import cn.net.yzl.workorder.model.db.WorkOrderRuleConfigBean;
import cn.net.yzl.workorder.model.vo.WorkOrderRuleConfigTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *  工单规则配置相关功能
 */
@Service
@FeignClient(name = "workOrderRuleClient",url = "${api.gateway.url}/workorderServer/rule")
public interface WorkOrderRuleConfigClient {

    /**
     *  新增工单规则配置
     * @param workOrderRuleConfigTO
     * @return
     */
    @PostMapping("/v1/createOrUpdateWorkOrderRuleConfigInfo")
    ComResponse<Void> createOrUpdateWorkOrderRuleConfigInfo(@RequestBody WorkOrderRuleConfigTO workOrderRuleConfigTO);

    /**
     *  根据ruleType查询工单规则配置列表
     * @param ruleType
     * @return
     */
    @GetMapping("/v1/getWorkOrderRuleConfigList")
    ComResponse<List<WorkOrderRuleConfigBean>> list(@RequestParam(name = "ruleType", required = true) Integer ruleType);

}
