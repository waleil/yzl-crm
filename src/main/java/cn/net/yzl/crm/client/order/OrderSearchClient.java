package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

//@FeignClient(name = "orderSerch", url = "${api.gateway.url}/orderService/orderSearch")
@FeignClient(name = "orderSerch", url = "localhost:4455/orderSearch")
public interface OrderSearchClient {

    /**
     * @param dto
     * @return 查询订单列表
     * @date 2021年1月25日, 下午1:28:43
     */
    @RequestMapping(path = "v1/selectOrderList", method = RequestMethod.POST)
    public ComResponse<Page<OderListResDTO>> selectOrderList(@RequestBody OderListReqDTO dto);

    /**
     * @param orderNo
     * @return 查询订单基本信息
     * @date 2021年1月25日, 下午1:28:55
     */
    @GetMapping("v1/selectOrderInfo")
    public ComResponse<List<OrderInfoResDTO>> selectOrderInfo(@RequestParam String orderNo);

    /**
     * @param orderNo
     * @return 查询订单商品列表
     * @date 2021年1月25日, 下午1:29:07
     */
    @GetMapping("v1/selectOrderProductDetail")
    public ComResponse<OrderProductListVo> selectOrderProductDetail(@RequestParam String orderNo);

    /**
     * @param orderNo
     * @return 查询订单操作日志
     * @date 2021年1月25日, 下午1:29:15
     */
    @GetMapping("v1/selectOrderLogList")
    public ComResponse<List<OrderUpdateLogDTO>> selectOrderLogList(
            @RequestParam @NotNull(message = "订单编号不能为空") String orderNo);

    /**
     * @param dto
     * @return 查询订单审核列表
     * @date 2021年1月25日, 下午1:29:29
     */
    @RequestMapping(path = "v1/selectOrderList4Check", method = RequestMethod.POST)
    public ComResponse<Page<OderListResDTO>> selectOrderList4Check(@RequestBody OrderList4CheckReqDTO dto);

    @ApiOperation(value = "查询订单信息服务层,为取消订单售后单提供的接口")
    @PostMapping("v1/selectOrderInfo4Opr")
    public ComResponse<OrderInfoVo> selectOrderInfo4Opr(@RequestParam("orderNo") String orderNo);


    @GetMapping("v1/getSignOrderStatus")
    public ComResponse<CustomerNearSignOrderStatusDTO> getSignOrderStatus(
            @RequestParam("memberCarNo") @NotEmpty(message = "会员编号不能为空") String memberCarNo,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime);

    @ApiOperation(value = "查询订单基本信息仅订单信息")
    @GetMapping("v1/selectOrderInfoOnly")
    public ComResponse<OrderInfoResDTO> selectOrderInfoOnly(@RequestParam("orderNo")
                                                            @NotNull(message = "订单编号不能为空") String orderNo);

    /**
     * 获取顾客画像-订单信息
     *
     * @param workOrderNo
     * @param workBatchNo
     * @return
     */
    @GetMapping("v1/getPortraitOrderDetail")
    ComResponse<List<PortraitOrderDetailDTO>> getPortraitOrderDetail(
            @RequestParam("workOrderNo") @NotEmpty(message = "工单号不可为空") String workOrderNo,
            @RequestParam("workBatchNo") @NotEmpty(message = "工单流水号不可为空") String workBatchNo);

    /**
     * 查询顾客购买过的商品
     *
     * @param memberCarNo
     * @return
     */
    @GetMapping("v1/selectProductsByMemberCard")
    public ComResponse<List<String>> selectProductsByMemberCard(
            @RequestParam("memberCarNo") @NotEmpty(message = "会员编号不能为空") String memberCarNo);

    /**
     * 查询顾客当天是否有退单记录
     *
     * @return
     */
    @GetMapping("v1/selectHasRefund")
    public ComResponse<Boolean> selectHasRefund(
            @RequestParam("memberCarNo") @NotEmpty(message = "会员编号不能为空") String memberCarNo);


    /**
     * 根据员工号顾客卡号查询累计消费金额(元)
     *
     * @return
     */
    @GetMapping("v1/selectSalesQuota")
    public ComResponse<String> selectSalesQuota(
            @RequestParam("memberCarNo") @NotEmpty(message = "会员编号不能为空") String memberCarNo,
            @RequestParam("staffNo") @NotEmpty(message = "员工号不能为空") String staffNo,
            @RequestParam("startTime") @NotEmpty(message = "开始时间不可为空(yyyy-MM-dd HH:mm:ss)") String startTime,
            @RequestParam("endTime") @NotEmpty(message = "结束时间不可为空(yyyy-MM-dd HH:mm:ss)") String endTime);


    @ApiOperation(value = "查询订单销售明细")
    @PostMapping("v1/selectOrderSaleDetail")
    public ComResponse<Page<OrderSellDetailResDTO>> selectOrderSaleDetail(@RequestBody OrderSellDetailReqDTO dto);

    @ApiOperation(value = "查询顾客指定时间范围内客户退单情况")
    @GetMapping("orderSearch/v1/hasRefundByMemberCardNo")
    public ComResponse<Boolean> hasRefundByMemberCardNo(@ApiParam("会员编号") @RequestParam("memberCarNo") String memberCarNo,
                                                        @ApiParam("开始时间") @RequestParam("startTime") String startTime,
                                                        @ApiParam("结束时间") @RequestParam("endTime") String endTime);
    @ApiOperation(value = "根据分仓规则查询仓库")
    @GetMapping("v1/selectSplitStore")
    public ComResponse<String> selectSplitStore(@RequestParam Integer provienceCode);
}