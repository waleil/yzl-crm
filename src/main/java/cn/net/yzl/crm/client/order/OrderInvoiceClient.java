package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderInvoiceDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceListDTO;
import cn.net.yzl.order.model.vo.order.OrderInvoiceReqDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@FeignClient(name = "OrderInvoiceClient", url = "${api.gateway.url}/orderService/orderInvoice")
//@FeignClient(name = "OrderInvoiceClient", url = "localhost:4455/orderInvoice")
public interface OrderInvoiceClient {


    @ApiOperation("查询订单发票申请列表")
    @PostMapping("v1/selectInvoiceApplyOrderList")
    public ComResponse<Page<OrderInvoiceListDTO>> selectInvoiceApplyOrderList(@RequestBody OrderInvoiceReqDTO dto);


    @ApiOperation("根据订单号，查询剩余可开发票金额")
    @GetMapping("v1/selectRemainedAmt")
    public ComResponse<String> selectRemainedAmt(@RequestParam("orderNo")
                                                 @NotNull(message = "订单编号不能为空")
                                                 @ApiParam(name = "orderNo", value = "订单编号", required = true) String orderNo);

    @ApiOperation("申请开票")
    @PostMapping("v1/applyInvoice")
    public ComResponse<Boolean> applyInvoice (@RequestBody OrderInvoiceDTO dto);


    @ApiOperation("标记")
    @PostMapping("v1/tagInvoice")
    public ComResponse<Boolean> tagInvoice (@RequestBody OrderInvoiceDTO dto ) ;


    @ApiOperation("查询订单发票列表")
    @PostMapping("v1/selectInvoiceList")
    public ComResponse<Page<OrderInvoiceListDTO>> selectInvoiceList (@RequestBody OrderInvoiceReqDTO dto );
}
