package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "OrderOprClient",url = "${api.gateway.url}/orderService/orderOpr")
public interface OrderOprClient {

    @ApiOperation(value = "取消订单")
    @PostMapping("v1/cancleOrderM")
    public ComResponse<Boolean> cancleOrderM( String orderNo);
}
