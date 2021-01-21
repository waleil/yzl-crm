package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderSale;
import cn.net.yzl.order.model.vo.order.OrderSaleCheckDetailVO;
import cn.net.yzl.order.model.vo.order.OrderSaleCheckListVO;
import cn.net.yzl.order.model.vo.order.OrderSaleDetailVO;
import cn.net.yzl.order.model.vo.order.OrderSaleListVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "orderSale",url = "${api.gateway.url}/orderService/orderSale")
@FeignClient(name = "orderSale",url = "${api.gateway.url}/orderService/orderSale")
public interface OrderSaleClient {
    //新建售后订单
    @PostMapping("v1/saveOrderSale")
    public ComResponse<Boolean> saveOrderSale(@RequestBody @Validated OrderSale orderSalem);

    @PostMapping("v1/updateOrderSale")
    public ComResponse<Boolean> updateOrderSale(@RequestBody @Validated OrderSale orderSalem);

    @ApiOperation(value = "查询售后单列表")
    @GetMapping("v1/selectOrderSaleList")
    public ComResponse<List<OrderSaleListVO>> selectOrderSaleList(@RequestParam(required = false) @ApiParam(value = "订单编号", name = "orderNo") String orderNo,
                                                                  @RequestParam(required = false) @ApiParam(value = "售后类型", name = "saleOrderType") Integer saleOrderType,
                                                                  @RequestParam(required = false) @ApiParam(value = "返货类型", name = "refundType") Integer refundType,
                                                                  @RequestParam(required = false) @ApiParam(value = "顾客名称", name = "memberName") String memberName,
                                                                  @RequestParam(required = false) @ApiParam(value = "开始时间", name = "createStartTime") String createStartTime,
                                                                  @RequestParam(required = false) @ApiParam(value = "结束时间", name = "createEndTime") String createEndTime) ;

    @ApiOperation(value = "查询售后单详情")
    @GetMapping("v1/selectOrderSaleInfo")
    public ComResponse<OrderSaleDetailVO> selectOrderSaleInfo(@RequestParam(required = false) @ApiParam(name = "orderNo", value = "订单编号") String orderNo,
                                                              @RequestParam @NotBlank(message = "售后单号不能为空") @ApiParam(name = "saleOrderNo", value = "售后单号", required = true) String saleOrderNo) ;

        @ApiOperation(value = "查询售后顶单审核详情")
    @GetMapping("v1/selectOrderSaleCheckInfo")
    public ComResponse<OrderSaleCheckDetailVO> selectOrderSaleCheckInfo(@RequestParam @NotBlank(message = "售后订单不能为空") @ApiParam(name = "saleOrderNo", value = "售后单号") String saleOrderNo) ;

    @ApiOperation(value = "根据订单号查询订单信息")
    @GetMapping("v1/selectOrderSaleProductInfoByOrderNo")
    public ComResponse<OrderSaleDetailVO> selectOrderSaleProductInfoByOrderNo(@RequestParam @ApiParam(name = "orderNo",value = "订单编号") String orderNo) ;
    @ApiOperation(value = "查询售后订单审批列表")
    @GetMapping("v1/selectOrderSaleCheckList")
    public ComResponse<List<OrderSaleCheckListVO>> selectOrderSaleCheckList(@RequestParam(required = false) @ApiParam(name = "orderNo", value = "订单编号") String orderNo
            , @RequestParam(required = false) @ApiParam(name = "memberName", value = "顾客姓名") String memberName
            , @RequestParam(required = false) @ApiParam(name = "createStartTime", value = "开始时间") String createStartTime
            , @RequestParam(required = false) @ApiParam(name = "createEndTime", value = "结束时间") String createEndTime
            , @RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(name = "state", value = "售后单状态 1:未审核,其他:已审核", required = true) Integer state) ;



    @ApiOperation(value = "售后订单审批")
    @GetMapping("v1/updateOrderSaleState")
    public ComResponse<Boolean> updateOrderSaleState(@RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(name = "orderSaleNo", value = "售后单号", required = true) String orderSaleNo,
                                                     @RequestParam @NotBlank(message = "审批状态不能为空") @ApiParam(name = "state", value = "审批状态 0:不通过,其他:通过", required = true) Integer checkStatus,
                                                     @RequestParam(required = false) @ApiParam(name = "userNo", value = "用户ID") String userNo,
                                                     @RequestParam(required = false) @ApiParam(name = "userWorkInfo", value = "用户岗位") Integer userWorkInfo,
                                                     @RequestParam(required = false) @ApiParam(name = "remark", value = "备注") String remark,
                                                     @RequestParam(required = false) @ApiParam(name = "checkType", value = "售后类型") String checkType) ;


    }
