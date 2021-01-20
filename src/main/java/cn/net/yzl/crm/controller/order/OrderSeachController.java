package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.model.Advisor;
import cn.net.yzl.crm.model.Media;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.service.order.IOrderSearchService;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
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
    public ComResponse<OrderInfoVO> selectOrderInfo(@RequestParam("orderNo")
                                                        @NotNull(message = "订单编号不能为空")
                                                        @ApiParam(name="orderNo",value="免审规则类型",required=true)String orderNo) {

        return  orderSearchService.selectOrderInfo(orderNo);
    }


    @ApiOperation(value = "查询订单商品列表")
    @GetMapping("v1/selectOrderProductDetail")
    public  ComResponse<List<OrderProductDTO>> selectOrderProductDetail(@RequestParam("orderNo")
                                                                        @NotNull(message = "订单编号不能为空")
                                                                        @ApiParam(name="orderNo",value="订单编号",required=true)String orderNo) {

        return  orderSearchClient.selectOrderProductDetail(orderNo);
    }

    @ApiOperation(value = "查询订单操作日志")
    @GetMapping("v1/selectOrderLogList")
    public  ComResponse<List<OrderUpdateLogDTO>> selectOrderLogList(@RequestParam("orderNo")
                                                                    @NotNull(message = "订单编号不能为空")
                                                                    @ApiParam(name="orderNo",value="订单编号",required=true)String orderNo) {

        return  orderSearchClient.selectOrderLogList(orderNo);
    }
    @ApiOperation(value = "查询订单审核列表")
    @PostMapping("v1/selectOrderList4Check")
    public  ComResponse<Page<OderListResDTO>> selectOrderList4Check(@RequestBody OrderList4CheckReqDTO dto) {

        return  orderSearchClient.selectOrderList4Check(dto);
    }
}
