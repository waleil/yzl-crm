package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.NewCreateOrderSaleVO;
import cn.net.yzl.order.model.vo.order.OrderSale;
import cn.net.yzl.order.model.vo.order.OrderSaleVO;
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
@FeignClient(name = "orderSale",url = "127.0.0.1:4455/orderSale")
public interface OrderSaleClient {
    /**
     * 新建售后订单
     * @return
     */
    @PostMapping(path="v1/saveOrderSale")
    public ComResponse<Boolean> saveOrderSale(@RequestBody @Validated OrderSale orderSalem);

    /**
     * 修改售后订单信息
     * @return
     */
    @PostMapping(path="v1/updateOrderSale")
    public ComResponse<Boolean> updateOrderSale(@RequestBody @Validated OrderSale orderSalem);

    /**
     * 查询售后单详情
     * @return
     */
    @GetMapping(path="v1/selectOrderSaleList")
    public ComResponse<List<OrderSaleVO>> selectOrderSaleList(@RequestParam(required = false) String orderNo,
                                                              @RequestParam(required = false) Integer saleOrderType,
                                                              @RequestParam(required = false) Integer refundType,
                                                              @RequestParam(required = false) String memberName,
                                                              @RequestParam(required = false) String createStartTime,
                                                              @RequestParam(required = false) String createEndTime);

    /**
     * 查询售后单详情
     * @return
     */
    @GetMapping(path="v1/selectOrderSaleInfo")
    public ComResponse<OrderSaleVO> selectOrderSaleInfo(@RequestParam(required = false) String orderNo, @RequestParam String saleOrderNo);

    /**
     * 售后订单审批
     * @param orderSaleNo
     * @param
     * @return
     */
    @GetMapping("v1/updateOrderSaleState")
    public ComResponse<Boolean> updateOrderSaleState(@RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(name="orderSaleNo",value="售后单号",required=true) String orderSaleNo,
                                                     @RequestParam @NotBlank(message = "审批状态不能为空") @ApiParam(name="state",value="审批状态 0:不通过,其他:通过",required=true) Integer checkStatus,
                                                     @RequestParam(required = false) String userNo,
                                                     @RequestParam(required = false) Integer userWorkInfo,
                                                     @RequestParam(required = false) String remark,
                                                     @RequestParam(required = false) String checkType);

    /**
     * 查询售后订单审批列表
     * @param orderNo
     * @param memberName
     * @param createStartTime
     * @param createEndTime
     * @param state
     * @return
     */
    @GetMapping("v1/selectOrderSaleCheckList")
    public ComResponse<List<OrderSaleVO>> selectOrderSaleCheckList(@RequestParam(required = false) String orderNo,  @RequestParam(required = false) String memberName, @RequestParam(required = false) String createStartTime,@RequestParam(required = false) String createEndTime,@RequestParam @NotBlank(message = "售后单状态不能为空") @ApiParam(name="state",value="售后单状态 1:未审核,其他:已审核",required=true) Integer state);


    /**
     * 查询售后订单审核详情
     * @param orderNo
     * @param saleOrderNo
     * @return
     */
    @GetMapping("v1/selectOrderSaleCheckInfo")
    public ComResponse<OrderSaleVO> selectOrderSaleCheckInfo(@RequestParam(required = false) String orderNo,
                                                             @RequestParam @NotBlank(message = "售后单号不能为空") @ApiParam(name="saleOrderNo",value="售后单号",required=true)String saleOrderNo) ;

    /**
     * 新建时查询订单信息
     * @param orderNo
     * @return
     */
    @GetMapping("v1/selectOrderSaleProductInfo")
    public ComResponse<NewCreateOrderSaleVO> selectOrderSaleProductInfo(@RequestParam(required = false) String orderNo);
    }
