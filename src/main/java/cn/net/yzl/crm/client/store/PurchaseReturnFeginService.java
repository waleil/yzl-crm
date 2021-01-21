package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.PurchaseReturnOrderDto;
import cn.net.yzl.model.dto.PurchaseReturnResDto;
import cn.net.yzl.model.dto.PurchaseReturnReviewDto;
import cn.net.yzl.model.vo.PurReturnOrderAddVo;
import cn.net.yzl.model.vo.PurReturnOrderUpdateVo;
import cn.net.yzl.model.vo.PurchaseReturnCondition;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    ComResponse<Page<PurchaseReturnResDto>> page(@RequestBody PurchaseReturnCondition purchaseReturnCondition);

    @ApiOperation(value = "采购退货单新增")
    @PostMapping("purchaseReturn/v1/add")
    ComResponse add(@RequestBody PurReturnOrderAddVo purReturnOrderAddVo);

    @ApiOperation(value = "采购退货单更新")
    @PostMapping("purchaseReturn/v1/update")
    ComResponse update(@RequestBody PurReturnOrderUpdateVo purReturnOrderUpdateVo);

    @ApiOperation(value = "采购退货单审核")
    @PostMapping("purchaseReturn/v1/review")
    ComResponse review(@RequestBody PurchaseReturnReviewDto purchaseReturnReviewDto);

    @ApiOperation(value = "采购退货单详情")
    @ApiImplicitParam(name = "id", value = "采购退货单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("purchaseReturn/v1/detail")
    ComResponse<PurchaseReturnResDto> detail(@RequestParam("id") Integer id);



    /**
     * 采购退货单状态下拉框列表
     * @Author wangshuaidong
     * @return
     */
    @ApiOperation(value = "采购退货单状态下拉框列表")
    @GetMapping("purchaseReturn/v1/status/list")
    ComResponse purchaseReturnStatus();

    /**
     * 采购订单审核列表分页查询
     * @param purchaseReturnCondition
     * @return
     */
    @ApiOperation(value = "采购退货单审核列表", notes = "采购退货单审核列表")
    @PostMapping("purchaseReturn/v1/review/page")
    ComResponse<Page<PurchaseReturnResDto>> reviewPage(@RequestBody PurchaseReturnCondition purchaseReturnCondition);
}
