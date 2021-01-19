package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "orderSerch",url = "${api.gateway.url}/orderService/orderSearch")
public interface OrderSearchClient {


    @ApiOperation(value = "查询订单列表")
    @RequestMapping(path="v1/selectOrderList",method = RequestMethod.POST)
    public ComResponse<Page<OderListResDTO>> selectOrderList(@RequestBody OderListReqDTO dto);

    @ApiOperation(value = "查询订单基本信息")
    @GetMapping("v1/selectOrderInfo")
    public ComResponse<OrderInfoResDTO> selectOrderInfo(@RequestParam("orderNo")String orderNo);

    @ApiOperation(value = "查询订单商品列表")
    @GetMapping("v1/selectOrderProductDetail")
    public  ComResponse<List<OrderProductDTO>> selectOrderProductDetail(@RequestParam("orderNo")String orderNo);

    @ApiOperation(value = "查询订单操作日志")
    @GetMapping("v1/selectOrderLogList")
    public  ComResponse<List<OrderUpdateLogDTO>> selectOrderLogList(@RequestParam("orderNo")
                                                                    @NotNull(message = "订单编号不能为空")
                                                                    @ApiParam(name="orderNo",value="订单编号",required=true)String orderNo);

    @ApiOperation(value = "查询订单审核列表")
    @RequestMapping(path="v1/selectOrderList4Check",method = RequestMethod.POST)
    public  ComResponse<Page<OderListResDTO>> selectOrderList4Check(@RequestBody OrderList4CheckReqDTO dto);

}
