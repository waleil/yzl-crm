package cn.net.yzl.crm.client.workorder;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.db.WorkOrderRuleBean;
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
public interface WorkOrderRuleClient {

    /**
     *  新增工单规则配置
     * @param workOrderRuleBean
     * @return
     */
    @PostMapping("/v1/add")
    ComResponse<Void> add(@RequestBody  WorkOrderRuleBean workOrderRuleBean);

    /**
     *  根据ruleType查询工单规则配置列表
     * @param ruleType
     * @return
     */
    @GetMapping("/v1/list")
    ComResponse<List<WorkOrderRuleBean>> list(@RequestParam(name = "ruleType",required = true) Integer ruleType);


    /**
     *  根据ID查询工单规则配置详情
     * @param id
     * @return
     */
    @GetMapping("/v1/detail")
    ComResponse<WorkOrderRuleBean> getById(@RequestParam(name = "id",required = true) Integer id);

    /**
     *  修改工单规则配置
     * @param workOrderRuleBean
     * @return
     */
    @PostMapping("/v1/edit")
    ComResponse<Void> update(@RequestBody  WorkOrderRuleBean workOrderRuleBean);


}
