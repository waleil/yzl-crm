package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.*;
import cn.net.yzl.model.vo.PurchaseOrderAddVo;
import cn.net.yzl.model.vo.PurchaseOrderCondition;
import cn.net.yzl.model.vo.PurchaseOrderUpdateVo;
import cn.net.yzl.model.vo.PurchaseOrderVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author wangshuaidong
 * @version 1.0
 */
@FeignClient(name = "purchaseService",url = "${api.gateway.url}/storeServer")
public interface PurchaseFeginService {
    /**
     * 采购订单分页查询
     * @author wangshuaidong
     * @param purchaseOrderCondition
     * @return
     */
    @ApiOperation(value = "采购订单列表", notes = "采购订单列表")
    @PostMapping("purchase/v1/page")
    ComResponse<Page<PurchaseOrderDto>> page(@RequestBody PurchaseOrderCondition purchaseOrderCondition);

    /**
     * 新增采购订单
     * @author wangshuaidong
     * @param purchaseOrderaddVo
     * @return
     */
    @ApiOperation(value = "新增采购订单", notes = "新增采购订单")
    @PostMapping("purchase/v1/add")
    ComResponse add(@RequestBody PurchaseOrderAddVo purchaseOrderaddVo) ;


    /**
     * 修改采购订单
     * @author wangshuaidong
     * @param purchaseOrderUpdateVo
     * @return
     */
    @ApiOperation(value = "修改采购订单", notes = "修改采购订单")
    @PostMapping("purchase/v1/update")
    ComResponse update(@RequestBody PurchaseOrderUpdateVo purchaseOrderUpdateVo);


    /**
     * 查看采购订单
     * @author wangshuaidong
     * @param id
     * @return
     */
    @ApiOperation(value = "查看采购订单", notes = "查看采购订单")
    @ApiImplicitParam(name = "id", value = "采购订单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("purchase/v1/detail")
    ComResponse<PurchaseOrderResDto> detail(@RequestParam("id") Integer id);


    /**
     * 审核采购订单
     * @author wangshuaidong
     * @param purchaseReviewDto
     * @return
     */
    @ApiOperation(value = "审核采购订单")
    @PostMapping("purchase/v1/review")
    ComResponse purchaseOrderReview(@RequestBody PurchaseReviewDto purchaseReviewDto);

    /**
     * 采购订单验收
     * @author wangshuaidong
     * @param warehousingOrderDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "采购订单验收")
    @PostMapping("purchase/v1/check/accept")
    ComResponse purchaseCheckAccept(@RequestBody WarehousingOrderDto warehousingOrderDto);

    /**
     * 采购订单撤回
     * @author wangshuaidong
     * @param purchaseWithdrawDto
     * @return
     */
    @ApiOperation(value = "采购订单撤回")
    @PostMapping("purchase/v1/withdraw")
    ComResponse purchaseWithdraw(@RequestBody PurchaseWithdrawDto purchaseWithdrawDto) ;

    @ApiOperation(value = "采购订单状态下拉框列表")
    @GetMapping("purchase/v1/status/list")
    ComResponse<List<Map<String,Object>>> purchaseStatus() ;

    /**
     * 采购订单审核列表分页查询
     * @author wangshuaidong
     * @param purchaseOrderCondition
     * @return
     */
    @ApiOperation(value = "采购订单审核列表", notes = "采购订单审核列表")
    @PostMapping("purchase/v1/review/page")
    ComResponse<Page<PurchaseOrderDto>> reviewPage(@RequestBody PurchaseOrderCondition purchaseOrderCondition);

    @ApiOperation(value = "采购订单预计到货日期", notes = "采购订单预计到货日期")
    @ApiImplicitParam(name = "supplierNo", value = "供应商编码", required = true, dataType = "String", paramType = "query")
    @GetMapping("purchase/v1/computer/expect/date")
    ComResponse<String> computerExpectDate(@RequestParam(value = "supplierNo") String supplierNo);


    @ApiOperation(value = "采购单号查找退货信息", notes = "采购单号查找退货信息")
    @ApiImplicitParam(name = "orderNo", value = "采购订单号", required = true, dataType = "String", paramType = "query")
    @GetMapping("purchase/v1/detail/by/order/no")
    ComResponse<PurchaseReturnResDto> detailByOrderNo(@RequestParam("orderNo") String orderNo);
}
