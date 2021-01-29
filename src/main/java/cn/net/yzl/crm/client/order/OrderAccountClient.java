package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderAccoundInfoDTO;
import cn.net.yzl.order.model.vo.order.OrderListAccuntDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "orderRefund", url = "${api.gateway.url}/orderService/orderRefund")
//@FeignClient(name = "orderRefund", url = "localhost:4455/orderRefund")
public interface OrderAccountClient {

    //新建退款订单分页查询
    @GetMapping("v1/getOrderAccountList")
    ComResponse<Page<OrderListAccuntDTO>> getOrderAccountList(@RequestParam String orderNo,
                                                              @RequestParam Integer pageNo,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam String nameOrCard,
                                                              @RequestParam String financialOwnerName,
                                                              @RequestParam String startTime,
                                                              @RequestParam String endTime);
    //退款商品信息查询
    @GetMapping("v1/selectAccountOrderInfo")
    ComResponse<OrderAccoundInfoDTO> selectAccountOrderInfo(@RequestParam String saleOrderNo,@RequestParam String orderNo);

    @PostMapping("v1/saveAccountOrderInfo")
    ComResponse<Boolean> saveAccountOrderInfo(@RequestParam String refundPayeeAccount,
                                              @RequestParam String transactionNo,
                                              @RequestParam String orderAccountTime,
                                              @RequestParam String saleOrderNo,
                                              @RequestParam String updateCode,
                                              @RequestParam String updateName,
                                              @RequestParam String isn);
}
