package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @return 查询售后订单审批列表
     * @author zhangweiwei
     * @date 2021年1月25日, 下午1:26:48
     */
    @GetMapping("/v1/selectOrderSaleCheckList")
    public ComResponse<Page<OrderSaleCheckListVO>> selectOrderSaleCheckList(
            @RequestParam(required = false) String orderNo, @RequestParam(required = false) String memberName,
            @RequestParam(required = false) String createStartTime,
            @RequestParam(required = false) String createEndTime, @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam @NotBlank(message = "售后单状态不能为空") Integer state);

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
    ComResponse<Boolean> updateExpress(@RequestParam @Valid OrderSaleUpdateExpress express);


    /**
     * 审核售后单
     *
     * @param dto
     * @return
     */
    ComResponse<Boolean> reviewSaleOrder(SaleOrderReviewDTO dto);
}
