package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.db.AutoAllocateRuleBean;
import cn.net.yzl.workorder.model.vo.AutoAllocateRuleCriteriaTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "autoAllocateRuleClient",url = "${api.gateway.url}/workorderServer/autoAllocateRule")
//@FeignClient(name = "autoAllocateRuleClient",url = "localhost:4602/autoAllocateRule")
public interface AutoAllocateRuleClient {

    /**
     * 功能描述: 智能派单管理规则列表查询
     * @Author: xuwei
     * @Date: 2021/1/9 4:04 下午
     */
    @GetMapping(value = "/v1/list")
    ComResponse<List<AutoAllocateRuleBean>> list(@SpringQueryMap AutoAllocateRuleCriteriaTO criteriaTO);

    /**
     * 功能描述: 根据ID修改规则状态
     * @Author: xuwei
     * @Date: 2021/1/9 8:13 下午
     */
    @GetMapping(path="/v1/changeStatus")
    ComResponse<Void> updateStatusById(@RequestParam(name = "id", required = true) Integer id,
                                              @RequestParam(name = "status", required = true) Integer status);
    /**
     * 功能描述: 根据ID逻辑删除规则
     * @Author: xuwei
     * @Date: 2021/1/9 8:14 下午
     */
    @GetMapping(path="/v1/deleteById")
    ComResponse<Void> deleteById(@RequestParam(name = "id", required = true) Integer id);

    /**
     * 功能描述: 新增派单规则
     * @Param: [id]
     * @Return: cn.net.yzl.common.entity.ComResponse<java.lang.Void>
     * @Author: xuwei
     * @Date: 2021/1/11 10:20 下午
     */
    @PostMapping(path="/v1/add")
    ComResponse<Void> add(AutoAllocateRuleBean allocateRuleBean);

}
