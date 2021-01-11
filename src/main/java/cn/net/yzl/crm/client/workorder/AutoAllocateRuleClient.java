package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingDTO;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingProduct;
import cn.net.yzl.order.model.vo.order.UpdateOrderCheckSettingDTO;
import cn.net.yzl.workorder.model.db.AutoAllocateRuleBean;
import cn.net.yzl.workorder.model.vo.AutoAllocateRuleCriteriaTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Service
@FeignClient(name = "autoAllocateRuleClient",url = "${api.gateway.url}/workorderServer/autoAllocateRule")
public interface AutoAllocateRuleClient {

    /**
     * 功能描述: 智能派单管理规则列表查询
     * @Author: xuwei
     * @Date: 2021/1/9 4:04 下午
     */
    @PostMapping(path="/v1/list")
    ComResponse<List<AutoAllocateRuleBean>> list(AutoAllocateRuleCriteriaTO criteriaTO);

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

}
