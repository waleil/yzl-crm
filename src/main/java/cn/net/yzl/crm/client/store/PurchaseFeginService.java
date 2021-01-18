package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.dto.PurchaseOrderDto;
import cn.net.yzl.model.dto.WarehousingOrderDto;
import cn.net.yzl.model.vo.PurchaseOrderCondition;
import cn.net.yzl.model.vo.PurchaseOrderVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author wangshuaidong
 * @version 1.0
 */
@FeignClient(name = "purchaseService",url = "${api.gateway.url}/storeServer")
public interface PurchaseFeginService {
    /**
     * 采购订单分页查询
     * @param purchaseOrderCondition
     * @return
     */
    @ApiOperation(value = "采购订单列表", notes = "采购订单列表")
    @PostMapping("purchase/v1/page")
    ComResponse page(@RequestBody PurchaseOrderCondition purchaseOrderCondition);

    /**
     * 新增采购订单
     * @param purchaseOrderVo
     * @return
     */
    @ApiOperation(value = "新增采购订单", notes = "新增采购订单")
    @PostMapping("purchase/v1/add")
    ComResponse add(@RequestBody @Valid PurchaseOrderVo purchaseOrderVo) ;


    /**
     * 修改采购订单
     * @param purchaseOrderVo
     * @return
     */
    @ApiOperation(value = "修改采购订单", notes = "修改采购订单")
    @PostMapping("purchase/v1/update")
    ComResponse update(@RequestBody @Valid PurchaseOrderVo purchaseOrderVo);


    /**
     * 查看采购订单
     * @param id
     * @return
     */
    @ApiOperation(value = "查看采购订单", notes = "查看采购订单")
    @ApiImplicitParam(name = "id", value = "采购订单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("purchase/v1/detail")
    ComResponse detail(@RequestParam("id") Integer id);


    /**
     * 审核采购订单
     * @Author wangshuaidong
     * @param purchaseOrderVo
     * @return
     */
    @ApiOperation(value = "审核采购订单")
    @PostMapping("purchase/v1/review")
    ComResponse purchaseOrderReview(@RequestBody PurchaseOrderVo purchaseOrderVo);

    /**
     * 采购订单验收
     * @Author wangshuaidong
     * @param warehousingOrderDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "采购订单验收")
    @PostMapping("purchase/v1/check/accept")
    ComResponse purchaseCheckAccept(@RequestBody @Valid WarehousingOrderDto warehousingOrderDto);

    /**
     * 采购订单撤回
     * @Author wangshuaidong
     * @param purchaseOrderDto
     * @return
     */
    @ApiOperation(value = "采购订单撤回")
    @PostMapping("purchase/v1/withdraw")
    ComResponse purchaseWithdraw(@RequestBody PurchaseOrderDto purchaseOrderDto) ;

    @ApiOperation(value = "采购订单状态下拉框列表")
    @GetMapping("purchase/v1/status/list")
    ComResponse purchaseStatus() ;
}
