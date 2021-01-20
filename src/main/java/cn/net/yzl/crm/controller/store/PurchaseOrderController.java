package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.store.PurchaseFeginService;
import cn.net.yzl.model.dto.PurchaseOrderDto;
import cn.net.yzl.model.dto.PurchaseReviewDto;
import cn.net.yzl.model.dto.PurchaseWithdrawDto;
import cn.net.yzl.model.dto.WarehousingOrderDto;
import cn.net.yzl.model.vo.PurchaseOrderCondition;
import cn.net.yzl.model.vo.PurchaseOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangshuaidong
 * @version 1.0
 * @date 2021/1/14 17:11
 */
@Api(value = "采购订单管理", tags = {"采购订单管理"})
@RequestMapping("purchase")
@RestController
@Slf4j
public class PurchaseOrderController {


    @Autowired
    private PurchaseFeginService purchaseFeginService;

    @ApiOperation(value = "采购订单列表", notes = "采购订单列表")
    @PostMapping("v1/page")
    public ComResponse page(@RequestBody PurchaseOrderCondition purchaseOrderCondition){
        return purchaseFeginService.page(purchaseOrderCondition);
    }

    @ApiOperation(value = "新增采购订单", notes = "新增采购订单")
    @PostMapping("v1/add")
    public ComResponse add(@RequestBody PurchaseOrderVo purchaseOrderVo)  {
        return purchaseFeginService.add(purchaseOrderVo);
    }


    @ApiOperation(value = "修改采购订单", notes = "修改采购订单")
    @PostMapping("v1/update")
    public ComResponse update(@RequestBody PurchaseOrderVo purchaseOrderVo){
        return purchaseFeginService.update(purchaseOrderVo);
    }


    @ApiOperation(value = "查看采购订单", notes = "查看采购订单")
    @ApiImplicitParam(name = "id", value = "采购订单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("v1/detail")
    public ComResponse detail(@RequestParam("id") Integer id){
        return purchaseFeginService.detail(id);
    }


    @ApiOperation(value = "审核采购订单")
    @PostMapping("v1/review")
    public ComResponse purchaseOrderReview(@RequestBody PurchaseReviewDto purchaseReviewDto){
        return purchaseFeginService.purchaseOrderReview(purchaseReviewDto);
    }

    @ApiOperation(value = "采购订单验收")
    @PostMapping("v1/check/accept")
    public ComResponse purchaseCheckAccept(@RequestBody WarehousingOrderDto warehousingOrderDto){
        return purchaseFeginService.purchaseCheckAccept(warehousingOrderDto);
    }

    @ApiOperation(value = "采购订单撤回")
    @PostMapping("v1/withdraw")
    public ComResponse purchaseWithdraw(@RequestBody PurchaseWithdrawDto purchaseWithdrawDto) {
        return purchaseFeginService.purchaseWithdraw(purchaseWithdrawDto);
    }

    @ApiOperation(value = "采购订单状态下拉框列表")
    @GetMapping("v1/status/list")
    public ComResponse purchaseStatus() {
        return purchaseFeginService.purchaseStatus();
    }


    @ApiOperation(value = "采购订单审核列表", notes = "采购订单审核列表")
    @PostMapping("v1/review/page")
    public ComResponse reviewPage(@RequestBody PurchaseOrderCondition purchaseOrderCondition){
        return purchaseFeginService.reviewPage(purchaseOrderCondition);
    }


}
