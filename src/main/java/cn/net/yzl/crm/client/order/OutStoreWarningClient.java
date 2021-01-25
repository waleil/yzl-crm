package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OutStoreWarningDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhouchangsong
 */
@FeignClient(name = "outStoreWarningClient", url = "${api.gateway.url}/orderService/outStoreWarning")
public interface OutStoreWarningClient {

    @GetMapping("v1/getOutStoreWarningDetail")
    @ApiOperation(value = "查询出库预警订单信息")
    ComResponse<OutStoreWarningDTO> getOutStoreWarningDetail();
}
