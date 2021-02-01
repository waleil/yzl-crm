package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@FeignClient(name = "orderSale", url = "${api.gateway.url}/orderService/orderSale")
public interface OrderSaleClient {
    /**
     * @param dto
     * @return 新建售后订单
     * @author zhangweiwei
     * @date 2021年1月25日, 下午1:27:55
     */
    @PostMapping("/v1/saveOrderSale")
    public ComResponse<Boolean> saveOrderSale(@RequestBody @Validated OrderSaleAddDTO dto);

    /**
     * @return 查询售后单列表
     * @author zhangweiwei
     * @date 2021年1月25日, 下午1:25:01
     */
    @GetMapping("/v1/selectOrderSaleList")
    public ComResponse<Page<OrderSaleListVO>> selectOrderSaleList(@RequestParam(required = false) String orderNo,
                                                                  @RequestParam(required = false) Integer saleOrderType, @RequestParam(required = false) Integer refundType,
                                                                  @RequestParam(required = false) String memberName, @RequestParam(required = false) String createStartTime,
                                                                  @RequestParam(required = false) String createEndTime, @RequestParam(required = false) Integer pageSize,
                                                                  @RequestParam(required = false) Integer pageNo);

    /**
     * @return 查询售后单详情
     * @author zhangweiwei
     * @date 2021年1月25日, 下午1:27:39
     */
    @GetMapping("/v1/selectOrderSaleInfo")
    public ComResponse<OrderSaleDetailWatchDTO> selectOrderSaleInfo(@RequestParam @NotBlank(message = "售后单号不能为空") String saleOrderNo);


    /**
     * @return 根据订单号查询订单信息
     * @author zhangweiwei
     * @date 2021年1月25日, 下午1:27:07
     */
    @GetMapping("/v1/selectOrderSaleProductInfoByOrderNo")
    public ComResponse<CreateOrderSaleForSearchDTO> selectOrderSaleProductInfoByOrderNo(@RequestParam String orderNo);



    /**
     * 查询待审核售后单列表
     */
    @GetMapping("/v1/getUnReviewSaleOrder")
    public ComResponse<Page<OrderSaleCheckListVO>> getUnReviewSaleOrder(
            @RequestParam(required = false) @ApiParam(name = "orderNo", value = "订单编号") String orderNo,
            @RequestParam(required = false) @ApiParam(name = "memberName", value = "顾客姓名") String memberName,
            @RequestParam @ApiParam(value = "页数", name = "pageSize") Integer pageSize,
            @RequestParam @ApiParam(value = "条数", name = "pageNo") Integer pageNo);
    /**
     * 查询已审核审核售后单列表
     */
    @GetMapping("/v1/getReviewedSaleOrder")
    public ComResponse<Page<OrderSaleCheckListVO>> getReviewedSaleOrder(
            @RequestParam(required = false) @ApiParam(name = "orderNo", value = "订单编号") String orderNo,
            @RequestParam(required = false) @ApiParam(name = "memberName", value = "顾客姓名") String memberName,
            @RequestParam @ApiParam(value = "页数", name = "pageSize") Integer pageSize,
            @RequestParam @ApiParam(value = "条数", name = "pageNo") Integer pageNo);

    /**
     * @return 售后订单审批
     * @author zhangweiwei
     * @date 2021年1月25日, 下午1:26:32
     */
    @GetMapping("/v1/updateOrderSaleState")
    public ComResponse<Boolean> updateOrderSaleState(@RequestParam @NotBlank(message = "售后单状态不能为空") String orderSaleNo,
                                                     @RequestParam @NotBlank(message = "审批状态不能为空") Integer checkStatus,
                                                     @RequestParam(required = false) String userNo, @RequestParam(required = false) Integer userWorkInfo,
                                                     @RequestParam(required = false) String remark, @RequestParam(required = false) String checkType);

    /**
     * 变更售后单物流信息
     *
     * @param express
     * @return
     */
    @PostMapping("/v1/updateExpress")
    ComResponse<Boolean> updateExpress(@RequestBody @Valid OrderSaleUpdateExpress express);


    /**
     * 审核售后单
     *
     * @param dto
     * @return
     */
    @PutMapping("/v1/reviewSaleOrder")
    ComResponse<Boolean> reviewSaleOrder(@RequestBody SaleOrderReviewDTO dto);
}
