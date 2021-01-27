package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderCheckDetailDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "OrderOprClient",url = "${api.gateway.url}/orderService/orderOpr")
@FeignClient(name = "OrderOprClient",url = "localhost:4455/orderOpr")
public interface OrderOprClient {

    @ApiOperation(value = "取消订单")
    @PostMapping("v1/cancleOrderM")
    public ComResponse<Boolean> cancleOrderM( @RequestParam("orderNo") String orderNo);

    /**
     * 订单审批
     * @param dto
     * @return
     */
    @ApiOperation(value = "正常订单审批")
    @PostMapping("v1/checkOrder")
    public ComResponse checkOrder(@RequestBody OrderCheckDetailDTO dto);

}
