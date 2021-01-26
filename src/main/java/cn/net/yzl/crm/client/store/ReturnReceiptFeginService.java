package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.*;
import cn.net.yzl.model.vo.ReturnReceiptCondition;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "returnReceiptService",url = "${api.gateway.url}/storeServer")
public interface ReturnReceiptFeginService {

    /**
     * 确认收货
     * @param addList
     * @return
     */
    @ApiOperation(value = "确认收货", notes = "确认收货")
    @PostMapping("returnReceipt/v1/confirm/receipt")
    ComResponse confirmReceipt(@RequestBody List<ReturnToStoreAddDto> addList);

    /**
     * 拒绝收货
     * @param refuseReturnDto
     * @return
     */
    @ApiOperation(value = "拒绝收货", notes = "拒绝收货")
    @PostMapping("returnReceipt/v1/refuse/return")
    ComResponse refuseReturn(@RequestBody RefuseReturnDto refuseReturnDto);


    /**
     * 退货入库单详情
     * @param id
     * @return
     */
    @ApiOperation(value = "退货入库单详情", notes = "退货入库单详情")
    @ApiImplicitParam(name = "id", value = "退货入库单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("returnReceipt/v1/detail")
    ComResponse<ReturnReceiptDetailDto> detail(@RequestParam("id") Integer id);



    /**
     * 退货入库单详情
     * @param returnReceiptCondition
     * @return
     */
    @ApiOperation(value = "退货入库单列表", notes = "退货入库单列表")
    @PostMapping("returnReceipt/v1/page")
    ComResponse<Page<ReturnReceiptPageDto>> page(@RequestBody ReturnReceiptCondition returnReceiptCondition);

    @ApiOperation(value = "退货入库单类型列表")
    @GetMapping("returnReceipt/v1/type/list")
    ComResponse<List<Map<String,Object>>> returnTypeList();



    /**
     * 扫描商品
     * @param returnGoodsScanDto
     * @return
     */
    @ApiOperation(value = "扫描商品", notes = "扫描商品")
    @PostMapping("returnReceipt/v1/scanning/product")
    ComResponse<List<ReturnToStoreDto>> scanningProduct(@RequestBody ReturnGoodsScanDto returnGoodsScanDto);


}
