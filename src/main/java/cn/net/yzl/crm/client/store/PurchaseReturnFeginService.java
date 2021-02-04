package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.PurchaseReturnResDto;
import cn.net.yzl.model.dto.PurchaseReturnReviewDto;
import cn.net.yzl.model.dto.PurchaseReturnWaybillDto;
import cn.net.yzl.model.dto.purchase.returns.*;
import cn.net.yzl.model.vo.PurchaseReturnCondition;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author wangshuaidong
 * @version 1.0
 */
@FeignClient(name = "purchaseReturnService",url = "${api.gateway.url}/storeServer")
public interface PurchaseReturnFeginService {


    /**
     * 采购退货单分页查询
     * @param purchaseReturnCondition
     * @return
     */
    @ApiOperation(value = "采购退货单分页列表", notes = "采购退货单分页列表")
    @PostMapping("purchaseReturn/v1/page")
    ComResponse<Page<PurReturnNotMerge>> page(@RequestBody PurchaseReturnCondition purchaseReturnCondition);

    @ApiOperation(value = "采购退货单新增")
    @PostMapping("purchaseReturn/v1/add")
    ComResponse add(@RequestBody PurchaseReturnOrderAddDto purchaseReturnOrderAddDto);

    @ApiOperation(value = "采购退货单更新")
    @PostMapping("purchaseReturn/v1/update")
    ComResponse update(@RequestBody PurchaseReturnUpdateDto purchaseReturnUpdateDto);

    @ApiOperation(value = "采购退货单审核")
    @PostMapping("purchaseReturn/v1/review")
    ComResponse review(@RequestBody @Valid PurchaseReturnReviewDto purchaseReturnReviewDto);

    @ApiOperation(value = "采购退货单详情")
    @ApiImplicitParam(name = "id", value = "采购退货单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("purchaseReturn/v1/detail")
    ComResponse<PurReturnEditNotMergeDto> detail(@RequestParam("id") Integer id);



    /**
     * 采购退货单状态下拉框列表
     * @Author wangshuaidong
     * @return
     */
    @ApiOperation(value = "采购退货单状态下拉框列表")
    @GetMapping("purchaseReturn/v1/status/list")
    ComResponse<List<Map<String,Object>>> purchaseReturnStatus() ;

    /**
     * 采购退货单审核列表
     * @param purchaseReturnCondition
     * @return
     */
    @ApiOperation(value = "采购退货单审核列表")
    @PostMapping("purchaseReturn/v1/review/page")
    ComResponse<Page<PurchaseReturnResDto>>  reviewPage(@RequestBody PurchaseReturnCondition purchaseReturnCondition);

    /**
     * 采购退货单审核列表
     * @param waybillAddDto
     * @return
     */
    @ApiOperation(value = "采购退货单退回添加物流信息")
    @PostMapping("purchaseReturn/v1/add/waybill")
    ComResponse addWayBill(@RequestBody WaybillAddDto waybillAddDto);

    /**
     * 采购退货单审核列表
     * @param waybillUpdateDto
     * @return
     */
    @ApiOperation(value = "采购退货单编辑物流信息")
    @PostMapping("purchaseReturn/v1/update/waybill")
    ComResponse updateWayBill(@RequestBody WaybillUpdateDto waybillUpdateDto);

    @ApiOperation(value = "退货物流信息详情", notes = "退货物流信息详情")
    @ApiImplicitParam(name = "id", value = "采购退货单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("purchaseReturn/v1/waybill/detail")
    ComResponse<WaybillDetailDto> selectWaybill(@RequestParam("id") Integer id);

}
