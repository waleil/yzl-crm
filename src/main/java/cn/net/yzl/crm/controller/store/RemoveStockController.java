package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.RemoveStockFeignService;
import cn.net.yzl.model.dto.RemoveStockDto;
import cn.net.yzl.model.dto.RemoveStockManageDto;
import cn.net.yzl.model.dto.StoreToLogisticsDto;
import cn.net.yzl.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/25 21:36
 */
@RestController
@Slf4j
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
    public ComResponse createOutStoreOrder(@RequestBody List<OutStoreOrderVo> outStoreOrderVoList){
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

    @ApiOperation(value = "物流-快递运单查询或运单异常登记")
    @PostMapping("v1/selectOutStoreToLogistics")
    public ComResponse<Page<StoreToLogisticsDto>> selectOutStoreToLogistics(@RequestBody StoreToLogisticsParamVo storeToLogisticsParamVo){
        return removeStockFeignService.selectOutStoreToLogistics(storeToLogisticsParamVo);
    }


    @ApiOperation(value = "物流-修改订单状态和收据状态")
    @PostMapping("v1/updateOutStoreToLogistics")
    public ComResponse updateOutStoreToLogistics(@RequestBody LogisticsToStoreUpdateParam logisticsToStoreUpdateParam){
        return removeStockFeignService.updateOutStoreToLogistics(logisticsToStoreUpdateParam);

    }





}
