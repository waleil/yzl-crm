package cn.net.yzl.crm.client.workorder;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.workorder.UpdateRecyclingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *  工单规则配置相关功能
 */
@Service
@FeignClient(name = "WorkOrderHotline",url = "${api.gateway.url}/workorderServer/hotline")
public interface WorkOrderHotlineClient {

    /**
     *  热线工单：回收
     * @param updateRecyclingDTO
     * @return
     */
    @PostMapping("v1/updateRecycling")
    ComResponse<Void> updateRecycling(@RequestBody UpdateRecyclingDTO updateRecyclingDTO);


}
