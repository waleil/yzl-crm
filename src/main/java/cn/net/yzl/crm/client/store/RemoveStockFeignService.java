package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.*;
import cn.net.yzl.model.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/25 21:37
 */
@FeignClient(name = "removeStockClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface RemoveStockFeignService {

    @ApiOperation(value = "分页查询出库制表单表", notes = "分页查询出库制表单表")
    @PostMapping("removestock/v1/selectRemoveStoreListPage")
    ComResponse<Page<RemoveStockDto>> selectRemoveStoreListPage(@RequestBody RemoveStockRaramsVo removeStockRaramsVo);


    @ApiOperation(value = "生成出库单",notes = "生成出库单")
    @PostMapping("removestock/v1/createOutStoreOrder")
    public ComResponse createOutStoreOrder(@RequestBody List<OutStoreOrderVo> outStoreOrderVoList);

    @ApiOperation(value = "查询出库单列表",notes = "查询出库单列表")
    @PostMapping("removestock/v1/selectOutStoreOrder")
    public ComResponse<Page<RemoveStockManageDto>> selectOutStoreOrder(@RequestBody OutStoreOrderParamVo outStoreOrderParamVo);

    @ApiOperation(value = "查看出库单详情",notes = "查看出库单详情")
    @PostMapping("removestock/v1/selectOutStoreOrderInfo")
    public ComResponse<Page<RemoveStockDto>> selectOutStoreOrderInfo(@RequestBody OutStoreOrderInfoParamVo outStoreOrderInfoParamVo);


//    @ApiOperation(value = "物流-快递运单查询或运单异常登记")
//    @PostMapping("removestock/v1/selectOutStoreToLogistics")
//    public ComResponse<Page<StoreToLogisticsDto>> selectOutStoreToLogistics(@RequestBody StoreToLogisticsParamVo storeToLogisticsParamVo);
//
//
//    @ApiOperation(value = "物流-修改订单状态和收据状态")
//    @PostMapping("removestock/v1/updateOutStoreToLogistics")
//    public ComResponse updateOutStoreToLogistics(@RequestBody LogisticsToStoreUpdateParam logisticsToStoreUpdateParam);
//
//
//    @ApiOperation(value = "物流-登记查询")
//    @GetMapping("removestock/v1/selectBillOrderNo")
//    public ComResponse<StoreToLogisticsDto> selectBillExpressNum(@RequestParam("expressNum") String expressNum);


    @ApiOperation(value = "打印-根据出库单号、制单人、制单时间查询出库单及对应订单",notes = "打印-根据出库单号、制单人、制单时间查询出库单及对应订单")
    @PostMapping("removestock/v1/selectAllOutStore2FY")
    public ComResponse<List<OrderNoPrintStatusDto>> selectAllOutStore2FY(@RequestBody OutStoreOrderParamVo outStoreOrderParamVo);


    @ApiOperation(value = "打印-根据出库单号、订单编号、打印状态查询出库单及对应订单",notes = "打印-根据出库单号、订单编号、打印状态查询出库单及对应订单")
    @PostMapping("removestock/v1/selectOneOutStore2FY")
    public ComResponse<List<OrderNoPrintStatusDto>> selectOneOutStore2FY(@RequestBody OutStoreOrderInfoParamVo outStoreOrderInfoParamVo);


    @ApiOperation(value = "二维码-根据订单号查询发件人和收件人信息",notes = "二维码-根据订单号查询发件人和收件人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单编号",  dataType = "String", paramType = "query"),
    })
    @GetMapping("removestock/v1/selectQRSendConsigneeInfo")
    public ComResponse<QrSendConsigneeDto> selectQRSendConsigneeInfo(@RequestParam("orderNo") String orderNo);

    @ApiOperation(value = "修改出库单和订单-打印状态",notes = "修改出库单和订单-打印状态")
    @PostMapping("removestock/v1/updateOutStoreOrderStatus")
    public ComResponse updateOutStoreOrderPrintStatus(@RequestBody List<OrderNoPrintStatusVo> orderNoPrintStatusVoList);



}
