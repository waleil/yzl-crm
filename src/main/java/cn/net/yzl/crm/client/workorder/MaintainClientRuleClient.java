package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.dto.WorkOrderUsersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 顾客维护总数量规则校验
 */
@FeignClient(name = "workOrder", url = "${api.gateway.url}/workorderServer/maintainClientRule")
//@FeignClient(name = "workOrder", url = "127.0.0.1:4602/maintainClientRule")
public interface MaintainClientRuleClient {

    /**
     * 员工维护的顾客是否超限
     *
     * @param workOrderUsersDTO
     * @return
     */
    @PostMapping(value = "v1/isUsersToplimit")
    ComResponse<Boolean> isUsersToplimit(WorkOrderUsersDTO workOrderUsersDTO);

}
