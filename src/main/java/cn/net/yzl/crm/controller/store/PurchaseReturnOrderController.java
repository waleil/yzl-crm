package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.client.store.PurchaseReturnFeginService;
import cn.net.yzl.model.dto.PurchaseReturnDetailDto;
import cn.net.yzl.model.dto.PurchaseReturnOrderDto;
import cn.net.yzl.model.dto.PurchaseReturnReviewDto;
import cn.net.yzl.model.vo.PurchaseReturnCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangshuaidong
 * @version 1.0
 * @date 2021/1/14 17:11
 */
@Api(value = "采购退货单管理", tags = {"采购退货单管理"})
@RequestMapping("purchaseReturn")
@RestController
@Slf4j
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
    public ComResponse page(@RequestBody PurchaseReturnCondition purchaseReturnCondition){
        return purchaseReturnFeginService.page(purchaseReturnCondition);
    }

    @ApiOperation(value = "采购退货单新增")
    @PostMapping("v1/add")
    public ComResponse add(@RequestBody PurchaseReturnOrderDto purchaseReturnOrderDto){
        log.info("采购退货单新增参数 add:{}", JsonUtil.toJsonStr(purchaseReturnOrderDto));
        List<PurchaseReturnDetailDto> returnDetailList = purchaseReturnOrderDto.getReturnDetailList();
        if(CollectionUtils.isEmpty(returnDetailList)){
            return ComResponse.fail(ResponseCodeEnums.SAVE_FAIL);
        }
        return purchaseReturnFeginService.add(purchaseReturnOrderDto);
    }

    @ApiOperation(value = "采购退货单更新")
    @PostMapping("v1/update")
    public ComResponse update(@RequestBody PurchaseReturnOrderDto purchaseReturnOrderDto){
        log.info("采购退货单更新参数 update:{}", JsonUtil.toJsonStr(purchaseReturnOrderDto));
        if(purchaseReturnOrderDto == null || purchaseReturnOrderDto.getId()==null
                || CollectionUtils.isEmpty(purchaseReturnOrderDto.getReturnDetailList())){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        return purchaseReturnFeginService.update(purchaseReturnOrderDto);
    }

    @ApiOperation(value = "采购退货单审核")
    @PostMapping("v1/review")
    public ComResponse review(@RequestBody PurchaseReturnReviewDto purchaseReturnReviewDto){
        log.info("采购退货单审核参数:{}", JsonUtil.toJsonStr(purchaseReturnReviewDto));
        if(purchaseReturnReviewDto == null || purchaseReturnReviewDto.getId()==null){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        return purchaseReturnFeginService.review(purchaseReturnReviewDto);
    }

    @ApiOperation(value = "采购退货单详情")
    @ApiImplicitParam(name = "id", value = "采购退货单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("v1/detail")
    public ComResponse detail(@RequestParam("id") Integer id){
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
    public ComResponse reviewPage(@RequestBody PurchaseReturnCondition purchaseReturnCondition){
        return purchaseReturnFeginService.reviewPage(purchaseReturnCondition);
    }

}
