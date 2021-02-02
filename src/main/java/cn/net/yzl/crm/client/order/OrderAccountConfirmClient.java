package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderAccountConfirmVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

//@FeignClient(name = "OrderAccountConfirmClient", url = "${api.gateway.url}/orderService/orderAccountConfirm")
@FeignClient(name = "OrderAccountConfirmClient", url = "localhost:4455/orderAccountConfirm")
public interface OrderAccountConfirmClient {

    @ApiOperation(value = "根据订单号查询收款确认单")
    @GetMapping("v1/selectByOrderNo")
    public ComResponse<OrderAccountConfirmVO> selectByOrderNo(@RequestParam("orderNo")
                                                              @NotNull(message = "订单编号不能为空")
                                                              @ApiParam(name = "orderNo", value = "订单编号", required = true) String orderNo);
    @ApiOperation(value = "保存收款确认单")
    @PostMapping("v1/saveOrderAccountConfirm")
    public ComResponse<Boolean> saveOrderAccountConfirm(@RequestBody @Valid OrderAccountConfirmVO vo);

}
