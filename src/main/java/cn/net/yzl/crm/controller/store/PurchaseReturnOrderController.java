package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.PurchaseReturnFeginService;
import cn.net.yzl.model.dto.PurchaseReturnResDto;
import cn.net.yzl.model.dto.PurchaseReturnReviewDto;
import cn.net.yzl.model.dto.PurchaseReturnWaybillDto;
import cn.net.yzl.model.vo.PurReturnOrderAddVo;
import cn.net.yzl.model.vo.PurReturnOrderUpdateVo;
import cn.net.yzl.model.vo.PurchaseReturnCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangshuaidong
 * @version 1.0
 * @date 2021/1/14 17:11
 */
@Api(value = "仓储中心心心心心-采购退货单管理", tags = {"仓储中心心心心心-采购退货单管理"})
@RequestMapping("purchaseReturn")
@RestController
public class PurchaseReturnOrderController {


    @Autowired
    private PurchaseReturnFeginService purchaseReturnFeginService;

    /**
     * 采购退货单分页查询
     * @param purchaseReturnCondition
     * @return
     */
    @ApiOperation(value = "采购退货单分页列表", notes = "采购退货单分页列表")
    @PostMapping("v1/page")
    public ComResponse<Page<PurchaseReturnResDto>> page(@RequestBody PurchaseReturnCondition purchaseReturnCondition){
        return purchaseReturnFeginService.page(purchaseReturnCondition);
    }

    @ApiOperation(value = "采购退货单新增")
    @PostMapping("v1/add")
    public ComResponse add(@RequestBody PurReturnOrderAddVo purReturnOrderAddVo){
        return purchaseReturnFeginService.add(purReturnOrderAddVo);
    }

    @ApiOperation(value = "采购退货单更新")
    @PostMapping("v1/update")
    public ComResponse update(@RequestBody PurReturnOrderUpdateVo returnOrderUpdateVo){
        return purchaseReturnFeginService.update(returnOrderUpdateVo);
    }

    @ApiOperation(value = "采购退货单审核")
    @PostMapping("v1/review")
    public ComResponse review(@RequestBody PurchaseReturnReviewDto purchaseReturnReviewDto){
        return purchaseReturnFeginService.review(purchaseReturnReviewDto);
    }

    @ApiOperation(value = "采购退货单详情")
    @ApiImplicitParam(name = "id", value = "采购退货单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("v1/detail")
    public ComResponse<PurchaseReturnResDto> detail(@RequestParam("id") Integer id){
        return purchaseReturnFeginService.detail(id);
    }



    /**
     * 采购退货单状态下拉框列表
     * @Author wangshuaidong
     * @return
     */
    @ApiOperation(value = "采购退货单状态下拉框列表")
    @GetMapping("v1/status/list")
    public ComResponse purchaseReturnStatus() {
        return purchaseReturnFeginService.purchaseReturnStatus();
    }

    /**
     * 采购订单审核列表分页查询
     * @param purchaseReturnCondition
     * @return
     */
    @ApiOperation(value = "采购退货单审核列表", notes = "采购退货单审核列表")
    @PostMapping("v1/review/page")
    public ComResponse<Page<PurchaseReturnResDto>> reviewPage(@RequestBody PurchaseReturnCondition purchaseReturnCondition){
        return purchaseReturnFeginService.reviewPage(purchaseReturnCondition);
    }
    /**
     * 采购退货单审核列表
     * @param returnWaybillDtos
     * @return
     */
    @ApiOperation(value = "采购退货单退回添加货运单号")
    @PostMapping("v1/back/waybill/")
    public ComResponse backWayBill(@RequestBody List<PurchaseReturnWaybillDto> returnWaybillDtos){
        return purchaseReturnFeginService.backWayBill(returnWaybillDtos);
    }
}
