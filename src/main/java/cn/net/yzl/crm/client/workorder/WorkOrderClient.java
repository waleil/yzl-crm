package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 智能工单
 */
@FeignClient(name = "workOrder",url = "${api.gateway.url}/workorderServer/workOrder")
//@FeignClient(name = "workOrder",url = "127.0.0.1:4602/workorderServer/workOrder")
public interface WorkOrderClient {

    @PostMapping(value = "v1/isListPage")
    ComResponse<WorkOrderClient> isListPage(IsListPageDTO isListPageDTO);
}
