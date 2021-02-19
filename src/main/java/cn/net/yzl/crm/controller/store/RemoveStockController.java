package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.RemoveStockFeignService;
import cn.net.yzl.model.dto.*;
import cn.net.yzl.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/25 21:36
 */
@RestController
@Api(value = "仓储中心心心心心-出库制单", tags = {"仓储中心心心心心-出库制单"})
@RequestMapping("removestock")
public class RemoveStockController {


    @Autowired
    private RemoveStockFeignService removeStockFeignService;

    @ApiOperation(value = "分页查询出库制表单表", notes = "分页查询出库制表单表")
    @PostMapping("v1/selectRemoveStoreListPage")
    public ComResponse<Page<RemoveStockDto>> selectRemoveStoreListPage(@RequestBody RemoveStockRaramsVo removeStockRaramsVo){
        return removeStockFeignService.selectRemoveStoreListPage(removeStockRaramsVo);
    }


    @ApiOperation(value = "生成出库单",notes = "生成出库单")
    @PostMapping("v1/createOutStoreOrder")
    public ComResponse createOutStoreOrder(@RequestBody List<OutStoreOrderVo> outStoreOrderVoList, HttpServletRequest request){
        String userNo = request.getHeader("userNo");
        for (OutStoreOrderVo outStoreOrderVo : outStoreOrderVoList) {
            outStoreOrderVo.setUserNo(userNo);
        }

        return removeStockFeignService.createOutStoreOrder(outStoreOrderVoList);
    }

    @ApiOperation(value = "查询出库单列表",notes = "查询出库单列表")
    @PostMapping("v1/selectOutStoreOrder")
    public ComResponse<Page<RemoveStockManageDto>> selectOutStoreOrder(@RequestBody OutStoreOrderParamVo outStoreOrderParamVo){
        return removeStockFeignService.selectOutStoreOrder(outStoreOrderParamVo);
    }

    @ApiOperation(value = "查看出库单详情",notes = "查看出库单详情")
    @PostMapping("v1/selectOutStoreOrderInfo")
    public ComResponse<Page<RemoveStockDto>> selectOutStoreOrderInfo(@RequestBody OutStoreOrderInfoParamVo outStoreOrderInfoParamVo){
        return removeStockFeignService.selectOutStoreOrderInfo(outStoreOrderInfoParamVo);
    }

//    @ApiOperation(value = "物流-快递运单查询或运单异常登记")
//    @PostMapping("v1/selectOutStoreToLogistics")
//    public ComResponse<Page<StoreToLogisticsDto>> selectOutStoreToLogistics(@RequestBody StoreToLogisticsParamVo storeToLogisticsParamVo){
//        return removeStockFeignService.selectOutStoreToLogistics(storeToLogisticsParamVo);
//    }
//
//
//    @ApiOperation(value = "物流-修改订单状态和收据状态")
//    @PostMapping("v1/updateOutStoreToLogistics")
//    public ComResponse updateOutStoreToLogistics(@RequestBody LogisticsToStoreUpdateParam logisticsToStoreUpdateParam){
//        return removeStockFeignService.updateOutStoreToLogistics(logisticsToStoreUpdateParam);
//    }
//
//
//    @ApiOperation(value = "物流-登记查询")
//    @GetMapping("v1/selectBillExpressNum")
//    public ComResponse<StoreToLogisticsDto> selectBillExpressNum(@RequestParam("expressNum") String expressNum){
//        return removeStockFeignService.selectBillExpressNum(expressNum);
//    }



    @ApiOperation(value = "打印-根据出库单号、制单人、制单时间查询出库单及对应订单",notes = "打印-根据出库单号、制单人、制单时间查询出库单及对应订单")
    @PostMapping("v1/selectAllOutStore2FY")
    public ComResponse<List<OrderNoPrintStatusDto>> selectAllOutStore2FY(@RequestBody OutStoreOrderParamVo outStoreOrderParamVo){
        return removeStockFeignService.selectAllOutStore2FY(outStoreOrderParamVo);
    }


    @ApiOperation(value = "打印-根据出库单号、订单编号、打印状态查询出库单及对应订单",notes = "打印-根据出库单号、订单编号、打印状态查询出库单及对应订单")
    @PostMapping("v1/selectOneOutStore2FY")
    public ComResponse<List<OrderNoPrintStatusDto>> selectOneOutStore2FY(@RequestBody OutStoreOrderInfoParamVo outStoreOrderInfoParamVo){
        return removeStockFeignService.selectOneOutStore2FY(outStoreOrderInfoParamVo);
    }


    @ApiOperation(value = "二维码-根据订单号查询发件人和收件人信息",notes = "二维码-根据订单号查询发件人和收件人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单编号",  dataType = "String", paramType = "query"),
    })
    @GetMapping("v1/selectQRSendConsigneeInfo")
    public ComResponse<QrSendConsigneeDto> selectQRSendConsigneeInfo(@RequestParam("orderNo") String orderNo){

        return removeStockFeignService.selectQRSendConsigneeInfo(orderNo);
    }

    @ApiOperation(value = "修改出库单和订单-打印状态",notes = "修改出库单和订单-打印状态")
    @PostMapping("v1/updateOutStoreOrderStatus")
    public ComResponse updateOutStoreOrderPrintStatus(@RequestBody List<OrderNoPrintStatusVo> orderNoPrintStatusVoList){
        return removeStockFeignService.updateOutStoreOrderPrintStatus(orderNoPrintStatusVoList);
    }




}
