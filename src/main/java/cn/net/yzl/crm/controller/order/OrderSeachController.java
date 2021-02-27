package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.model.order.OrderLogistcInfo;
import cn.net.yzl.crm.service.order.IOrderSearchService;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("orderSearch")
@Api(tags = "订单管理")
public class OrderSeachController {

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Autowired
    private IOrderSearchService orderSearchService;

    @ApiOperation(value = "查询订单列表")
    @PostMapping("v1/selectOrderList")
    public ComResponse<Page<OderListResDTO>> selectOrderList(@RequestBody OderListReqDTO dto) {
        ComResponse<Page<OderListResDTO>> list = orderSearchClient.selectOrderList(dto);
        return list;
    }


    @ApiOperation(value = "查询订单基本信息")
    @GetMapping("v1/selectOrderInfo")
    public ComResponse<OrderInfoVO> selectOrderInfo(@RequestParam
                                                        @NotNull(message = "订单编号不能为空")
                                                        @ApiParam(value="免审规则类型",required=true)String orderNo) {

        return  orderSearchService.selectOrderInfo(orderNo);
    }


    @ApiOperation(value = "查询订单商品列表")
    @GetMapping("v1/selectOrderProductDetail")
    public  ComResponse<OrderProductListVo> selectOrderProductDetail(@RequestParam
                                                                        @NotNull(message = "订单编号不能为空")
                                                                        @ApiParam(value="订单编号",required=true)String orderNo) {

        return  orderSearchClient.selectOrderProductDetail(orderNo);
    }

    @ApiOperation(value = "查询订单操作日志")
    @GetMapping("v1/selectOrderLogList")
    public  ComResponse<List<OrderUpdateLogDTO>> selectOrderLogList(@RequestParam
                                                                    @NotNull(message = "订单编号不能为空")
                                                                    @ApiParam(value="订单编号",required=true)String orderNo) {

        return  orderSearchClient.selectOrderLogList(orderNo);
    }
    @ApiOperation(value = "查询订单审核列表")
    @PostMapping("v1/selectOrderList4Check")
    public  ComResponse<Page<OderListResDTO>> selectOrderList4Check(@RequestBody OrderList4CheckReqDTO dto) {

        return  orderSearchClient.selectOrderList4Check(dto);
    }

    @ApiOperation(value = "查询物流信息")
    @GetMapping("v1/selectLogisticInfo")
    public  ComResponse<OrderLogistcInfo> selectLogisticInfo(@RequestParam("orderNo") @NotEmpty(message = "订单号不能为空") String orderNo,
                                                             @RequestParam(name = "companyCode" ,required = false)  String companyCode,
                                                             @RequestParam(name = "expressNo",required = false) String expressNo) {

        return  orderSearchService.selectLogisticInfo(orderNo,companyCode,expressNo);
    }

    @ApiOperation(value = "查询订单销售明细")
    @PostMapping("v1/selectOrderSaleDetail")
    public ComResponse<Page<OrderSellDetailResDTO>> selectOrderSaleDetail(@RequestBody OrderSellDetailReqDTO dto) {

        return orderSearchClient.selectOrderSaleDetail(dto);
    }
}
