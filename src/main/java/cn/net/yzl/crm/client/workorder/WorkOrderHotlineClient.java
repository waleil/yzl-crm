package cn.net.yzl.crm.client.workorder;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.workorder.UpdateMoreAdjustDTO;
import cn.net.yzl.crm.dto.workorder.UpdateRecyclingDTO;
import cn.net.yzl.crm.dto.workorder.UpdateSingleAdjustDTO;
import java.util.List;
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


    /**
     * 热线工单：单数据调整
     * @param updateSingleAdjustDTO
     * @return
     */
    @PostMapping("v1/updateSingleAdjust")
    ComResponse<Void> updateSingleAdjust(@RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO);

    /**
     * 热线工单：多数据调整
     * @param updateMoreAdjustDTO
     * @return
     */
    @PostMapping("v1/updateMoreAdjust")
    ComResponse<Void> updateMoreAdjust(UpdateMoreAdjustDTO updateMoreAdjustDTO);
}
